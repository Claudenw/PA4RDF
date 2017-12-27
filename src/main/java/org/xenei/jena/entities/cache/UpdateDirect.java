package org.xenei.jena.entities.cache;

import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.update.Update;
import org.apache.jena.update.UpdateRequest;
import org.xenei.jena.entities.impl.EntityManagerImpl;
import org.xenei.jena.entities.impl.TransactionHolder;

public class UpdateDirect implements UpdateHandler
{

	/**
	 * 
	 */
	private final RDFConnection connection;

	/**
	 * @param entityManagerImpl
	 */
	public UpdateDirect(RDFConnection connection)
	{
		this.connection = connection;
	}

	@Override
	public synchronized void prepare(UpdateRequest updateReq)
	{
		if (updateReq.iterator().hasNext())
		{
			synchronized (this.connection)
			{
				this.connection.begin(ReadWrite.WRITE);
				try
				{
					this.connection.update(updateReq);
					this.connection.commit();
				} catch (final RuntimeException e)
				{
					this.connection.abort();
					throw e;
				}
			}
		}
	}

	@Override
	public synchronized void prepare(Update update)
	{
		synchronized (this.connection)
		{
		    TransactionHolder th = new TransactionHolder( this.connection, ReadWrite.WRITE);
			try
			{
				this.connection.update(update);
				th.commit();
			} catch (final RuntimeException e)
			{
				th.abort();
				throw e;
			} 
		}
	}

	public void execute()
	{
	}

	public void reset()
	{
	};
}