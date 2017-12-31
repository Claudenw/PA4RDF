package org.xenei.jena.entities.impl.handlers;

public interface HandlerTestInterface {
    /**
     * Create an rdf node with a normal value
     */
    public void testCreateRDFNode();

    /**
     * Create an rdf node with a null argument
     */
    public void testCreateRDFNode_Null();

    /**
     * test values are empty
     */
    public void testIsEmpty();

    /**
     * Test that parse object works.
     */
    public void testParseObject();

}
