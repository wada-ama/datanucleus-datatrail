package mydomain.datanucleus.types.wrappers.tracker;

import org.datanucleus.test.AbstractTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;

public class MapTestOfChangeTracker extends AbstractTest {


    @DisplayName("Adds multiple values to the map.  Only the final value should be tracked.")
    @Test
    public void testAddedTracker(){

        executeTx( pm -> {
            PrimitiveMapClass sut = new PrimitiveMapClass();
            // make persistent and flush in order to trigger the changeTracket
            pm.makePersistent(sut);
            pm.flush();

            assertThat("A persisted object with a map should be a ChangeTrackable map", sut.getPrimitiveMap(), isA(ChangeTrackable.class));
            ChangeTracker changeTracker =((ChangeTrackable) sut.getPrimitiveMap()).getChangeTracker();

            Map<String, String> map = sut.getPrimitiveMap();
            map.put( "key", "initialValue");
            map.put( "key", "lastValue");

            assertThat( "Should contain zero changed objects", (Collection<Object>)changeTracker.getChanged(), hasSize(0) );
            assertThat( "Should contain zero removed objects", (Collection<Object>)changeTracker.getRemoved(), hasSize(0) );

            assertThat( "Should contain one added object", (Collection<Object>)changeTracker.getAdded(), hasSize(1) );
            assertThat( "Should contain one added object of type SimpleEntry", (Collection<Object>)changeTracker.getAdded(), hasItems(isA(SimpleEntry.class) ));

            assertThat( "SimpleEntry should be the last value added", (Collection<Object>)changeTracker.getAdded(), hasItems( new SimpleEntry("key", "lastValue")) );
        });
    }

    @DisplayName("Put and remove values in the map in the same tx.  Nothing should be recorded.")
    @Test
    public void testRemovedTracker(){

        executeTx( pm -> {
            PrimitiveMapClass sut = new PrimitiveMapClass();
            // make persistent and flush in order to trigger the changeTracket
            pm.makePersistent(sut);
            pm.flush();

            assertThat("A persisted object with a map should be a ChangeTrackable map", sut.getPrimitiveMap(), isA(ChangeTrackable.class));
            ChangeTracker changeTracker =((ChangeTrackable) sut.getPrimitiveMap()).getChangeTracker();


            Map<String, String> map = sut.getPrimitiveMap();

            map.put( "key", "initialValue");
            map.remove( "key" );



            assertThat( "Should contain zero changed objects", (Collection<Object>)changeTracker.getChanged(), hasSize(0) );
            assertThat( "Should contain zero removed objects", (Collection<Object>)changeTracker.getRemoved(), hasSize(0) );
            assertThat( "Should contain zero added objects", (Collection<Object>)changeTracker.getAdded(), hasSize(0) );
        });
    }


    @DisplayName("Change a value in the map multiple times.  The initial value prior to the first change should be recorded.")
    @Test
    public void testChangedTracker(){

        executeTx( pm -> {
            PrimitiveMapClass sut = new PrimitiveMapClass();
            // make persistent and flush in order to trigger the changeTracket
            sut.getPrimitiveMap().put("key", "initialValue");
            pm.makePersistent(sut);
            }, false);


        executeTx( pm ->{
            PrimitiveMapClass sut = pm.newJDOQLTypedQuery(PrimitiveMapClass.class).executeUnique();
            Map<String, String> map = sut.getPrimitiveMap();

            assertThat("A persisted object with a map should be a ChangeTrackable map", map, isA(ChangeTrackable.class));
            ChangeTracker changeTracker = ((ChangeTrackable) map).getChangeTracker();

            map.put( "key", "modifiedValue");
            map.put( "key", "lastValue");

            assertThat( "Should contain 1 changed objects", (Collection<Object>)changeTracker.getChanged(), hasSize(1) );
            assertThat( "Should contain zero removed objects", (Collection<Object>)changeTracker.getRemoved(), hasSize(0) );
            assertThat( "Should contain zero added object", (Collection<Object>)changeTracker.getAdded(), hasSize(0) );

            assertThat( "Should contain one changed object of type SimpleEntry", (Collection<Object>)changeTracker.getChanged(), hasItems(isA(SimpleEntry.class) ));

            assertThat( "SimpleEntry should be the initial value of the map", (Collection<Object>)changeTracker.getChanged(), hasItems( new SimpleEntry("key", "initialValue")) );
        });
    }
}
