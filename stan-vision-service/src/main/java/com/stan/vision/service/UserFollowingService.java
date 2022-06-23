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


    /* 1. get Fans List
     * 2. query userInfo by userID in Fans List
     * 3. Check whether this user followed this fan(互粉状态要在前端有展示)
     * */
    public List<UserFollowing> getUserFans(Long userID){
        List<UserFollowing> fanList = userFollowingDAO.getUserFans(userID);
        Set<Long> fanIDSet = fanList.stream().map(UserFollowing::getUserID).collect(Collectors.toSet());
        List<UserInfo> userInfoList = new ArrayList<>();
        if(fanIDSet.size() > 0){
            userInfoList = userService.getUserInfoByUserIDs(fanIDSet);
        }
        List<UserFollowing> followingList = userFollowingDAO.getUserFollowings(userID);
        for(UserFollowing fan : fanList){
            for(UserInfo userInfo : userInfoList){
                if(fan.getUserID().equals(userInfo.getUserID())){
                    userInfo.setFollowed(false);
                    fan.setUserInfo(userInfo);
                }
            }
            for(UserFollowing following : followingList){
                if(following.getFollowingID().equals(fan.getUserID())){
                    fan.getUserInfo().setFollowed(true);
                }
            }
        }
        return fanList;
    }


    public Long addUserFollowingsGroup(FollowingGroup followingGroup) {
        followingGroup.setCreateTime(new Date());
        followingGroup.setType(UserConstant.USER_FOLLOWING_GROUP_TYPE_USER);
        followingGroupService.addFollowingGroup(followingGroup);
        return followingGroup.getId();
    }


    public List<FollowingGroup> getUserFollowingGroups(Long userID) {
        return followingGroupService.getUserFollowingGroups(userID);
    }


    public List<UserInfo> checkFollowingStatus(List<UserInfo> userInfoList, Long userID){
        List<UserFollowing> userFollowingList = userFollowingDAO.getUserFollowings(userID);
        for(UserInfo userInfo : userInfoList){
            userInfo.setFollowed(false);
            for(UserFollowing userFollowing : userFollowingList){
                if(userFollowing.getFollowingID().equals(userInfo.getUserID())){
                    userInfo.setFollowed(true);
                }
            }
        }
        return userInfoList;
    }
}













