package com.yookos.reco.controllers.web;

import com.yookos.reco.domain.RecoQuery;
import com.yookos.reco.domain.RecoUser;
import com.yookos.reco.domain.ResultWrapper;
import com.yookos.reco.services.RecommendationService;
import com.yookos.reco.services.UserAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by jome on 2014/02/06.
 */

@RequestMapping("/")
@Controller
public class HomeController {
    @Autowired
    UserAnalysisService analysisService;

    @Autowired
    RecommendationService recommendationService;

    @RequestMapping("{name}")
    public String showHome(@PathVariable("name") String name, Model model) {
        model.addAttribute("name", name);
        return "home";
    }

    @RequestMapping(value = "/reco", method = RequestMethod.GET)
    public String recoForm(Model model) {
        model.addAttribute("query", new RecoQuery());
        return "reco";
    }

    @RequestMapping(value = "/reco", method = RequestMethod.POST)
    public String greetingSubmit(@ModelAttribute RecoQuery query, Model model) {
        //Of course we have to validate the query object here...
        //If all the fields are empty, return back to the form.

        String result = analysisService.getQueryResult(query);

        model.addAttribute("result", result);
        return "recoresult";
    }

    @RequestMapping(value = "/fof/{username}/{limit}/{offset}")
    public String fofrecommendations(@PathVariable("username") String username, @PathVariable("limit") int limit, @PathVariable("offset") int offset, Model model) {
        List<ResultWrapper> results = recommendationService.getFriendsOfFriendsForUsername(username, limit, offset);
        model.addAttribute("results", results);

        return "fofresult";
    }

    @RequestMapping(value = "/fof/{username}/likes/{hobby}/{limit}/{offset}")
    public String fofrecommendationsWithLikes(@PathVariable("username") String username, @PathVariable("limit") int limit, @PathVariable("offset") int offset, @PathVariable("hobby") String hobby, Model model) {
        List<ResultWrapper> results = recommendationService.getFriendsOfFriendsForUsernameWithHobby(username, limit, offset, hobby);
        model.addAttribute("results", results);

        return "foflikesresult";
    }

    @RequestMapping("/friends/{userid}/{fofid}")
    public String getMutualFriends(@PathVariable("userid") long userid, @PathVariable("fofid") long fofid, Model model){
        List<RecoUser> friendslist = recommendationService.getMutualFriends(userid, fofid);
        model.addAttribute("flist", friendslist);

        return "mutualfriends";
    }
}
