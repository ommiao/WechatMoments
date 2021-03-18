package cn.ommiao.wechatmoments.bridge;

import androidx.databinding.ObservableField;
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

    public static class MomentsUserViewModel extends ViewModel {

        public final ObservableField<String> profileImageUrl = new ObservableField<>();

        public final ObservableField<String> avatarImageUrl = new ObservableField<>();

        public final ObservableField<String> nickname = new ObservableField<>();

        {
            getUser();
        }

        public void getUser(){
            User user = CachedData.getInstance().getUser();
            profileImageUrl.set(user.getProfileImage());
            avatarImageUrl.set(user.getAvatar());
            nickname.set(user.getNick());
        }

    }

}
