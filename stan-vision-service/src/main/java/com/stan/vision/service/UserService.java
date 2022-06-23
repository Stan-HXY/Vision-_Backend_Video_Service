package com.stan.vision.service;

import com.mysql.cj.util.StringUtils;
import com.stan.vision.dao.UserDAO;
import com.stan.vision.domain.User;
import com.stan.vision.domain.constant.UserConstant;
import com.stan.vision.domain.exception.ConditionException;
import com.stan.vision.domain.UserInfo;
import com.stan.vision.service.util.MD5Util;
import com.stan.vision.service.util.RSAUtil;
import com.stan.vision.service.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public void addUser(User user) {
        String phone = user.getPhone();
        if(StringUtils.isNullOrEmpty(phone)){
            throw new ConditionException("Phone number cannot be empty!");
        }
        User dbUser = this.getUserByPhone(phone);
        if(dbUser != null){
            throw new ConditionException("This phone is already used!");
        }
        Date now = new Date();
        String salt = String.valueOf(now.getTime());
        // this password is encrypted sent by frontend
        String password = user.getPassword();
        String rawPassword;
        // so decrypt it first
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e){
            throw new ConditionException("Fail to decrypt password!");
        }
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        user.setSalt(salt);
        user.setPassword(md5Password);
        user.setCreateTime(now);

        userDAO.addUser(user);
        // add user information
        UserInfo userInfo = new UserInfo();
        userInfo.setUserID(user.getId());
        userInfo.setNickname(UserConstant.DEFAULT_NICKNAME);
        userInfo.setBirthday(UserConstant.DEFAULT_BIRTHDAY);
        userInfo.setGender(UserConstant.GENDER_UNKNOWN);
        userInfo.setCreateTime(now);

        userDAO.addUserInfo(userInfo);

        return;
    }

    public User getUserByPhone(String phone){
        return userDAO.getUserByPhone(phone);
    }

    public User getUserByPhoneOrEmail(String phone, String email) {
        return userDAO.getUserByPhoneOrEmail(phone, email);
    }

    // return a token
    public String login(User user) throws Exception{
        String phone = user.getPhone() == null ? "" : user.getPhone();
        String email = user.getEmail() == null ? "" : user.getEmail();
        if(StringUtils.isNullOrEmpty(phone) && StringUtils.isNullOrEmpty(email)){
            throw new ConditionException("Invalid Parameter!");
        }
        User dbUser = this.getUserByPhoneOrEmail(phone, email);
        if(dbUser == null){
            throw new ConditionException("This user is not exist!");
        }

        // this password is encrypted sent by frontend
        String password = user.getPassword();
        String rawPassword;
        // so decrypt it first
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e){
            throw new ConditionException("Fail to decrypt password!");
        }
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");

        if(!md5Password.equals(dbUser.getPassword())){
            throw new ConditionException("Wrong password!");
        }

        return TokenUtil.generateToken(dbUser.getId());
    }

    public User getUserInfo(Long userID) {
        User user = userDAO.getUserByID(userID);
        UserInfo userInfo = userDAO.getUserInfoByUserID(userID);
        user.setUserInfo(userInfo);
        return user;
    }

    public void updateUsers(User user) throws Exception{
        Long id = user.getId();
        User dbUser = userDAO.getUserByID(id);
        if(dbUser == null){
            throw new ConditionException("User not exist!");
        }
        if(!StringUtils.isNullOrEmpty(user.getPassword())){
            String rawPassword = RSAUtil.decrypt(user.getPassword());
            String md5Password = MD5Util.sign(rawPassword, dbUser.getSalt(), "UTF-8");
            user.setPassword(md5Password);
        }
        user.setUpdateTime(new Date());
        userDAO.updateUsers(user);
        return;
    }

    public void updateUserInfos(UserInfo userInfo) {
        userInfo.setUpdateTime(new Date());
        userDAO.updateUserInfos(userInfo);
    }

    public User getUserByID(Long followingID) {
        return userDAO.getUserByID(followingID);
    }
}
