package mydomain.model;

import mydomain.audit.DataTrail;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;


@DataTrail
@Inheritance(strategy= InheritanceStrategy.SUPERCLASS_TABLE)
public class Teacher extends BaseTeacher{
    public Teacher(String name) {
        super(name);
    }
}
