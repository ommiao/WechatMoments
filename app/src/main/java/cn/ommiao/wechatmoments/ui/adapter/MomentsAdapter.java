package cn.ommiao.wechatmoments.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import cn.ommiao.wechatmoments.R;
import cn.ommiao.wechatmoments.bridge.MomentsViewModel;
import cn.ommiao.wechatmoments.databinding.ItemHeaderBinding;
import cn.ommiao.wechatmoments.databinding.ItemTweetBinding;
import cn.ommiao.wechatmoments.ui.other.MomentTweetImageDecoration;

public class MomentsAdapter extends BaseBindingAdapter<MomentsViewModel.MomentsTweetViewModel, ViewDataBinding> {

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_TWEET = 0;

    private MomentsViewModel.MomentsUserViewModel user;

    private final RecyclerView.RecycledViewPool imagesViewPool;

    private final MomentTweetImageDecoration imageDecoration;

    public MomentsAdapter(Context context) {
        super(context);
        imagesViewPool = new RecyclerView.RecycledViewPool();
        imageDecoration = new MomentTweetImageDecoration(context);
    }

    public void setUser(MomentsViewModel.MomentsUserViewModel user){
        this.user = user;
    }

    public void notifyRangeInserted(int position, int count) {
        notifyItemRangeInserted(position + 1, count);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return viewType == TYPE_TWEET ? R.layout.item_tweet : R.layout.item_header;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_TWEET;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_HEADER){
            ItemHeaderBinding binding = DataBindingUtil.getBinding(holder.itemView);
            this.onBindHeader(binding, user);
            if(binding != null){
                binding.executePendingBindings();
            }
        } else {
            super.onBindViewHolder(holder, position - 1);
        }
    }

    private void onBindHeader(ViewDataBinding binding, MomentsViewModel.MomentsUserViewModel user){
        ItemHeaderBinding headerBinding = (ItemHeaderBinding) binding;
        headerBinding.setVm(user);
    }

    @Override
    protected void onBindItem(ViewDataBinding binding, MomentsViewModel.MomentsTweetViewModel tweet, RecyclerView.ViewHolder holder) {
        ItemTweetBinding tweetBinding = (ItemTweetBinding) binding;
        tweetBinding.setVm(tweet);
        if(tweet.hasImages.get()){
            tweetBinding.rvImages.setNestedScrollingEnabled(false);
            tweetBinding.rvImages.setHasFixedSize(true);
            tweetBinding.rvImages.setRecycledViewPool(imagesViewPool);
            if(tweetBinding.rvImages.getItemDecorationCount() == 0){
                tweetBinding.rvImages.addItemDecoration(imageDecoration);
            }
            ImageAdapter imageAdapter = new ImageAdapter(mContext);
            imageAdapter.setList(tweet.images.get());
            imageAdapter.notifyDataSetChanged();
            tweetBinding.rvImages.setAdapter(imageAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 1 : mList.size() + 1;
    }
}
