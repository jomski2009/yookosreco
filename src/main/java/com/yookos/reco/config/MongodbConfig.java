package com.yookos.reco.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.yookos.reco.domain.YookosUser;
import org.mongodb.morphia.AdvancedDatastore;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;

/**
 * Created by jome on 2014/02/06.
 */

@Configuration
public class MongodbConfig {

    @Bean(name = "mongo")
    Mongo mongoClient() {
        MongoClient client = null;
        try {
            client = new MongoClient("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Bean
    Datastore morphia() {
        Morphia morphia = new Morphia();
        morphia.mapPackage("com.yookos.reco.domain");

        Datastore ds = morphia.createDatastore(mongoClient(), "yookos");
        AdvancedDatastore ads = (AdvancedDatastore) ds;
        ads.ensureIndexes();

        ads.ensureIndex(YookosUser.class, "username", "username", true, true);
        ads.ensureIndex(YookosUser.class, "email", "email", true, true);

//        ads.ensureIndex(User.class, "email", "email", true, true);
//        ads.ensureIndex(User.class, "cellnumber", "cellnumber", true, true);
//        ads.ensureIndex(Group.class, "name_userid", "name, user_id", true, true);


        return ads;
    }
}
