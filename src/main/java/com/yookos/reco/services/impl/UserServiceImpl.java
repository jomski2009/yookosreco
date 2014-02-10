package com.yookos.reco.services.impl;

import com.mongodb.DBCollection;
import com.mongodb.MongoException;
import com.yookos.reco.domain.YookosUser;
import com.yookos.reco.jpa.entities.UserProfile;
import com.yookos.reco.jpa.repository.UserProfileRepository;
import com.yookos.reco.services.UserService;
import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jome on 2014/02/07.
 */

@Service
public class UserServiceImpl implements UserService {

    Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    Datastore ds;
    @Autowired
    UserProfileRepository profileRepository;

    @Override
    public YookosUser save(YookosUser user) {

        return null;
    }

    @Override
    public void bulkAddUsers(List<String> users) {
        int rowCount = 0;
        List<Map<String, Object>> holder = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();

        for (String r : users) {
            String[] rd = r.split(";");

            YookosUser user = new YookosUser();

            user.setUserid(Long.parseLong(rd[0]));
            user.setUsername(sanitize(rd[1]));
            user.setFirstname(sanitize(rd[3]));
            user.setLastname(sanitize(rd[4]));
            user.setEmail(sanitize(rd[5]));
            boolean active = false;
            int enabled = Integer.parseInt(rd[6]);
            if (enabled == 1)
                active = true;
            user.setActive(active);
            user.setCreationdate(Long.parseLong(rd[7]));

            try {
                Key<YookosUser> userKey = ds.save(user);
                log.info("User with id: " + userKey.getId() + " saved.");
            } catch (MongoException me) {
                log.error(me.getMessage());
                continue;
            }

        }
    }

    @Override
    public void bulkUpdateUserProfile() {
        DBCollection collection = ds.getCollection(YookosUser.class);
        Query<YookosUser> query = ds.createQuery(YookosUser.class);
        for (YookosUser yookosUser : query.fetch()) {
            //Get all userprofile fields associated with the user.
            for (UserProfile userProfile : profileRepository.findByUserid(yookosUser.getUserid())) {
                log.info("Processing: " + userProfile.toString());

                //A lot has to happen here. First we check what profile type this object has
                //and then accordingly set the profile field to save. There are currently 19 possibilities.
                switch (userProfile.getFieldid()) {
                    case 5001: //Gender
                        if (userProfile.getValue() != null) {
                            UpdateOperations<YookosUser> updateOperations = ds.createUpdateOperations(YookosUser.class).set("profile.gender", userProfile.getValue());
                            ds.update(yookosUser, updateOperations);
                            log.info("Updating user: " + yookosUser.getUsername() + ", " + userProfile.getName());

                        }
                        break;

                    case 5002: //Birthdate
                        Date bdate = convertStringToDate(userProfile.getValue());
                        if (bdate != null) {
                            UpdateOperations<YookosUser> updateOperations = ds.createUpdateOperations(YookosUser.class).set("profile.birthdate", bdate);
                            ds.update(yookosUser, updateOperations);
                            log.info("Updating user: " + yookosUser.getUsername() + ", " + userProfile.getName());

                        }
                        break;

                    case 5009: //Country
                        if (userProfile.getValue() != null) {
                            UpdateOperations<YookosUser> updateOperations = ds.createUpdateOperations(YookosUser.class).set("profile.country", userProfile.getValue());
                            ds.update(yookosUser, updateOperations);
                            log.info("Updating user: " + yookosUser.getUsername() + ", " + userProfile.getName());

                        }
                        break;

                    case 5010: //Hobbies
                        if (userProfile.getValue() != null) {
                            UpdateOperations<YookosUser> updateOperations = ds.createUpdateOperations(YookosUser.class).set("profile.hobbies", userProfile.getValue());
                            ds.update(yookosUser, updateOperations);
                            log.info("Updating user: " + yookosUser.getUsername() + ", " + userProfile.getName());

                        }
                        break;

                    case 8: //Biography
                        if (userProfile.getValue() != null) {
                            UpdateOperations<YookosUser> updateOperations = ds.createUpdateOperations(YookosUser.class).set("profile.about", userProfile.getValue());
                            ds.update(yookosUser, updateOperations);
                            log.info("Updating user: " + yookosUser.getUsername() + ", " + userProfile.getName());

                        }
                        break;

                    case 5012: //RelationshipStatus
                        if (userProfile.getValue() != null) {
                            UpdateOperations<YookosUser> updateOperations = ds.createUpdateOperations(YookosUser.class).set("profile.relationshipstatus", userProfile.getValue());
                            ds.update(yookosUser, updateOperations);
                            log.info("Updating user: " + yookosUser.getUsername() + ", " + userProfile.getName());

                        }
                        break;
                }
            }


        }
    }


    private String sanitize(String string) {
        String result = string.replaceAll("\"", StringUtils.EMPTY);
        return result;
    }

    private Date convertStringToDate(String birthdate) {

        if (birthdate != null) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

            try {
                Date bdate = format.parse(birthdate);
                return bdate;
            } catch (ParseException e) {
                log.error(e.getMessage());
                return null;
            }

        }
        return null;

    }
}
