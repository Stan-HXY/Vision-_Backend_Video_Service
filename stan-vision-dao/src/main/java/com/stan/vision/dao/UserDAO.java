package com.stan.vision.dao;

import com.stan.vision.domain.User;
import com.stan.vision.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDAO {

    User getUserByPhone(String phone);

    Integer addUser(User user);

    Integer addUserInfo(UserInfo userInfo);

    User getUserByID(Long id);

    UserInfo getUserInfoByUserID(Long userID);
}
