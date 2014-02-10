package com.yookos.reco;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.yookos.reco.domain.YookosUser;
import com.yookos.reco.jpa.repository.UserProfileRepository;
import com.yookos.reco.services.UserService;
import org.mongodb.morphia.Datastore;
import org.neo4j.rest.graphdb.RestAPIFacade;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Created by jome on 2014/02/06.
 */

@Component
public class PlayClass implements CommandLineRunner {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    Datastore ds;

    @Autowired
    UserProfileRepository userProfileRespository;

    @Autowired
    UserService userService;

    @Autowired
    RestAPIFacade facade;

    @Autowired
    RestCypherQueryEngine engine;

    @Override
    public void run(String... args) throws Exception {
        log.info("This is my world....");

//        Map<String, Object> params = new HashMap<>();
//        params.put("userid", 8432);
//        params.put("limit", 25);
//
//        QueryResult<Map<String, Object>> result = engine.query("match (p:Person{userid:{userid}})-[:friends_with]->(p1:Person)-[:friends_with]->(p2:Person) where not (p)<-[:friends_with]->(p2) return p, count(p1), p2 order by count(p1) desc limit {limit}", params);
//
//
//        Iterator<Map<String, Object>> iterator = result.iterator();
//
//        while (iterator.hasNext()) {
//            Map<String, Object> item = iterator.next();
//            RestNode node = (RestNode) item.get("p");
//            if (node.hasLabel(DynamicLabel.label("Person"))) {
//                System.out.println("The RestNode is of type: Person");
//            }
//            int count = (int) item.get("count(p1)");
//            RestNode node2 = (RestNode) item.get("p2");
//
//
//            String name1 = node.getProperty("firstname").toString() + " " + node.getProperty("lastname").toString();
//            String name2 = node2.getProperty("firstname").toString() + " " + node2.getProperty("lastname").toString();
//
//            System.out.println(name1 + ": Mutual friends - " + count + " : " + name2);
//        }

        targetedProfiles();
    }

    private void targetedProfiles() {
        DBCollection users = ds.getCollection(YookosUser.class);
        String gender = "Female";
        String country = "South Africa";
        String hobby = "cooking";

        //Query for all men living in Nigeria who have football as hobby.
        DBObject query = new BasicDBObject().append("profile.gender", gender).append("profile.country", country).append("profile.hobbies", new BasicDBObject("$regex", hobby).append("$options", "i"));
        System.out.println(query);

        QueryBuilder queryBuilder = QueryBuilder.start();
        Pattern pattern = Pattern.compile(hobby,Pattern.CASE_INSENSITIVE);

        DBObject queryObject = queryBuilder.put("profile.gender").is(gender).put("profile.country").is(country).put("profile.hobbies").regex(pattern).get();

        System.out.println(queryObject);


        long count = users.getCount(queryObject);
        String genderTitle = "";
        if (gender.equals("Male")) {
            genderTitle = "men";
        } else {
            genderTitle = "women";
        }
        System.out.println("Number of " + genderTitle + " living in " + country + " who like " + hobby + " is: " + count);


    }
}
