package com.yookos.reco.config;

import org.neo4j.rest.graphdb.RestAPIFacade;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.batch.BatchRestAPI;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;

/**
 * Created by jome on 2014/02/06.
 */

@Configuration
@EnableNeo4jRepositories(basePackages = "com.yookos.reco.neo4j")
public class Neo4jConfig {
    @Autowired
    Environment env;

    @Bean
    @Autowired
    RestGraphDatabase restGraphDatabase() {
        return new RestGraphDatabase("http://neo4jbox:7474/db/data/");
    }

    @Bean
    @Autowired
    RestAPIFacade restAPIFacade() {
        RestAPIFacade facade = new RestAPIFacade("http://neo4jbox:7474/db/data/");
        return facade;
    }

    @Bean
    RestCypherQueryEngine restCypherQueryEngine() {
        RestCypherQueryEngine engine = new RestCypherQueryEngine(restAPIFacade());
        return engine;
    }

    @Bean
    public BatchRestAPI batchRestAPI() {
        return new BatchRestAPI("http://neo4jbox:7474/db/data/", restAPIFacade());
    }

}
