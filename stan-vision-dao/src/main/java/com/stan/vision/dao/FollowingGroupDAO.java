package com.stan.vision.dao;

import com.stan.vision.domain.FollowingGroup;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FollowingGroupDAO {

    FollowingGroup getByType(String type);

    FollowingGroup getByID(Long id);
}
