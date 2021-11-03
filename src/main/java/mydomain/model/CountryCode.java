package mydomain.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;
import java.util.Objects;

@PersistenceCapable(detachable = "true")
@Version(strategy= VersionStrategy.VERSION_NUMBER, column="VERSN")
public class CountryCode implements ITrailDesc{
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
    public String minimalTxtDesc() {
        return country + " => +" + code;
    }
}
