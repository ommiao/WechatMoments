package cn.ommiao.wechatmoments.bridge;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.orhanobut.logger.Logger;

import cn.ommiao.wechatmoments.connection.HttpProxy;
import cn.ommiao.wechatmoments.constant.CachedData;
import cn.ommiao.wechatmoments.constant.Urls;
import cn.ommiao.wechatmoments.entity.Tweets;
import cn.ommiao.wechatmoments.entity.User;
import cn.ommiao.wechatmoments.util.ThreadPool;

/**
 * @author LiKai
 */
public class LauncherViewModel extends ViewModel {

    public final ObservableBoolean loading = new ObservableBoolean();

    public final MutableLiveData<Boolean> loadingSuccess = new MutableLiveData<>();

    public final ObservableField<String> errorTips = new ObservableField<>();

    {
        loading.set(true);
        loadData();
    }

    public void loadData(){
        ThreadPool.getHttpExecutor().execute(() -> {
            try {
                String data = HttpProxy.getSync(Urls.URL_USER);

                User user = User.fromJson(data, User.class);

                data = HttpProxy.getSync(Urls.URL_TWEETS);

                Tweets tweets = Tweets.fromJson(String.format("{\"tweets\":%s}", data), Tweets.class);

                CachedData.init(user, tweets.getTweets());

                Logger.d(user.toJson());
                Logger.d(tweets.toJson());

                loading.set(false);
                loadingSuccess.postValue(true);
            } catch (Exception e) {
                loading.set(false);
                e.printStackTrace();
                errorTips.set(e.getMessage());
            }
        });

    }

}
