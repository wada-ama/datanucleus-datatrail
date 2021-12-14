package org.datanucleus.test.model;

import org.datanucleus.datatrail.spi.DataTrailDescription;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;
import java.util.Objects;

@PersistenceCapable(detachable = "true")
@Version(strategy= VersionStrategy.VERSION_NUMBER, column="VERSN")
public class CountryCode implements DataTrailDescription {
    String country;
    int code;

    public CountryCode(String country, int code) {
        this.country = country;
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getDataTrailDescription() {
        return country + " => +" + code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryCode that = (CountryCode) o;
        return getCode() == that.getCode() && Objects.equals(getCountry(), that.getCountry()) && Objects.equals(JDOHelper.getObjectId(this), JDOHelper.getObjectId(that));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCountry(), getCode(), JDOHelper.getObjectId(this));
    }
}
