package cn.ommiao.wechatmoments.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiKai
 */
public abstract class BaseBindingAdapter<M, B extends ViewDataBinding> extends RecyclerView.Adapter {

    protected final Context mContext;

    protected List<M> mList = new ArrayList<>();

    public BaseBindingAdapter(Context context){
        this.mContext = context;
    }

    public void setList(List<M> list) {
        this.mList = list;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        B binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), this.getLayoutId(viewType), parent, false);
        return new BaseBindingViewHolder(binding.getRoot());
    }

    protected abstract @LayoutRes
    int getLayoutId(int viewType);

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        B binding = DataBindingUtil.getBinding(holder.itemView);
        this.onBindItem(binding, this.mList.get(position), holder);
        if(binding != null){
            binding.executePendingBindings();
        }
    }

    protected abstract void onBindItem(B binding, M item, RecyclerView.ViewHolder holder);

    public static class BaseBindingViewHolder extends RecyclerView.ViewHolder{
        BaseBindingViewHolder(View itemView){
            super(itemView);
        }
    }
}
