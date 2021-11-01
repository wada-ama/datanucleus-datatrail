package mydomain.datanucleus.datatrail;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import mydomain.audit.DataTrail;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.datatrail.nodes.NodePriority;
import org.datanucleus.enhancement.Persistable;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;
import org.datanucleus.state.ObjectProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.datanucleus.util.ClassUtils.getConstructorWithArguments;


/**
 * Singleton factory responsible for instantiating a node type based on requirements
 */
public class NodeFactory {
    // get a static slf4j logger for the class
    protected static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(NodeFactory.class);

    static final private NodeFactory instance = new NodeFactory();

    private List<mydomain.datanucleus.datatrail.nodes.NodeFactory> factories = new ArrayList<>();

    private NodeFactory() {
        // scan through all availabe nodes and add them to the list
        registerNodeFactories();
    }


    private void registerNodeFactories(){
        try (ScanResult scanResult = new ClassGraph()
                .enableAnnotationInfo()
                .acceptPackages(Node.class.getPackage().getName())
                .scan()) {

            scanResult.getClassesImplementing(mydomain.datanucleus.datatrail.nodes.NodeFactory.class).stream()
                    .map(ClassInfo::loadClass)
                    .forEach(clazz -> {
                        try {
                            factories.add((mydomain.datanucleus.datatrail.nodes.NodeFactory) clazz.newInstance());
                        } catch (InstantiationException | IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

//
//    /**
//     * Automatically scans and registers any {@link NodeDefinition} classes found in the same package or below from {@link Node}
//     */
//    private void registerNodes(){
//        try (ScanResult scanResult = new ClassGraph()
//                .enableAnnotationInfo()
//                .acceptPackages(Node.class.getPackage().getName())
//                .scan()){
//
//            scanResult.getClassesWithAnnotation(NodeDefinition.class).stream().forEach(classInfo -> {
//                Class clazz = classInfo.loadClass();
//                registerNode(clazz);
//            });
//        }
//    }
//
//    /**
//     * Registers an implementation of a {@link NodeType}.  The implementation must be a subclass {@link Node}
//     * @param clazz the implementation class
//     */
//    public void registerNode(Class<Node> clazz){
//        if( clazz.getAnnotation(NodeDefinition.class) == null ){
//            logger.warn("Unable to register a node without metadata information provided by {}", NodeDefinition.class.getCanonicalName());
//            return;
//        }
//
//        NodeDefinition nodeDefn = (NodeDefinition)clazz.getAnnotation(NodeDefinition.class);
//        NodePriority nodePriority = (NodePriority) clazz.getAnnotation(NodePriority.class);
//        int priority = nodePriority == null ? 0 : nodePriority.priority();
//
//        registerNode(nodeDefn.type(), nodeDefn.action(), clazz, priority);
//    }
//
//
//    /**
//     * Registers an implementation of a {@link NodeType}.  The implementation must be a subclass {@link Node}
//     * @param type the type of node represented
//     * @param action the action occuring on the
//     * @param clazz the implementation class
//     */
//    public void registerNode(NodeType type, Node.Action action, Class<Node> clazz, int priority){
//        nodeTypes.get(action).put(type, clazz);
//    }
//
//
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

        // find the factory for this type of value
        mydomain.datanucleus.datatrail.nodes.NodeFactory factory = factories.stream().filter(nodeFactory -> nodeFactory.supports(value, md))
                .sorted((o1, o2) -> {
                    return o1.priority() - o2.priority();
                })
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No type found to support: " + value.getClass().getCanonicalName() + " / " + action));


        // create a node for this value
        return factory.create(action, value, md, parent)
                .orElseThrow(() -> new IllegalArgumentException("Factory unable to support: " + value.getClass().getCanonicalName() + " / " + action));
    }
//
//
//    private NodeType getType(Object value, MetaData md, Node parent) {
////        nodeTypes.get(Node.Action.UPDATE).values().stream().filter( nodeType -> {
////            ((Node)nodeType).canProcess(value, md);
////        })
//////        return null;
////
//        Arrays.asList(Array.class, Collection.class, Entity.class).stream().filter(aClass -> {
//            Method supports = aClass.getMethod("supports", ...);
//            if(supports == null ) then print "cannot use";
//
//            supports.invoke( aClass, value, md );
////            aClass.supports(value, md)
//        }).findFirst();
//    }

//
//
//    // TODO move this logic to the individual Node implementation to determine if they can handle it or not
//    /**
//     * Determine the type of node needed for the given object
//     * @param value
//     * @return
//     */
//    private NodeType getType(Object value, MetaData md, Node parent){
//        if( parent == null ){
//            // by definition, the root object MUST be an Entity type
//            return NodeType.ENTITY;
//        }
//
//        // anything other than the root must have field metadata
//        AbstractMemberMetaData mmd = (AbstractMemberMetaData)md;
//
//        // if the object is a peristable object, it must be a REF
//        if( value instanceof Persistable ){
//            return NodeType.REF;
//        }
//
//        // if the value is not a persistable object, need to find if it is a special container (ie: collection / Map)
//        // mmd can be null if the node is part of a container object
//
//        // TODO need to figure out how to identify MMD for individual container fields
//        if( mmd != null ) {
//            if( Persistable.class.isAssignableFrom(mmd.getType())){
//                return NodeType.REF;
//            }
//
//            if (mmd.hasArray()) {
//                logger.warn("Unable to track changes to objects with arrays. {}.{}", mmd.getClassName(), mmd.getName());
//                return NodeType.ARRAY;
//            }
//
//            if (mmd.hasCollection()) {
//                return NodeType.COLLECTION;
//            }
//
//            if (mmd.hasMap()) {
//                return NodeType.MAP;
//            }
//        }
//
//        // default case, treat as primitive field
//        return NodeType.PRIMITIVE;
//    }
//


}
