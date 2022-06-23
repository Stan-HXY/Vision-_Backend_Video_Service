package com.stan.vision.service;

import com.stan.vision.dao.FollowingGroupDAO;
import com.stan.vision.dao.UserFollowingDAO;
import com.stan.vision.domain.FollowingGroup;
import com.stan.vision.domain.User;
import com.stan.vision.domain.UserFollowing;
import com.stan.vision.domain.UserInfo;
import com.stan.vision.domain.constant.UserConstant;
import com.stan.vision.domain.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserFollowingService {

    @Autowired
    private UserFollowingDAO userFollowingDAO;

    @Autowired
    private FollowingGroupService followingGroupService;

    @Autowired
    private UserService userService;

    @Transactional
    public void addUserFollowings(UserFollowing userFollowing){
        Long groupID = userFollowing.getGroupID();
        if(groupID == null){
            FollowingGroup followingGroup = followingGroupService.getByType(UserConstant.USER_FOLLOWING_GROUP_TYPE_DEFAULT);
            userFollowing.setGroupID(followingGroup.getId());
        }
        else{
            FollowingGroup followingGroup = followingGroupService.getByID(groupID);
            if(followingGroup == null){
                throw new ConditionException("Group not exist!");
            }
        }
        Long followingID = userFollowing.getId();
        User user = userService.getUserByID(followingID);
        if(user == null){
            throw new ConditionException("Followed user not exist!");
        }
        userFollowingDAO.deleteUserFollowing(userFollowing.getUserID(), followingID);
        userFollowing.setCreateTime(new Date());
        userFollowingDAO.addUserFollowing(userFollowing);
        return;
    }


    /* 1. get Following List
    * 2. query userInfo by userID in list
    * 3. Group the following user based on GroupID
    * */
    public List<FollowingGroup> getUserFollowings(Long userID){
        // 1
        List<UserFollowing> list = userFollowingDAO.getUserFollowings(userID);
        Set<Long> followingIDSet = list.stream().map(UserFollowing::getFollowingID).collect(Collectors.toSet());
        List<UserInfo> userInfoList = new ArrayList<>();
        // 2
        if(followingIDSet.size() > 0){
            userInfoList = userService.getUserInfoByUserIDs(followingIDSet);
        }
        for(UserFollowing userFollowing : list){
            for(UserInfo userInfo : userInfoList){
                if(userFollowing.getFollowingID().equals(userInfo.getUserID())){
                    userFollowing.setUserInfo(userInfo);
                }
            }
        }
        // 3
        List<FollowingGroup> groupList = followingGroupService.getByUserID(userID);
        FollowingGroup allGroup = new FollowingGroup();
        allGroup.setName(UserConstant.USER_FOLLOWING_GROUP_ALL_NAME);
        allGroup.setFollowingUserInfoList(userInfoList);

        List<FollowingGroup> result = new ArrayList<>();
        result.add(allGroup);
        for(FollowingGroup group : groupList){
            List<UserInfo> infoList = new ArrayList<>();
            for(UserFollowing userFollowing : list){
                if(group.getId().equals(userFollowing.getGroupID())){
                    infoList.add(userFollowing.getUserInfo());
                }
            }
            group.setFollowingUserInfoList(infoList);
            result.add(group);
        }
        return result;
    }






}













