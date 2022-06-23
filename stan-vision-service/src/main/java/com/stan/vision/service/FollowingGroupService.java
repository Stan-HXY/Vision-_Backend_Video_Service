package com.stan.vision.service;

import com.stan.vision.dao.FollowingGroupDAO;
import com.stan.vision.domain.FollowingGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowingGroupService {

    @Autowired
    private FollowingGroupDAO followingGroupDAO;

    public FollowingGroup getByType(String type){
        return followingGroupDAO.getByType(type);
    }

    public FollowingGroup getByID(Long id){
        return followingGroupDAO.getByID(id);
    }
}
