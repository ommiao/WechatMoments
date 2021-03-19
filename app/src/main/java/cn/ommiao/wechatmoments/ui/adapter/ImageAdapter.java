package cn.ommiao.wechatmoments.ui.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import cn.ommiao.wechatmoments.R;
import cn.ommiao.wechatmoments.databinding.ItemImageBinding;
import cn.ommiao.wechatmoments.entity.Tweet;

public class ImageAdapter extends BaseBindingAdapter<Tweet.Image, ItemImageBinding> {

    public ImageAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_image;
    }

    @Override
    protected void onBindItem(ItemImageBinding binding, Tweet.Image item, RecyclerView.ViewHolder holder) {
        BindAdapter.loadUrl(binding.iv, item.getUrl(), R.mipmap.placeholder);
    }

}
