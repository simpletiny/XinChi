package com.xinchi.solr.service.impl;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.stereotype.Service;

import com.xinchi.solr.service.SimpletinySolr;

@Service(BeanDefinition.SCOPE_SINGLETON)
public class SimpletinySolrImpl implements SimpletinySolr {

	@Override
	public SolrClient getSolr(String url) {
		SolrClient solr = new HttpSolrClient.Builder(url).build();
		return solr;
	}

}
