package com.xinchi.common;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

public class SolrUtil {
	public static SolrInputDocument toSolrInputDocument(SolrDocument d) {
		SolrInputDocument doc = new SolrInputDocument();
		for (String name : d.getFieldNames()) {
			doc.addField(name, d.getFieldValue(name), 1.0f);
		}
		return doc;
	}

	public static SolrDocument toSolrDocument(SolrInputDocument d) {
		SolrDocument doc = new SolrDocument();
		for (String name : d.getFieldNames()) {
			doc.addField(name, d.getFieldValue(name));
		}
		return doc;
	}
}
