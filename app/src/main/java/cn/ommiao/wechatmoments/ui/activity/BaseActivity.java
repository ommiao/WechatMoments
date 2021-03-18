package cn.ommiao.wechatmoments.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import cn.ommiao.wechatmoments.App;
import cn.ommiao.wechatmoments.bridge.SharedViewModel;


public abstract class BaseActivity<B extends ViewDataBinding> extends AppCompatActivity {

    protected B mBinding;

    protected SharedViewModel mSharedViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedViewModel = getAppViewModelProvider().get(SharedViewModel.class);
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mBinding.setLifecycleOwner(this);
    }

    protected abstract @LayoutRes
    int getLayoutId();

    protected ViewModelProvider getAppViewModelProvider() {
        return ((App) getApplicationContext()).getAppViewModelProvider(this);
    }

    protected ViewModelProvider getActivityViewModelProvider(AppCompatActivity activity) {
        return new ViewModelProvider(activity, activity.getDefaultViewModelProviderFactory());
    }

    protected void shortToast(String content){
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    protected void shortToast(@StringRes int stringResId){
        shortToast(getResources().getString(stringResId));
    }

}
