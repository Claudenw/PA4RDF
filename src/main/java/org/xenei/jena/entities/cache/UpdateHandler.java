package org.xenei.jena.entities.cache;

import org.apache.jena.update.Update;
import org.apache.jena.update.UpdateRequest;

public interface UpdateHandler
{
	void reset();

	void execute();

	void prepare(Update update);

	void prepare(UpdateRequest updateReq);
}