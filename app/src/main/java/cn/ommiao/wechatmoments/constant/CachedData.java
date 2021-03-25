package cn.ommiao.wechatmoments.constant;

import java.util.ArrayList;

import cn.ommiao.wechatmoments.entity.Tweet;
import cn.ommiao.wechatmoments.entity.User;

/**
 * @author LiKai
 */
public class CachedData {

    private static volatile CachedData mInstance;

    private User user;

    private ArrayList<Tweet> tweets;

    private int currentIndex = 0;

    public static CachedData getInstance() {
        if(mInstance == null){
            throw new IllegalArgumentException("You haven't initialize your cached data!");
        }
        return mInstance;
    }

    public static void init(User user, ArrayList<Tweet> tweets){
        synchronized (CachedData.class){
            mInstance = new CachedData();
            mInstance.user = user;
            mInstance.tweets = tweets;
        }
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Tweet> loadMoreTweet(){
        ArrayList<Tweet> moreTweets = new ArrayList<>();
        for (int i = currentIndex; i < tweets.size(); i++) {
            if(moreTweets.size() == Config.QUANTITY_LOAD_MORE){
                break;
            }
            Tweet tweet = tweets.get(i);
            if(tweet.isInvalid()){
                moreTweets.add(tweet);
            }
            currentIndex += 1;
        }
        return moreTweets;
    }

    public void reset(){
        currentIndex = 0;
    }

}
