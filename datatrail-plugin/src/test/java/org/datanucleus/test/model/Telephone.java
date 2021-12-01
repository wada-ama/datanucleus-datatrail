package org.datanucleus.test.model;

import org.datanucleus.datatrail.DataTrailDescription;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;
import java.util.Date;

@PersistenceCapable(detachable="true")
@Version(strategy= VersionStrategy.VERSION_NUMBER, column="VERSN")
public class Telephone implements DataTrailDescription
{
    String number;
    CountryCode countryCode;
    Date created;

    public Telephone(String number, CountryCode countryCode)
    {
        this.number = number;
        this.countryCode = countryCode;
    }

    public String getNumber()
    {
        return number;
    }
    public void setNumber(String number)
    {
        this.number = number;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String getDataTrailDescription() {
        return number;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }
}
