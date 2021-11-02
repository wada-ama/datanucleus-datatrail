package mydomain.datanucleus.datatrail;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import mydomain.audit.DataTrail;
import org.datanucleus.enhancement.Persistable;
import org.datanucleus.metadata.MetaData;
import org.datanucleus.state.ObjectProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Singleton factory responsible for instantiating a node type based on requirements
 */
public class DataTrailFactory {
    // get a static slf4j logger for the class
    protected static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DataTrailFactory.class);

    static final private DataTrailFactory instance = new DataTrailFactory();

    private Set<NodeFactory> factories = ConcurrentHashMap.newKeySet();

    /**
     * Protected constructor
     */
    private DataTrailFactory() {

    }


    /**
     * Automatically scans for any non-abstract {@link NodeFactory} implementations and attempts to initialize them
     * @param classPackageToScan package of class to scan for any implementations
     */
    public void registerNodeFactories(Class<?> classPackageToScan){
        synchronized (this) {
            try (ScanResult scanResult = new ClassGraph()
                    .enableAnnotationInfo()
                    .acceptPackages(classPackageToScan.getPackage().getName())
                    .scan()) {

                scanResult.getClassesImplementing(NodeFactory.class)
                        .filter(classInfo -> !classInfo.isAbstract())
                        .stream()
                        .map(ClassInfo::loadClass)
                        .forEach(clazz -> registerFactory((Class<NodeFactory>) clazz));
            }
        }
    }


    /**
     * Register a {@link NodeFactory} to the DataTrail factory.  This will make all the types exposed by the factory class
     * accessible to the DataTrail factory.  The {@link NodeFactory} class must supply a public constructor which takes a {@link DataTrailFactory} object
     * as a parameter
     * @param factory
     */
    public void registerFactory(Class<NodeFactory> factory){
        synchronized (this) {
            try {
                Constructor ctor = factory.getConstructor(DataTrailFactory.class);
                factories.add((NodeFactory) ctor.newInstance(this));
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Retrieve an instance of the factory
     * By default, automatically add any {@link NodeFactory} found in any subpackage of {@link BaseNode}
     * @return
     */
    static public DataTrailFactory getDataTrailFactory() {
        return getDataTrailFactory(BaseNode.class);
    }

    /**
     * Retrieve an instance of the factory
     * @return
     */
    static public DataTrailFactory getDataTrailFactory(Class<?> classPackageToScan) {
        DataTrailFactory factory = new DataTrailFactory();
        factory.registerNodeFactories(classPackageToScan);
        return factory;
    }

    /**
     * Retrieve a root node from the factory. Should only be used for persistable objects
     * @param value
     * @param action
     * @return
     */
    public BaseNode createNode(Object value, NodeAction action){
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


    /**
     * Retrieve a node from the factory
     * @param value
     * @param md
     * @param parent
     * @return
     * @throws RuntimeException if unable to create the node
     */
    public BaseNode createNode(Object value, NodeAction action, MetaData md, BaseNode parent){

        // find the factory for this type of value
        NodeFactory factory = factories.stream().filter(nodeFactory -> nodeFactory.supports(action, value, md))
                .sorted(Comparator.comparingInt(NodeFactory::priority))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No type found to support: " + value.getClass().getCanonicalName() + " / " + action));


        // create a node for this value
        BaseNode node = factory.create(action, value, md, parent)
                .orElseThrow(() -> new IllegalArgumentException("Factory unable to support: " + value.getClass().getCanonicalName() + " / " + action));

        return node;
    }

}
