package apptest;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.util.NamedList;

public class Test extends SolrClient{

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NamedList<Object> request(SolrRequest arg0, String arg1)
			throws SolrServerException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
