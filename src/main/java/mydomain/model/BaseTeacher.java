package mydomain.model;

import mydomain.audit.DataTrail;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.Key;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Value;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;
import java.util.HashMap;
import java.util.Map;

@PersistenceCapable
@Discriminator(strategy=DiscriminatorStrategy.CLASS_NAME, column="TYP", indexed="true")
@Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSN")
@DataTrail(excludeFromDataTrail = true)
public class BaseTeacher implements ITrailDesc
{
    String name;

    public BaseTeacher(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String minimalTxtDesc() {
        return name;
    }
}
