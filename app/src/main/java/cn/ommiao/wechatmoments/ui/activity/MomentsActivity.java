package cn.ommiao.wechatmoments.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import cn.ommiao.wechatmoments.R;
import cn.ommiao.wechatmoments.bridge.MomentsViewModel;
import cn.ommiao.wechatmoments.databinding.ActivityMomentsBinding;
import cn.ommiao.wechatmoments.ui.adapter.MomentsAdapter;

public class MomentsActivity extends BaseActivity<ActivityMomentsBinding> {

    private MomentsViewModel momentsViewModel;

    private MomentsAdapter adapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, MomentsActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        momentsViewModel = getActivityViewModelProvider(this).get(MomentsViewModel.class);
        mBinding.setVm(momentsViewModel);
        adapter = new MomentsAdapter(this);
        adapter.setUser(momentsViewModel.getUser());
        adapter.setList(momentsViewModel.allTweets);
        mBinding.rv.setAdapter(adapter);

        //observe more tweets callback
        momentsViewModel.tweetsMore.observe(this, (moreTweets) -> {
            int moreSize = moreTweets.size();
            if(moreSize == 0){
                shortToast(R.string.app_name);
            } else {
                int oldSize = momentsViewModel.allTweets.size();
                momentsViewModel.allTweets.addAll(moreTweets);
                if(oldSize > 0){
                    String toastLoadedMore = String.format(getResources().getString(R.string.tips_loaded_more_tweets), moreSize);
                    shortToast(toastLoadedMore);
                    adapter.notifyRangeInserted(oldSize, moreSize);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });

        momentsViewModel.loadFirstBatchTweets();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            mBinding.rv.setOnApplyWindowInsetsListener((v, insets) -> {
                v.setPadding(v.getPaddingLeft(), 0, v.getPaddingRight(), v.getPaddingBottom());
                return insets;
            });
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_moments;
    }

}