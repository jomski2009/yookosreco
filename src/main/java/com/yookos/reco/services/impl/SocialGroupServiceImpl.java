package com.yookos.reco.services.impl;

import com.mongodb.MongoException;
import com.yookos.reco.domain.SocialGroup;
import com.yookos.reco.domain.YookosUser;
import com.yookos.reco.services.SocialGroupService;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jome on 2014/02/10.
 */
@Service
public class SocialGroupServiceImpl implements SocialGroupService {
    @Autowired
    Datastore ds;

    @Override
    public void bulkAddGroups(List<String> groups) {
        Logger log = LoggerFactory.getLogger(this.getClass());

        int rowCount = 0;
        List<Map<String, Object>> holder = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();

        for (String group : groups) {
            String[] rd = group.split(";");

            SocialGroup socialGroup = new SocialGroup();

            socialGroup.setGroupid(Long.parseLong(rd[0]));
            socialGroup.setGrouptype(Integer.parseInt(rd[1]));
            socialGroup.setName(rd[2]);
            socialGroup.setDisplayname(rd[3]);
            socialGroup.setDescription(rd[4]);
            socialGroup.setUserid(Long.parseLong(rd[5]));
            socialGroup.setCreationdate(Long.parseLong(rd[6]));
            socialGroup.setModificationdate(Long.parseLong(rd[7]));
            socialGroup.setStatus(Integer.parseInt(rd[8]));

            try {
                Key<SocialGroup> groupKey = ds.save(socialGroup);
                log.info("Group with id: " + groupKey.getId() + " saved.");
            } catch (MongoException me) {
                log.error(me.getMessage());
                continue;
            }

        }
    }
}
