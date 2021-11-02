package mydomain.datanucleus.datatrail.nodes;

import mydomain.datanucleus.datatrail.NodeAction;
import mydomain.datanucleus.datatrail.NodeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NodeDefinition {

    /**
     * Type of node implemented by the class
     */
    NodeType type();

    /**
     * Type of action implemented by the class
     * @return
     */
    NodeAction[] action() default {};
}
