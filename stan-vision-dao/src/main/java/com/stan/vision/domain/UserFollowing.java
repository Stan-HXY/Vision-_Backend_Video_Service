package com.stan.vision.domain;

import java.util.Date;

public class UserFollowing {
    private Long id;
    private Long userID;
    private Long followingID;
    private Long groupID;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getFollowingID() {
        return followingID;
    }

    public void setFollowingID(Long followingID) {
        this.followingID = followingID;
    }

    public Long getGroupID() {
        return groupID;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
