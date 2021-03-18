package cn.ommiao.wechatmoments.bridge;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import cn.ommiao.wechatmoments.constant.CachedData;
import cn.ommiao.wechatmoments.entity.Tweet;
import cn.ommiao.wechatmoments.entity.User;

public class MomentsViewModel extends ViewModel {

    public final ArrayList<Tweet> allTweets = new ArrayList<>();

    public final MutableLiveData<ArrayList<Tweet>> tweetsMore = new MutableLiveData<>();

    public void loadFirstBatchTweets(){
        tweetsMore.postValue(CachedData.getInstance().loadMoreTweet());
    }

    public void loadMoreTweets(){
        tweetsMore.postValue(CachedData.getInstance().loadMoreTweet());
    }

    public User getUser(){
        return CachedData.getInstance().getUser();
    }

}
