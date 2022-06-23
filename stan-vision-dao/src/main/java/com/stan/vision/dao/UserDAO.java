package com.stan.vision.dao;

import com.stan.vision.domain.User;
import com.stan.vision.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDAO {

    User getUserByPhone(String phone);

    Integer addUser(User user);

    Integer addUserInfo(UserInfo userInfo);

    User getUserByID(Long id);

    UserInfo getUserInfoByUserID(Long userID);

    Integer updateUsers(User user);

    Integer updateUserInfos(UserInfo userInfo);

    User getUserByPhoneOrEmail(@Param("phone") String phone, @Param("email") String email);
}
