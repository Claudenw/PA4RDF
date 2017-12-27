package org.xenei.jena.entities.cache;

import java.util.Iterator;

import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.update.Update;
import org.apache.jena.update.UpdateRequest;
import org.xenei.jena.entities.impl.EntityManagerImpl;

public class UpdateCached implements UpdateHandler
{

	/**
	 * 
	 */
	private final RDFConnection connection;

	/**
	 * @param entityManagerImpl
	 */
	public UpdateCached(RDFConnection connection)
	{
		this.connection = connection;
	}

	private UpdateRequest updates = null;

	@Override
	public synchronized void prepare(UpdateRequest updateReq)
	{
		Iterator<Update> iter = updateReq.iterator();
		if (iter.hasNext())
		{
			if (updates == null)
			{
				updates = updateReq;
			} else
			{
				while (iter.hasNext())
				{
					updates.add(iter.next());
				}
			}
		}
	}

	@Override
	public synchronized void prepare(Update update)
	{
		if (updates == null)
		{
			updates = new UpdateRequest(update);
		} else
		{
			updates.add(update);
		}
	}

	public synchronized void execute()
	{
		if (updates.iterator().hasNext())
		{
			synchronized (this.connection)
			{
				this.connection.begin(ReadWrite.WRITE);
				try
				{
					this.connection.update(updates);
					this.connection.commit();
				} catch (final RuntimeException e)
				{
					this.connection.abort();
					throw e;
				}
			}
		}
		reset();
	}

	public synchronized void reset()
	{
		updates = null;
	}

}