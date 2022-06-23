package com.stan.vision.service;

import com.stan.vision.dao.UserFollowingDAO;
import com.stan.vision.domain.FollowingGroup;
import com.stan.vision.domain.User;
import com.stan.vision.domain.UserFollowing;
import com.stan.vision.domain.constant.UserConstant;
import com.stan.vision.domain.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
    }
}
