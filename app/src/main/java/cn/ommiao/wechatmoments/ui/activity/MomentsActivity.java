package cn.ommiao.wechatmoments.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import cn.ommiao.wechatmoments.R;
import cn.ommiao.wechatmoments.bridge.MomentsViewModel;
import cn.ommiao.wechatmoments.databinding.ActivityMomentsBinding;
import cn.ommiao.wechatmoments.ui.adapter.MomentsAdapter;
import cn.ommiao.wechatmoments.ui.other.RecyclerViewScrollListenerHelper;

public class MomentsActivity extends BaseActivity<ActivityMomentsBinding> {

    private static final long DURATION_REFRESH_ROTATION = 2000L;
    private static final int COUNT_REFRESH_ROTATION = 3;
    private static final long DURATION_RESET_PROGRESS_BAR = 500L;

    private MomentsViewModel momentsViewModel;
    private MomentsViewModel.MomentsUserViewModel momentsUserViewModel;
    private ArrayList<MomentsViewModel.MomentsTweetViewModel> momentsTweetViewModels = new ArrayList<>();

    private MomentsAdapter adapter;

    private ClickProxy clickProxy;

    private ValueAnimator animatorRotation;

    private ValueAnimator animatorTranslationY;

    private boolean refreshed = false;
    private int thresholdTopBarProgress;

    private boolean progressBarAnimating;

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
        //observe more tweets loaded
        momentsViewModel.tweetsMore.observe(this, (moreTweets) -> {
            if(refreshed){
                momentsTweetViewModels.clear();
                refreshed = false;
                shortToast(R.string.tips_refreshed_tweets);
            }
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
        //observe refresh finished
        momentsViewModel.refreshFinished.observe(this, (refreshFinished) -> {
            if(animatorRotation != null){
                animatorRotation.cancel();
            }
            makeResetProgressBarAnimator();
        });
    }

    private void listenListScroll(){

        final int thresholdTopBar = getResources().getDimensionPixelOffset(R.dimen.threshold_moments_top_bar);
        thresholdTopBarProgress = getResources().getDimensionPixelOffset(R.dimen.threshold_top_tweets_progress_bar);

        RecyclerViewScrollListenerHelper.bindListener(mBinding.rv, new RecyclerViewScrollListenerHelper.OnScrollListener() {
            @Override
            public void onScrollUpOnEnd() {
                if(!momentsViewModel.loadingMore.get()){
                    momentsViewModel.loadMoreTweets(MomentsActivity.this);
                }
            }

            @Override
            public void onScrollDownOnStart(int dy) {
                if(progressBarAnimating){
                    return;
                }
                int translationY = Math.min(dy, thresholdTopBarProgress);
                momentsViewModel.progressBarTranslationY.set(translationY);
                momentsViewModel.progressBarRotation.set(dy);
            }

            @Override
            public void onScrollDownOnStartRelease() {
                int translationY = momentsViewModel.progressBarTranslationY.get();
                if(translationY == thresholdTopBarProgress){
                    //refresh list and reset progress bar
                    progressBarAnimating = true;
                    momentsViewModel.refreshTweets(MomentsActivity.this);
                    refreshed = true;
                    makeRefreshAnimator();
                } else if(translationY > 0){
                    //reset progress bar
                    progressBarAnimating = true;
                    makeResetProgressBarAnimator();
                }
            }

            @Override
            public void onScrolled(int totalY) {
                changeTopBarStyle(thresholdTopBar, totalY);
            }
        });

    }

    private void makeRefreshAnimator(){
        int rotationCurrent = momentsViewModel.progressBarRotation.get();
        int rotationFinal = rotationCurrent + COUNT_REFRESH_ROTATION * 360;
        animatorRotation = ValueAnimator.ofInt(rotationCurrent, rotationFinal);
        animatorRotation.setDuration(DURATION_REFRESH_ROTATION);
        animatorRotation.setRepeatCount(ValueAnimator.INFINITE);
        animatorRotation.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            momentsViewModel.progressBarRotation.set(value);
        });
        animatorRotation.start();
    }

    private void makeResetProgressBarAnimator(){
        int translationYCurrent = momentsViewModel.progressBarTranslationY.get();
        int translationYFinal = 0;
        animatorTranslationY = ValueAnimator.ofInt(translationYCurrent, translationYFinal);
        long duration = (long) (translationYCurrent / (float)thresholdTopBarProgress * DURATION_RESET_PROGRESS_BAR);
        animatorTranslationY.setDuration(duration);
        animatorTranslationY.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            momentsViewModel.progressBarTranslationY.set(value);
        });
        animatorTranslationY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBarAnimating = false;
            }
        });
        animatorTranslationY.start();
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