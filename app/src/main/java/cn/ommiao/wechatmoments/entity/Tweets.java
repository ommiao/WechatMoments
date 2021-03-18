package cn.ommiao.wechatmoments.entity;

import java.util.ArrayList;

public class Tweets extends JavaBean {

    private ArrayList<Tweet> tweets = new ArrayList<>();

    public ArrayList<Tweet> getTweets() {
        return tweets;
    }
}
