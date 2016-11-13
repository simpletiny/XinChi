package com.xinchi.solr.service;

import org.apache.solr.client.solrj.SolrClient;

public interface SimpletinySolr {
	public SolrClient getSolr(String url);
}
