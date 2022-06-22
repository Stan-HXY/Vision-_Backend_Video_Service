package com.stan.vision.api;

import com.stan.vision.api.support.UserSupport;
import com.stan.vision.domain.JsonResponse;
import com.stan.vision.domain.User;
import com.stan.vision.domain.UserInfo;
import com.stan.vision.service.UserService;
import com.stan.vision.service.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserAPI {

    @Autowired
    private UserService userService;

    @Autowired
    private UserSupport userSupport;

    @GetMapping("/users")
    public JsonResponse<User> getUserInfo(){
        Long userID = userSupport.getCurrentUserID();
        User user = userService.getUserInfo(userID);
        return new JsonResponse<>(user);
    }

    @GetMapping("/rsa-pks")
    public JsonResponse<String> getRsaPublicKey(){
        String pk = RSAUtil.getPublicKeyStr();
        return new JsonResponse<>(pk);
    }

    @PostMapping("/users")
    public JsonResponse<String> addUser(@RequestBody User user){
        userService.addUser(user);
        return JsonResponse.success();
    }

    @PostMapping("/user-tokens")
    public JsonResponse<String> login(@RequestBody User user) throws Exception{
        String token = userService.login(user);
        return new JsonResponse<>(token);
    }

    @PutMapping("/users")
    public JsonResponse<String> updateUsers(@RequestBody User user) throws Exception{
        Long userID = userSupport.getCurrentUserID();
        user.setId(userID);
        userService.updateUsers(user);
        return JsonResponse.success();
    }

    @PutMapping("/user-infos")
    public JsonResponse<String> updateUserInfos(@RequestBody UserInfo userInfo ){
        Long userID = userSupport.getCurrentUserID();
        userInfo.setUserID(userID);
        userService.updateUserInfos(userInfo);
        return JsonResponse.success();
    }

}























