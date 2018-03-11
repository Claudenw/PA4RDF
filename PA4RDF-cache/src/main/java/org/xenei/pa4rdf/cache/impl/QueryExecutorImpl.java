package org.xenei.pa4rdf.cache.impl;

import org.apache.jena.graph.Node;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.sparql.syntax.ElementNamedGraph;
import org.xenei.pa4rdf.cache.QueryExecutor;

public class QueryExecutorImpl implements QueryExecutor
{
	private RDFConnection rdfConnection;
	private Node modelName;
	
	public QueryExecutorImpl( RDFConnection rdfConnection, Node modelName)
	{
		this.rdfConnection = rdfConnection;
		this.modelName = modelName;
	}
	
	public Node getModelName()
	{
		return modelName;
	}
	
	@Override
    public QueryExecution execute(Query query) {
        final Query q = query.cloneQuery();
        if (!modelName.equals( Quad.defaultGraphIRI )) {
            final ElementNamedGraph eng = new ElementNamedGraph( modelName, q.getQueryPattern() );
            q.setQueryPattern( eng );
        }
        return rdfConnection.query( q );
    }


	@Override
	public RDFConnection getConnection()
	{		
		return rdfConnection;
	}

}
