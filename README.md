# Loadbalancing_Solr
Different ways of load balancing Apache Solr via LBHttpSolrClient or DSE

Using DataStax Enterprise 5.1 for this example: 1 Data Center (DC1) with 3 nodes with DSE Search enabled

**SETUP**

Create simple table and few rows of data:

``` 
create keyspace demo with replication = {'class':'NetworkTopologyStrategy', 'DC1':3};
```

```
CREATE TABLE IF NOT EXISTS demo.solr (id VARCHAR PRIMARY KEY, body TEXT, title TEXT, date VARCHAR);
```

```
insert into demo.solr (id, body, title, date) values ('1', 'Douglas MacArthur escape from the Philippines', 'featured article', '2018-01-01');
insert into demo.solr (id, body, title, date) values ('2', 'Doglas MacArthur escape from the Philippines', 'featured article', '2018-01-01');
insert into demo.solr (id, body, title, date) values ('3', 'Dooglas MacArthur escape from the Philippines', 'featured article', '2018-01-01');
insert into demo.solr (id, body, title, date) values ('4', 'Duglas MacArthur escape from the Philippines', 'featured article', '2018-01-01');
```

```
create search index on demo.solr;
```

Test Setup: 
```
select * from demo.solr where solr_query = '{"q":"*:*","fq":"body:Do*"}';
```

**Testing**

Try running search.java with all 3 nodes up. Then take one of the nodes down.  Make sure it's one of the 2 contact points in your code. 

Don't forget to update the IP address in search.java. 


