package cn.ommiao.wechatmoments.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import cn.ommiao.wechatmoments.R;
import cn.ommiao.wechatmoments.bridge.MomentsViewModel;
import cn.ommiao.wechatmoments.databinding.ActivityMomentsBinding;
import cn.ommiao.wechatmoments.ui.adapter.MomentsAdapter;
import cn.ommiao.wechatmoments.ui.other.RecyclerViewScrollListenerHelper;

public class MomentsActivity extends BaseActivity<ActivityMomentsBinding> {

    private MomentsViewModel momentsViewModel;
    private MomentsViewModel.MomentsUserViewModel momentsUserViewModel;
    private ArrayList<MomentsViewModel.MomentsTweetViewModel> momentsTweetViewModels = new ArrayList<>();

    private MomentsAdapter adapter;

    private ClickProxy clickProxy;

    public static void start(Context context) {
        Intent starter = new Intent(context, MomentsActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        momentsViewModel = getActivityViewModelProvider(this).get(MomentsViewModel.class);
        momentsUserViewModel = getActivityViewModelProvider(this).get(MomentsViewModel.MomentsUserViewModel.class);
        mBinding.setVm(momentsViewModel);

        clickProxy = new ClickProxy();
        mBinding.setClick(clickProxy);

        fixTopBarPadding();

        bindData();

        listenListScroll();

    }

    private void fixTopBarPadding() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            mBinding.rv.setOnApplyWindowInsetsListener((v, insets) -> {
                v.setPadding(v.getPaddingLeft(), 0, v.getPaddingRight(), v.getPaddingBottom());
                return insets;
            });
        }
    }

    private void bindData() {
        adapter = new MomentsAdapter(this);
        adapter.setUser(momentsUserViewModel);
        adapter.setList(momentsTweetViewModels);
        mBinding.rv.setAdapter(adapter);
        //observe more tweets callback
        momentsViewModel.tweetsMore.observe(this, (moreTweets) -> {
            int moreSize = moreTweets.size();
            if(moreSize == 0){
                shortToast(R.string.tips_no_more_tweets);
            } else {
                int oldSize = momentsTweetViewModels.size();
                momentsTweetViewModels.addAll(moreTweets);
                if(oldSize > 0){
                    String toastLoadedMore = String.format(getResources().getString(R.string.tips_loaded_more_tweets), moreSize);
                    shortToast(toastLoadedMore);
                    adapter.notifyRangeInserted(oldSize, moreSize);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });
        //load first batch
        momentsViewModel.loadFirstBatchTweets(this);
    }

    private void listenListScroll(){

        final int thresholdTopBar = getResources().getDimensionPixelOffset(R.dimen.threshold_moments_top_bar);

        RecyclerViewScrollListenerHelper.bindListener(mBinding.rv, new RecyclerViewScrollListenerHelper.OnScrollListener() {
            @Override
            public void onScrollUpOnEnd() {
                if(!momentsViewModel.loadingMore.get()){
                    momentsViewModel.loadMoreTweets(MomentsActivity.this);
                }
            }

            @Override
            public void onScrollDownOnStart() {

            }

            @Override
            public void onScrolled(int totalY) {
                changeTopBarStyle(thresholdTopBar, totalY);
            }
        });


    }

    private void changeTopBarStyle(int threshold, int offsetY){
        float alpha = Math.abs(offsetY - threshold) / (float) 100;
        if(alpha > 1 && mBinding.flTopBar.getAlpha() == 1){
            return;
        }
        mBinding.flTopBar.setAlpha(alpha);
        if(offsetY > threshold){
            momentsViewModel.whiteTopBar.set(false);
            momentsViewModel.topBarBgColor.set(getResources().getColor(R.color.gray));
            setStatusBarMode(true);
        } else {
            momentsViewModel.whiteTopBar.set(true);
            momentsViewModel.topBarBgColor.set(Color.TRANSPARENT);
            setStatusBarMode(false);
        }
    }

    private void setStatusBarMode(boolean dark) {
        View decor = getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_moments;
    }

    public class ClickProxy {

        public void back(){
            finish();
        }

    }

}