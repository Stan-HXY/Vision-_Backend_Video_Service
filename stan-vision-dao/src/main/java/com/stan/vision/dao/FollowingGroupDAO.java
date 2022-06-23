package com.stan.vision.dao;

import com.stan.vision.domain.FollowingGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FollowingGroupDAO {

    FollowingGroup getByType(String type);

    FollowingGroup getByID(Long id);

    List<FollowingGroup> getByUserID(Long userID);

    Integer addFollowingGroup(FollowingGroup followingGroup);

    List<FollowingGroup> getUserFollowingGroups(Long userID);
}
