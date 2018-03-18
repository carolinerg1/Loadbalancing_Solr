package com.test.cassandra;

import com.datastax.driver.core.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.LBHttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

public class search {
	

	public void cqlSolr() {
		 Cluster cluster;
		 Session session;
		
		
		 cluster = Cluster.builder()
	        .addContactPoints("127.0.0.1", "127.0.0.2") //UPDATE!!
	        .build();
		 session = cluster.connect("demo");
		 
		 final String q = "select * from demo.solr where solr_query = '{\"q\":\"*:*\",\"fq\":\"body:Do*\"}';";
			
		 ResultSet results = session.execute(q);
		 for (Row row : results) {
	  		System.out.format("%s %s\n",row.getString("id"), row.getString("body"));
		 }
			

		cluster.close();
	}
	
	public void loadHttpSolr() {
		try {
			 String solr1 = "http://127.0.0.1:8983/solr/demo.solr/"; //UPDATE!!
			 String solr2 = "http://127.0.0.2:8983/solr/demo.solr/"; //UPDATE!!
		        
			 String[] urlArray = {solr1, solr2 };
       	
			 SolrClient client = new LBHttpSolrClient.Builder().withBaseSolrUrls(urlArray).build();
			
			 // HTTP Solr Client without load balancing:
			 //SolrClient client = new HttpSolrClient.Builder("http://127.0.0.1:8983/solr/demo.solr/").build();

       
			 SolrQuery query = new SolrQuery();
			 query.setQuery("*:*");
			 query.addFilterQuery("body:Do*");
			 query.setFields("id","body");
			 query.setStart(0);
			 //query.set("defType", "edismax");
			 query.set("df", "body");
			 query.set("wt", "json");
			 query.setShowDebugInfo(true);
			 query.set("indent", true);
			 query.setStart(0);
	        
			 QueryResponse response = client.query(query);
			 				
			 SolrDocumentList results = response.getResults();
			
			 for (int i = 0; i < results.size(); ++i) {
	            System.out.println("result: " + results.get(i));
			 }
			
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		search s = new search();
		
		System.out.println("Solr query using CQL: ");
		 s.cqlSolr();
		
		 System.out.println("Solr query using HTTP Load Balancer: ");
		 s.loadHttpSolr();
			 
			 
	
		
	}

}
