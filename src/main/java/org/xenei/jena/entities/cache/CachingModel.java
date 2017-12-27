package org.xenei.jena.entities.cache;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Alt;
import org.apache.jena.rdf.model.AnonId;
import org.apache.jena.rdf.model.Bag;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelChangedListener;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.NsIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.RDFReader;
import org.apache.jena.rdf.model.RDFWriter;
import org.apache.jena.rdf.model.RSIterator;
import org.apache.jena.rdf.model.ReifiedStatement;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceF;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.Seq;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.shared.Command;
import org.apache.jena.shared.Lock;
import org.apache.jena.shared.PrefixMapping;
import org.xenei.jena.entities.cache.ModelInterceptor.Intercepted;
import org.xenei.jena.entities.impl.EntityManagerImpl;

import net.sf.cglib.proxy.Enhancer;

public interface CachingModel extends Model
{
	public static CachingModel makeInstance(EntityManagerImpl entityManager) {
		ModelInterceptor interceptor = new ModelInterceptor(entityManager);
		final Enhancer e = new Enhancer();
		e.setInterfaces(new Class[] { CachingModel.class, Intercepted.class });
		e.setCallback(interceptor);
		return (CachingModel) e.create();
	}
	
	/**
	 * Synchronize the underlying CachingGraph.
	 */
	public void sync();
	
	public void clear();
	
	/**
	 * Adopt the resource into this graph.
	 * @param r the resource to adopt
	 * @return the adopted resource.
	 */
	public Resource adopt( Resource r );
		
}
