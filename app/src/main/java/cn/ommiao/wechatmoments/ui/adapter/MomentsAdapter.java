package cn.ommiao.wechatmoments.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import cn.ommiao.wechatmoments.R;
import cn.ommiao.wechatmoments.bridge.MomentsViewModel;
import cn.ommiao.wechatmoments.databinding.ItemHeaderBinding;
import cn.ommiao.wechatmoments.entity.Tweet;

public class MomentsAdapter extends BaseBindingAdapter<Tweet, ViewDataBinding> {

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_TWEET = 0;

    private MomentsViewModel.MomentsUserViewModel user;

    public MomentsAdapter(Context context) {
        super(context);
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
    protected void onBindItem(ViewDataBinding binding, Tweet item, RecyclerView.ViewHolder holder) {

    }

    @Override
    public int getItemCount() {
        return mList == null ? 1 : mList.size() + 1;
    }
}
