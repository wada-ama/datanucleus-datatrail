package mydomain.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;
import java.util.Objects;

@PersistenceCapable(detachable = "true")
@Version(strategy = VersionStrategy.VERSION_NUMBER, column = "VERSN")
public class Street implements ITrailDesc {
    String name;

    public Street(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String minimalTxtDesc() {
        return name;
    }
}
