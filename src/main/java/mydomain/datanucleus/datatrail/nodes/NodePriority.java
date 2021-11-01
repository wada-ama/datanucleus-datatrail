package mydomain.datanucleus.datatrail.nodes;

public @interface NodePriority {
    /**
     * Useful constant for the highest precedence value.
     * @see java.lang.Integer#MIN_VALUE
     */
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    /**
     * Useful constant for the lowest precedence value.
     * @see java.lang.Integer#MAX_VALUE
     */
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;


    /**
     * The priority/precedence level in which this node will be selected in case multiple nodes implementations are detected
     * @return
     */
    int priority() default LOWEST_PRECEDENCE;
}
