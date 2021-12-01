package org.datanucleus.test.model;

import org.datanucleus.datatrail.DataTrail;
import org.datanucleus.datatrail.DataTrailDescription;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

@PersistenceCapable
@Discriminator(strategy=DiscriminatorStrategy.CLASS_NAME, column="TYP", indexed="true")
@Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSN")
@DataTrail(excludeFromDataTrail = true)
public class BaseTeacher implements DataTrailDescription
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
    public String getDataTrailDescription() {
        return name;
    }
}
