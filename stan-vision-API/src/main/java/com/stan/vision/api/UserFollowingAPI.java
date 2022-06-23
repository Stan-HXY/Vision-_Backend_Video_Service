package com.stan.vision.api;

import com.stan.vision.api.support.UserSupport;
import com.stan.vision.domain.FollowingGroup;
import com.stan.vision.domain.JsonResponse;
import com.stan.vision.domain.UserFollowing;
import com.stan.vision.service.UserFollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/user-followings")
    public JsonResponse<List<FollowingGroup>> getUserFollowings(){
        Long userID = userSupport.getCurrentUserID();
        List<FollowingGroup> result = userFollowingService.getUserFollowings(userID);
        return new JsonResponse<>(result);
    }

    @GetMapping("/user-fans")
    public JsonResponse<List<UserFollowing>> getUserFans(){
        Long userID = userSupport.getCurrentUserID();
        List<UserFollowing> result = userFollowingService.getUserFans(userID);
        return new JsonResponse<>(result);
    }

    @PostMapping("/user-following-groups")
    public JsonResponse<Long> addUserFollowingGroups(@RequestBody FollowingGroup followingGroup){
        Long userID = userSupport.getCurrentUserID();
        followingGroup.setUserID(userID);
        Long groupID = userFollowingService.addUserFollowingsGroup(followingGroup);
        return new JsonResponse<>(groupID);
    }

    @GetMapping("/user-following-groups")
    public JsonResponse<List<FollowingGroup>> getUserFollowingGroup(){
        Long userID = userSupport.getCurrentUserID();
        List<FollowingGroup> list = userFollowingService.getUserFollowingGroups(userID);
        return new JsonResponse<>(list);
    }

}



















