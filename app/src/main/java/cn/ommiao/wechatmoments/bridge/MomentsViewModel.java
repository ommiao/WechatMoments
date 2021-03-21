package cn.ommiao.wechatmoments.bridge;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Pair;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import cn.ommiao.wechatmoments.R;
import cn.ommiao.wechatmoments.constant.CachedData;
import cn.ommiao.wechatmoments.entity.Tweet;
import cn.ommiao.wechatmoments.entity.User;
import cn.ommiao.wechatmoments.util.ThreadPool;

public class MomentsViewModel extends ViewModel {

    public final MutableLiveData<ArrayList<MomentsTweetViewModel>> tweetsMore = new MutableLiveData<>();

    public final ObservableBoolean whiteTopBar = new ObservableBoolean();

    public final ObservableBoolean loadingMore = new ObservableBoolean();

    public final MutableLiveData<Boolean> refreshFinished = new MutableLiveData<>();

    public final ObservableInt progressBarTranslationY = new ObservableInt();

    public final ObservableInt progressBarRotation = new ObservableInt();

    public final ObservableInt topBarBgColor = new ObservableInt();

    {
        whiteTopBar.set(true);
    }

    public void refreshTweets(Context context){
        ThreadPool.getHttpExecutor().execute(() -> {
            CachedData.getInstance().reset();
            //delay 2s
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            loadFirstBatchTweets(context);
            refreshFinished.postValue(true);
        });
    }

    public void loadFirstBatchTweets(Context context){
        loadMoreTweets(context);
    }

    public void loadMoreTweets(Context context){
        loadingMore.set(true);
        ArrayList<Tweet> tweets = CachedData.getInstance().loadMoreTweet();
        ArrayList<MomentsTweetViewModel> tweetViewModels = new ArrayList<>();
        for (Tweet tweet : tweets) {
            MomentsTweetViewModel tweetViewModel = new MomentsTweetViewModel();
            convertTweetViewModel(context, tweet, tweetViewModel);
            tweetViewModels.add(tweetViewModel);
        }
        tweetsMore.postValue(tweetViewModels);
        loadingMore.set(false);
    }

    private void convertTweetViewModel(Context context, Tweet tweet, MomentsTweetViewModel tweetViewModel){
        tweetViewModel.content.set(tweet.getContent());
        tweetViewModel.nickname.set(tweet.getSender().getNick());
        tweetViewModel.avatar.set(tweet.getSender().getAvatar());
        StringBuilder builder = new StringBuilder();
        ArrayList<Pair<Integer, Integer>> spans = new ArrayList<>();
        for (Tweet.Comment comment : tweet.getComments()) {
            String nick = comment.getSender().getNick();
            String content = comment.getContent();
            int start = builder.length();
            int end = start + nick.length();
            Pair<Integer, Integer> spanPair = new Pair<>(start, end);
            builder.append(nick).append(": ").append(content).append("\n");
            spans.add(spanPair);
        }
        if(tweet.getComments().size() > 0){
            String s = builder.toString();
            String allComments = s.substring(0, s.length() - "\n".length());
            SpannableString spannableComments = new SpannableString(allComments);
            for (Pair<Integer, Integer> span : spans) {
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.blue));
                spannableComments.setSpan(colorSpan, span.first, span.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tweetViewModel.comments.set(spannableComments);
        }
        tweetViewModel.hasImages.set(tweet.getImages().size() > 0);
        tweetViewModel.images.set(tweet.getImages());
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

    public static class MomentsTweetViewModel extends ViewModel {

        public final ObservableField<String> nickname = new ObservableField<>();

        public final ObservableField<String> content = new ObservableField<>();

        public final ObservableField<String> avatar = new ObservableField<>();

        public final ObservableBoolean hasImages = new ObservableBoolean();

        public final ObservableField<SpannableString> comments = new ObservableField<>();

        public final ObservableField<ArrayList<Tweet.Image>> images = new ObservableField<>();

    }

}
