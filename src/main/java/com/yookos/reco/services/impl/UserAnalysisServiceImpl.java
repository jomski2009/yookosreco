package com.yookos.reco.services.impl;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.yookos.reco.domain.RecoQuery;
import com.yookos.reco.domain.YookosUser;
import com.yookos.reco.services.UserAnalysisService;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

/**
 * Created by jome on 2014/02/08.
 */
@Service
public class UserAnalysisServiceImpl implements UserAnalysisService {

    @Autowired
    Datastore ds;

    @Override
    public String getQueryResult(RecoQuery recoQuery) {
        QueryBuilder queryBuilder = QueryBuilder.start();
        DBCollection users = ds.getCollection(YookosUser.class);

        if (recoQuery.getAge() > 0) {
            Calendar calendar = GregorianCalendar.getInstance();

            calendar.add(Calendar.YEAR, -recoQuery.getAge());
            Date queryDate = calendar.getTime();
            System.out.println(queryDate);
            queryBuilder.put("profile.birthdate").lessThan(queryDate);
        }


        if (recoQuery.getGender() != null && !recoQuery.getGender().isEmpty()) {
            queryBuilder.put("profile.gender").is(recoQuery.getGender()); //Ensure the case matches document case.
        }

        if (recoQuery.getCountry() != null && !recoQuery.getCountry().isEmpty()) {
            queryBuilder.put("profile.country").is(recoQuery.getCountry());

        }

        if (recoQuery.getHobby() != null && !recoQuery.getHobby().isEmpty()) {
            Pattern pattern = Pattern.compile(recoQuery.getHobby(), Pattern.CASE_INSENSITIVE);

            queryBuilder.put("profile.hobbies").regex(pattern);
        }

        DBObject queryObject = queryBuilder.get();


//

//        String gender = "Female";
//        String country = "South Africa";
//        String hobby = "cooking";
//
//        //Query for all men living in Nigeria who have football as hobby.
//        DBObject dbquery = new BasicDBObject().append("profile.gender", gender).append("profile.country", country).append("profile.hobbies", new BasicDBObject("$regex", hobby).append("$options", "i"));
//        System.out.println(query);
//
//        QueryBuilder queryBuilder = QueryBuilder.start();
//        Pattern pattern = Pattern.compile(hobby,Pattern.CASE_INSENSITIVE);
//
//        DBObject queryObject = queryBuilder.put("profile.gender").is(gender).put("profile.country").is(country).put("profile.hobbies").regex(pattern).get();

        System.out.println(queryObject);


        long count = users.getCount(queryObject);
        String genderTitle = "users";
        if (recoQuery.getGender().equals("Male")) {
            genderTitle = "men";
        }
        if (recoQuery.getGender().equals("Female")){
            genderTitle = "women";
        }

        String queryResult = "";
        if (recoQuery.getGender().equals("users")) {
            queryResult = "Number of users living in " + recoQuery.getCountry() + " older than " + recoQuery.getAge() + " who like " + recoQuery.getHobby() + " is: " + count;
        } else {
            queryResult = "Number of " + genderTitle + " living in " + recoQuery.getCountry() + " older than " + recoQuery.getAge() + " who like " + recoQuery.getHobby() + " is: " + count;
        }

        System.out.println(queryResult);

        return queryResult;
    }
}
