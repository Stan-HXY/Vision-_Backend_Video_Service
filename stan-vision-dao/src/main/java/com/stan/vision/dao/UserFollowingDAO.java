package com.stan.vision.dao;

import com.stan.vision.domain.UserFollowing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserFollowingDAO {

    Integer deleteUserFollowing(@Param("userID") Long userID, @Param("followingID") Long followingID);

    Integer addUserFollowing(UserFollowing userFollowing);
}
