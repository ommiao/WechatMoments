package cn.ommiao.wechatmoments.ui.adapter;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class BindAdapter {

    @BindingAdapter(value = {"imageUrl", "placeholder"}, requireAll = false)
    public static void loadUrl(ImageView view, String url, @DrawableRes int placeholder){
        Glide.with(view).load(url).placeholder(placeholder).into(view);
    }

    @BindingAdapter(value = {"tint"}, requireAll = false)
    public static void tint(ImageView view, @ColorRes int tintColor){
        view.setColorFilter(view.getResources().getColor(tintColor));
    }

    @BindingAdapter(value = {"bgColor"}, requireAll = false)
    public static void bgColor(View view, int color){
        view.setBackground(new ColorDrawable(color));
    }

}
