package org.xenei.jena.entities.cache;

import java.util.Set;

import org.apache.jena.graph.FrontsNode;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;

public interface SubjectTable { 
	/**
	 * Get the resource this table is for.
	 * @return the Resource
	 */
	public Node getSubject();
	
	/**
	 * Get the list of values for a predicate.
	 * 
	 * getValues( null ) should return a list of unique values.
	 * 
	 * @param predicate the predicate to look for.
	 * @return The list of RDFNodes for the property in this table.
	 */
	public Set<Node> getValues( FrontsNode predicate );
	
	/**
	 * Add the value to the list
	 * @param predicate the predicate for the value
	 * @param value the value itself.
	 */
	public void addValue( FrontsNode predicate, FrontsNode value );

	/**
	 * Add the value to the list
	 * @param predicate the predicate for the value
	 * @param value the value itself.
	 */
	public void addValue( Node predicate, Node value );

	/**
	 * Remove the value from the list.
	 * @param predicate the predicate to remove the value from.
	 * @param value the value to remove.
	 */
	public void removeValue( FrontsNode predicate, FrontsNode value );
	
	/**
	 * Remove the value from the list.
	 * @param predicate the predicate to remove the value from.
	 * @param value the value to remove.
	 */
	public void removeValue( Node predicate, Node value );

	/**
	 * Return the list of all predicates with the specified value.
	 * 
	 * getPredicates( null ) should return a list of unique predicates.
	 * @param value the value to locate.
	 * @return the list of properties with the value.
	 */
	public Set<Node> getPedicates(FrontsNode value);

	/**
	 * Return the list of all predicates with the specified value.
	 * 
	 * getPredicates( null ) should return a list of unique predicates.
	 * @param value the value to locate.
	 * @return the list of properties with the value.
	 */
	public Set<Node> getPedicates(Node value);

	/**
	 * Return the table as a graph.
	 * @return a graph.
	 */
	public Graph asGraph();
	
	/**
	 * Return true if the predicate and property exist in the table.
	 * Either value may be null for wild card.
	 * @param predicate the predicate to locate
	 * @param value the value to locate
	 * @return true if the predicate and value are in the table.
	 */
	public boolean has( FrontsNode predicate, FrontsNode value );
	
	/**
	 * Return true if the predicate and property exist in the table.
	 * Either value may be null for wild card.
	 * @param predicate the predicate to locate
	 * @param value the value to locate
	 * @return true if the predicate and value are in the table.
	 */
	public boolean has( Node predicate, Node value );
	
	/**
	 * Return the size of this table.
	 * @return the number of triples in the table.
	 */
	public int size();
	
	/**
	 * @return true if the table has no data, false otherwise.
	 */
	public boolean isEmpty();
}
