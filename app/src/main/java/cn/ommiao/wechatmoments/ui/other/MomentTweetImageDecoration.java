package cn.ommiao.wechatmoments.ui.other;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cn.ommiao.wechatmoments.R;

/**
 * @author LiKai
 */
public class MomentTweetImageDecoration extends RecyclerView.ItemDecoration {

    private final int spanCount;

    private final int marginHorizontalPerItem;

    private final int marginVertical;

    public MomentTweetImageDecoration(Context context) {
        int marginUnit = context.getResources().getDimensionPixelOffset(R.dimen.margin_tweet_image);
        spanCount = context.getResources().getInteger(R.integer.tweet_images_span_count);
        marginHorizontalPerItem = (int) ((spanCount - 1) * marginUnit / (float)spanCount);
        marginVertical = marginUnit;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        int left, top = 0, right, bottom = 0;

        //compute horizontal margin
        if(position % spanCount == 0){
            //first span
            left = 0;
            right = marginHorizontalPerItem;
        } else if(position % spanCount == spanCount - 1){
            //last span
            left = marginHorizontalPerItem;
            right = 0;
        } else {
            //middle spans
            left = right = marginHorizontalPerItem / 2;
        }

        //compute vertical margin
        if(position >= spanCount){
            top = marginVertical;
        }

        outRect.set(left, top, right, bottom);
    }
}
