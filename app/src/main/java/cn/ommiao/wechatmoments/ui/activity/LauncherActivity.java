package cn.ommiao.wechatmoments.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import cn.ommiao.wechatmoments.R;
import cn.ommiao.wechatmoments.bridge.LauncherViewModel;
import cn.ommiao.wechatmoments.databinding.ActivityLauncherBinding;

public class LauncherActivity extends BaseActivity<ActivityLauncherBinding> {

    private LauncherViewModel launcherViewModel;
    private ClickProxy clickProxy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launcherViewModel = getActivityViewModelProvider(this).get(LauncherViewModel.class);
        mBinding.setVm(launcherViewModel);
        clickProxy = new ClickProxy();
        mBinding.setClick(clickProxy);
        launcherViewModel.loadingSuccess.observe(this, (success) -> {
            //jump to moments page
            MomentsActivity.start(this);
            finish();
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launcher;
    }

    public class ClickProxy {

        public void retry(){
            launcherViewModel.loadData();
        }

    }
}