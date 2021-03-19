package cn.ommiao.wechatmoments.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.ommiao.wechatmoments.R;

public class RoundCornersFrameLayout extends FrameLayout {

    private final Paint mPaint;

    private static NinePatch mNinePatch;

    public RoundCornersFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public RoundCornersFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornersFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        if(mNinePatch == null){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.shape_corners);
            mNinePatch = new NinePatch(bitmap, bitmap.getNinePatchChunk(), null);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Rect bounds = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.saveLayer(new RectF(0, 0, bounds.right, bounds.bottom), null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        mNinePatch.draw(canvas, bounds, mPaint);
        canvas.restore();
    }
}
