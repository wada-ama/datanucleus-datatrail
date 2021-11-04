package mydomain.datanucleus.types.wrappers.tracker;

import org.datanucleus.test.model.CountryCode;
import org.datanucleus.test.model.Street;

import javax.jdo.annotations.Join;
import javax.jdo.annotations.Key;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Value;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;
import java.util.HashMap;
import java.util.Map;

@PersistenceCapable
@Version(strategy= VersionStrategy.VERSION_NUMBER, column="VERSN")
public class PrimitiveMapClass {
    @Join
    Map<String, String> primitiveMap = new HashMap<>();

    public Map<String, String> getPrimitiveMap() {
        return primitiveMap;
    }
}
