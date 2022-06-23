package com.stan.vision.api;

import com.stan.vision.api.support.UserSupport;
import com.stan.vision.domain.JsonResponse;
import com.stan.vision.domain.UserFollowing;
import com.stan.vision.service.UserFollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserFollowingAPI {

    @Autowired
    private UserFollowingService userFollowingService;

    @Autowired
    private UserSupport userSupport;

    @PostMapping("/user-followings")
    public JsonResponse<String> addUserFollowings(@RequestBody UserFollowing userFollowing){
        Long userID = userSupport.getCurrentUserID();
        userFollowing.setUserID(userID);
        userFollowingService.addUserFollowings(userFollowing);
        return JsonResponse.success();
    }
}
