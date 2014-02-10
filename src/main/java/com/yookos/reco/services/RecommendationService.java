package com.yookos.reco.services;

import com.yookos.reco.domain.RecoUser;
import com.yookos.reco.domain.ResultWrapper;

import java.util.List;

/**
 * Created by jome on 2014/02/08.
 */
public interface RecommendationService {
    List<String> getFriendsOfFriendsForUserid(long userid, int limit, int offset);

    List<ResultWrapper> getFriendsOfFriendsForUsername(String username, int limit, int offset);

    List<ResultWrapper> getFriendsOfFriendsForUsernameWithHobby(String username, int limit, int offset, String hobby);


    List<RecoUser> getMutualFriends(long userid, long fofid);
}
