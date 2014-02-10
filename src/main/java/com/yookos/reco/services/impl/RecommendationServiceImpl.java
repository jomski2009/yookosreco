package com.yookos.reco.services.impl;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.yookos.reco.domain.RecoUser;
import com.yookos.reco.domain.ResultWrapper;
import com.yookos.reco.domain.YookosUser;
import com.yookos.reco.services.RecommendationService;
import org.mongodb.morphia.Datastore;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Transaction;
import org.neo4j.rest.graphdb.RestAPIFacade;
import org.neo4j.rest.graphdb.entity.RestNode;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.neo4j.rest.graphdb.util.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by jome on 2014/02/08.
 */

@Service
public class RecommendationServiceImpl implements RecommendationService {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    RestCypherQueryEngine engine;

    @Autowired
    RestAPIFacade restAPIFacade;

    @Autowired
    Datastore ds;

    @Override
    public List<String> getFriendsOfFriendsForUserid(long userid, int limit, int offset) {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", userid);
        params.put("limit", limit);
        params.put("skip", offset);

        List<String> matches = new ArrayList<>();

        QueryResult<Map<String, Object>> result = engine.query("match (p:Person{userid:{userid}})-[:friends_with]->(p1:Person)-[:friends_with]->(p2:Person) where not (p)<-[:friends_with]->(p2) return p, count(p1), p2 order by count(p1) desc skip {skip} limit {limit}", params);


        Iterator<Map<String, Object>> iterator = result.iterator();

        while (iterator.hasNext()) {
            Map<String, Object> item = iterator.next();
            RestNode node = (RestNode) item.get("p");
            if (node.hasLabel(DynamicLabel.label("Person"))) {
                System.out.println("The RestNode is of type: Person");
            }
            int count = (int) item.get("count(p1)");
            RestNode node2 = (RestNode) item.get("p2");


            String name1 = node.getProperty("firstname").toString() + " " + node.getProperty("lastname").toString();
            String name2 = node2.getProperty("firstname").toString() + " " + node2.getProperty("lastname").toString();
            matches.add(name1 + ": Mutual friends - " + count + " : " + name2);
            System.out.println(name1 + ": Mutual friends - " + count + " : " + name2);
        }

        return matches;
    }

    @Override
    public List<ResultWrapper> getFriendsOfFriendsForUsername(String username, int limit, int offset) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("limit", limit);
        params.put("skip", offset);
        String queryStatement = "match (p:Person{username:{username}})<-[:friends_with]->(p1:Person)<-[:friends_with]->(p2:Person) "
                //+ "where not (p)<-[:friends_with]->(p2) "
                + "return p.userid, p.firstname, p.lastname, count(p1), p2.userid, p2.firstname, p2.lastname "
                + "order by count(p1) desc skip {skip} limit {limit}";
        List<ResultWrapper> matches = new ArrayList<>();
        QueryResult<Map<String, Object>> result;

        try (Transaction tx = restAPIFacade.beginTx()) {
            //result = restAPIFacade.query(queryStatement, params, null);
            result = engine.query(queryStatement, params);
            tx.success();
        }

        Iterator<Map<String, Object>> iterator = result.iterator();

        while (iterator.hasNext()) {
            Map<String, Object> item = iterator.next();
            RecoUser user = new RecoUser();
            RecoUser fof = new RecoUser();

            user.setUserid((int) item.get("p.userid"));
            user.setName(item.get("p.firstname") + " " + item.get("p.lastname"));
            int mutualFriendCount = (int) item.get("count(p1)");
            fof.setUserid((int) item.get("p2.userid"));
            fof.setName(item.get("p2.firstname") + " " + item.get("p2.lastname"));
            ResultWrapper wrapper = new ResultWrapper();

            if (friends(user, fof)) {
                System.out.println("Already friends with " + fof.getName() + " - " + fof.getUserid());
                continue;

            }

            wrapper.setUser(user);
            wrapper.setFof(fof);
            wrapper.setCount(mutualFriendCount);
            matches.add(wrapper);
            System.out.println("Recommended : " + fof.getName() + " - " + fof.getUserid());

        }

