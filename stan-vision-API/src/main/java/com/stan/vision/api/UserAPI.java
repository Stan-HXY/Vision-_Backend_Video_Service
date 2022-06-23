package com.stan.vision.api;

import com.alibaba.fastjson.JSONObject;
import com.stan.vision.api.support.UserSupport;
import com.stan.vision.domain.JsonResponse;
import com.stan.vision.domain.PageResult;
import com.stan.vision.domain.User;
import com.stan.vision.domain.UserInfo;
import com.stan.vision.service.UserFollowingService;
import com.stan.vision.service.UserService;
import com.stan.vision.service.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserAPI {

    @Autowired
    private UserService userService;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private UserFollowingService userFollowingService;

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

    /* 分页查询
    @no : page number
    @size : page display size
     */
    @GetMapping("/user-infos")
    public JsonResponse<PageResult<UserInfo>> pageListUserInfos(@RequestParam Integer no, @RequestParam Integer size, String nickname){
        Long userID = userSupport.getCurrentUserID();
        JSONObject params = new JSONObject();
        params.put("no", no);
        params.put("size", size);
        params.put("nickname", nickname);
        params.put("userID", userID);

        PageResult<UserInfo> result = userService.pageListUserInfos(params);
        if(result.getTotal() > 0){
            List<UserInfo> checkedUserInfoList = userFollowingService.checkFollowingStatus(result.getList(), userID);
            result.setList(checkedUserInfoList);
        }
        return new JsonResponse<>(result);
    }
}























