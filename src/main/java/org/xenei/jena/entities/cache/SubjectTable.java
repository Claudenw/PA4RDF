package org.xenei.jena.entities.cache;

import java.util.List;

import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public interface SubjectTable {
	/**
	 * Get the resource this table is for.
	 * @return the Resource
	 */
	public Resource getSubject();
	
	/**
	 * Get the list of values for a predicate.
	 * 
	 * getValues( null ) should return a list of unique values.
	 * 
	 * @param predicate the predicate to look for.
	 * @return The list of RDFNodes for the property in this table.
	 */
	public List<RDFNode> getValues( Property predicate );
	
	/**
	 * Add the value to the list
	 * @param predicate the predicate for the value
	 * @param value the value itself.
	 */
	public void addValue( Property predicate, RDFNode value );

	/**
	 * Remove the value from the list.
	 * @param predicate the predicate to remove the value from.
	 * @param value the value to remove.
	 */
	public void removeValue( Property predicate, RDFNode value );
	
	/**
	 * Return the list of all predicates with the specified value.
	 * 
	 * getPredicates( null ) should return a list of unique predicates.
	 * @param value the value to locate.
	 * @return the list of properties with the value.
	 */
	public List<Property> getPedicates(RDFNode value);

	/**
	 * Return the table as a list of triples.
	 * @return
	 */
	public List<Triple> asTriples();
	
	/**
	 * Return true if the predicate and property exist in the table.
	 * Either value may be null for wild card.
	 * @param predicate the predicate to locate
	 * @param value the value to locate
	 * @return true if the predicate and value are in the table.
	 */
	public boolean has( Property predicate, RDFNode value );
}