        return matches;
    }


    public List<ResultWrapper> getFriendsOfFriendsForUsernameWithHobby(String username, int limit, int offset, String hobby) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("limit", limit);
        params.put("skip", offset);
        String queryStatement = "match (p:Person{username:{username}})<-[:friends_with]->(p1:Person)<-[:friends_with]->(p2:Person) "
                //+ "where not (p)<-[:friends_with]->(p2) "
                + "return p.userid, p.firstname, p.lastname, count(p1), p2.userid, p2.firstname, p2.lastname "
                + "order by count(p1) desc skip {skip} limit {limit}";
        List<ResultWrapper> matches = new ArrayList<>();
        QueryResult<Map<String, Object>> result;

        try (Transaction tx = restAPIFacade.beginTx()) {
            //result = restAPIFacade.query(queryStatement, params, null);
            result = engine.query(queryStatement, params);
            tx.success();
        }

        Iterator<Map<String, Object>> iterator = result.iterator();

        while (iterator.hasNext()) {
            Map<String, Object> item = iterator.next();
            RecoUser user = new RecoUser();
            RecoUser fof = new RecoUser();

            user.setUserid((int) item.get("p.userid"));
            user.setName(item.get("p.firstname") + " " + item.get("p.lastname"));
            int mutualFriendCount = (int) item.get("count(p1)");
            fof.setUserid((int) item.get("p2.userid"));
            fof.setName(item.get("p2.firstname") + " " + item.get("p2.lastname"));
            ResultWrapper wrapper = new ResultWrapper();

            if (friends(user, fof)) {
                System.out.println("Already friends with " + fof.getName() + " - " + fof.getUserid());
                continue;

            }

            if(!fofLikes(fof, hobby)){
                continue;
            }

            wrapper.setUser(user);
            wrapper.setFof(fof);
            wrapper.setCount(mutualFriendCount);
            matches.add(wrapper);
            System.out.println("Recommended : " + fof.getName() + " - " + fof.getUserid());

        }

        return matches;
    }

    private boolean fofLikes(RecoUser fof, String hobby) {
        boolean result = false;

        QueryBuilder queryBuilder = QueryBuilder.start();
        DBCollection users = ds.getCollection(YookosUser.class);
        queryBuilder.put("_id").is(fof.getUserid());

        Pattern pattern = Pattern.compile(hobby,Pattern.CASE_INSENSITIVE);

        queryBuilder.put("profile.hobbies").regex(pattern);

        DBObject queryObject = queryBuilder.get();

        System.out.println(queryObject);

        long count = users.getCount(queryObject);

        if(count > 0){
            result = true;
        }                 else{
            result = false;
        }

        System.out.println("Count is: " + count);

        return result;
    }

    private boolean friends(RecoUser user, RecoUser fof) {
        boolean result = false;
        String query = "match (user:Person{userid:{userid}})<-[r:friends_with]->(fof:Person{userid:{fofid}}) return r";
        Map<String, Object> params = new HashMap<>();
        params.put("userid", user.getUserid());
        params.put("fofid", fof.getUserid());
        try (Transaction tx = restAPIFacade.beginTx()) {
            QueryResult<Map<String, Object>> queryResult = engine.query(query, params);

            Iterator<Map<String, Object>> iterator = queryResult.iterator();


            if (iterator.hasNext()) {
                System.out.println("result returned");
                result = true;

            } else {
                System.out.println("result returned");
                result = false;
            }
            tx.success();
        }

        return result;
    }

    @Override
    public List<RecoUser> getMutualFriends(long userid, long fofid) {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", userid);
        params.put("fofid", fofid);

        String query = "match (p:Person{userid:{userid}})<-[r:friends_with]->(p2)<-[r2:friends_with]->(fof:Person{userid:{fofid}}) return p2.userid, p2.firstname, p2.lastname";
        List<RecoUser> friendslist = new ArrayList<>();

        try (Transaction tx = restAPIFacade.beginTx()) {
            QueryResult<Map<String, Object>> queryResult = restAPIFacade.query(query, params, null);
            Iterator<Map<String, Object>> iterator = queryResult.iterator();

            while (iterator.hasNext()) {
                Map<String, Object> item = iterator.next();
                RecoUser friend = new RecoUser();
                friend.setName(item.get("p2.firstname") + " " + item.get("p2.lastname"));
                friend.setUserid((int) item.get("p2.userid"));

                friendslist.add(friend);
            }

            tx.success();
        }

        return friendslist;
    }
}
