package com.yookos.reco.controllers;

import com.yookos.reco.services.SocialGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jome on 2014/02/10.
 */

@RestController
@RequestMapping("groups")
public class SocialGroupController {
    @Autowired
    SocialGroupService socialGroupService;

    @RequestMapping("csv/addgroups")
    public ResponseEntity<String> addUsersToMongoStore(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("Starting csv processing...");
            List<String> rows = new ArrayList<String>();

            if (!file.isEmpty()) {
                String row = "";
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        file.getInputStream()));
                while ((row = br.readLine()) != null) {
                    rows.add(row);
                }
                br.close();

                rows.remove(0);

                socialGroupService.bulkAddGroups(rows);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

}
