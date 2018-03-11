package org.xenei.pa4rdf.cache;

import org.apache.jena.graph.Node;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdfconnection.RDFConnection;

public interface QueryExecutor
{
	
	/**
     * Get the model name this entity manager is working against.
     * 
     * @return
     */
    public Node getModelName();

	
	/**
     * Execute a query. The query will be modified to apply to the specific
     * queryExecutor.
     * 
     * @param query
     *            The query to execute.
     * @return The query execution for the query
     */
    public QueryExecution execute(Query query);

    /**
     * @return The RDFConnection that this executor is using.
     */
    public RDFConnection getConnection();
}
