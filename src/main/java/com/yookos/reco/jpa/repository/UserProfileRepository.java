package com.yookos.reco.jpa.repository;

import com.yookos.reco.jpa.entities.UserProfile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by jome on 2014/02/07.
 */
public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {

    List<UserProfile> findByUserid(long userid);

}
