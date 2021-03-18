package cn.ommiao.wechatmoments.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class BindAdapter {

    @BindingAdapter(value = {"imageUrl", "placeholder"}, requireAll = false)
    public static void loadUrl(ImageView view, String url, @DrawableRes int placeholder){
        Glide.with(view).load(url).placeholder(placeholder).into(view);
    }

}
