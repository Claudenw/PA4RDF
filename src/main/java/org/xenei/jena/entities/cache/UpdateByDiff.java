package org.xenei.jena.entities.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.compose.MultiUnion;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.util.iterator.WrappedIterator;
import org.xenei.jena.entities.EntityManager;
import org.xenei.jena.entities.impl.TransactionHolder;
import org.xenei.rdf_diff_patch.PatchFactory;
import org.xenei.rdf_diff_patch.UpdateFactory;

import difflib.Patch;

public class UpdateByDiff 
{

	private Map<Node,Graph> snapshots;
	private Map<Node,SubjectTable> tables;
	/**
	 * @param entityManagerImpl
	 */
	public UpdateByDiff()
	{
		this.snapshots = new HashMap<Node,Graph>();
		this.tables = new HashMap<Node,SubjectTable>();
	}

	public synchronized void clear() {
		snapshots.clear();
		tables.clear();
	}

	public synchronized void register( SubjectTable tbl )
	{
		if (!snapshots.containsKey(tbl.getSubject()))
		{			
			snapshots.put( tbl.getSubject(), tbl.snapshot());
			tables.put( tbl.getSubject(), tbl);
		}
	}
	
	public synchronized void rollback()
	{
		for (Node n : snapshots.keySet())
		{
			tables.get(n).reset( snapshots.get(n));
		}
		clear();
	}

	private  UpdateRequest generateUpdate( String graphName ) {

		final Iterator<Graph> iter = WrappedIterator.create( tables.values().iterator()).mapWith( st -> st.asGraph());

		final Dataset origDS = DatasetFactory.create();
		origDS.addNamedModel( graphName, ModelFactory.createModelForGraph(new MultiUnion( snapshots.values().iterator())));

		final Dataset chgdDS = DatasetFactory.create();
		chgdDS.addNamedModel( graphName,  ModelFactory.createModelForGraph(new MultiUnion( iter )));


		final Patch<Quad> patch = PatchFactory.patch( origDS,  chgdDS);
		return UpdateFactory.asUpdate(patch);
	}
	
	public List<Node> getTableNames() {
		return new ArrayList<Node>( snapshots.keySet() );
	}

	/**
	 * Synchronize the data.  
	 * @param entityManager
	 * @param graphNode
	 * @return true if any changes were made.
	 */
	public synchronized List<Node> sync( EntityManager entityManager, Node graphNode ) {
		final UpdateRequest req = generateUpdate( graphNode.getURI() );
		if (!req.iterator().hasNext())
		{
			return Collections.emptyList();
		}
		final TransactionHolder th = new TransactionHolder( entityManager.getConnection(), ReadWrite.WRITE);
		try {
			List<Node> retval = new ArrayList<Node>(snapshots.keySet());
			entityManager.getConnection().update(req);
			th.commit();
			snapshots.clear();
			tables.clear();
			return retval;
		}
		catch (final RuntimeException e) {
			th.abort();
			throw e;
		}
	}
}