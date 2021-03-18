package cn.ommiao.wechatmoments.entity;

import com.google.gson.annotations.SerializedName;

public class User extends JavaBean{

    private String username;

    private String nick;

    private String avatar;

    @SerializedName("profile-image")
    private String profileImage;

}
