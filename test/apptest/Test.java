package apptest;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.util.NamedList;

public class Test extends SolrClient {
	public static void main(String[] a) {
		String x = "20161203";
		String b = "11:03:26";
		String time = "";
		time += x.substring(0, 4);
		time += "-";
		time += x.substring(4, 6);
		time += "-";
		time += x.substring(6, 8);
		time += " " + b;
		System.out.println(time.length());
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public NamedList<Object> request(SolrRequest arg0, String arg1) throws SolrServerException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
