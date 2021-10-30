package mydomain.datanucleus.datatrail;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import mydomain.audit.DataTrail;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import org.datanucleus.enhancement.Persistable;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;
import org.datanucleus.state.ObjectProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.datanucleus.util.ClassUtils.getConstructorWithArguments;


/**
 * Singleton factory responsible for instantiating a node type based on requirements
 */
public class NodeFactory {
    // get a static slf4j logger for the class
    protected static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(NodeFactory.class);

    static final private NodeFactory instance = new NodeFactory();

    private Map<Node.Action,Map<NodeType, Class<? extends Node>>> nodeTypes = new HashMap<>();

    private NodeFactory() {
        // initialize the map
        Arrays.stream(Node.Action.values()).forEach(action -> nodeTypes.put(action, new HashMap<>()));

        // scan through all availabe nodes and add them to the list
        registerNodes();
    }

    /**
     * Automatically scans and registers any {@link NodeDefinition} classes found in the same package or below from {@link Node}
     */
    private void registerNodes(){
        try (ScanResult scanResult = new ClassGraph()
                .enableAnnotationInfo()
                .acceptPackages(Node.class.getPackage().getName())
                .scan()){

            scanResult.getClassesWithAnnotation(NodeDefinition.class).stream().forEach(classInfo -> {
                Class clazz = classInfo.loadClass();
                NodeDefinition nodeDefn = (NodeDefinition)clazz.getAnnotation(NodeDefinition.class);
                registerNodeType(nodeDefn.type(), nodeDefn.action(), clazz);
            });
        }
    }


    /**
     * Registers an implementation of a {@link NodeType}.  The implementation must be a subclass {@link Node}
     * @param type the type of node represented
     * @param action the action occuring on the
     * @param clazz the implementation class
     */
    public void registerNodeType( NodeType type,  Node.Action action, Class<? extends Node> clazz){
        nodeTypes.get(action).put(type, clazz);
    }


    /**
     * Retrieve the singleton
     * @return
     */
    static public NodeFactory getInstance() {
        return instance;
    }


    /**
     * Retrieve a root node from the factory. Should only be used for persistable objects
     * @param value
     * @param action
     * @return
     */
    public Node createRootNode(Object value, Node.Action action){
        // make sure it is a persistable object
        if( !( value instanceof Persistable) ) {
            return null;
        }

        // check if the object should be excluded from the data trail
        if( value.getClass().getAnnotation(DataTrail.class) != null && value.getClass().getAnnotation(DataTrail.class).excludeFromDataTrail()){
            // this element should be excluded, so skip it
            logger.debug("{} is marked as excluded from the datatrail", value.getClass().getCanonicalName());
            return null;
        }


        MetaData md = ((ObjectProvider)((Persistable)value).dnGetStateManager()).getClassMetaData();


        return createNode(value, action, md, null);
    }


        // TODO Fix the exception handling when calling the constructor
    /**
     * Retrieve a node from the factory
     * @param value
     * @param md
     * @param parent
     * @return
     * @throws RuntimeException if unable to create the node
     */
    public Node createNode(Object value, Node.Action action, MetaData md, Node parent){
        // get the map by action
        Map<NodeType, Class<? extends Node>> nodeClass = nodeTypes.get(action);
        NodeType type = getType(value, md, parent);
        Class clazz = nodeClass.get(type);
        if( clazz == null ){
            throw new IllegalArgumentException("No such type/action supported: " + type + " / " + action);
        }

        Constructor ctor = getConstructorWithArguments(clazz, new Class[]{ClassUtils.getClass(value), ClassUtils.getClass(md), ClassUtils.getClass(parent)});
        try {
            return (Node)ctor.newInstance(value, md, parent);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    // TODO move this logic to the individual Node implementation to determine if they can handle it or not
    /**
     * Determine the type of node needed for the given object
     * @param value
     * @return
     */
    private NodeType getType(Object value, MetaData md, Node parent){
        if( parent == null ){
            // by definition, the root object MUST be an Entity type
            return NodeType.ENTITY;
        }

        // anything other than the root must have field metadata
        AbstractMemberMetaData mmd = (AbstractMemberMetaData)md;

        // if the object is a peristable object, it must be a REF
        if( value instanceof Persistable ){
            return NodeType.REF;
        }

        // if the value is not a persistable object, need to find if it is a special container (ie: collection / Map)
        // mmd can be null if the node is part of a container object

        // TODO need to figure out how to identify MMD for individual container fields
        if( mmd != null ) {
            if( Persistable.class.isAssignableFrom(mmd.getType())){
                return NodeType.REF;
            }

            if (mmd.hasArray()) {
                logger.warn("Unable to track changes to objects with arrays. {}.{}", mmd.getClassName(), mmd.getName());
                return NodeType.ARRAY;
            }

            if (mmd.hasCollection()) {
                return NodeType.COLLECTION;
            }

            if (mmd.hasMap()) {
                return NodeType.MAP;
            }
        }

        // default case, treat as primitive field
        return NodeType.PRIMITIVE;
    }

}
