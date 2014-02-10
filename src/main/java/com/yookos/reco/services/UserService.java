package com.yookos.reco.services;

import com.yookos.reco.domain.YookosUser;

import java.util.List;

/**
 * Created by jome on 2014/02/07.
 */
public interface UserService {
    YookosUser save(YookosUser user);

    void bulkAddUsers(List<String> users);
    void bulkUpdateUserProfile();

}
