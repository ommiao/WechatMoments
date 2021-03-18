package cn.ommiao.wechatmoments.ui.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.util.ObjectsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FitSystemInsetsFrameLayout extends FrameLayout {

    private androidx.core.view.OnApplyWindowInsetsListener mApplyWindowInsetsListener;

    public FitSystemInsetsFrameLayout(@NonNull Context context) {
        super(context);
    }

    public FitSystemInsetsFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FitSystemInsetsFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupForInsets();
    }

    private void setupForInsets() {
        if (Build.VERSION.SDK_INT < 21) {
            return;
        }

        if (ViewCompat.getFitsSystemWindows(this)) {
            if (mApplyWindowInsetsListener == null) {
                mApplyWindowInsetsListener =
                        new androidx.core.view.OnApplyWindowInsetsListener() {
                            @Override
                            public WindowInsetsCompat onApplyWindowInsets(View v,
                                                                          WindowInsetsCompat insets) {
                                return setWindowInsets(insets);
                            }
                        };
            }
            // First apply the insets listener
            ViewCompat.setOnApplyWindowInsetsListener(this, mApplyWindowInsetsListener);

            // Now set the sys ui flags to enable us to lay out in the window insets
            setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            ViewCompat.setOnApplyWindowInsetsListener(this, null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    final WindowInsetsCompat setWindowInsets(WindowInsetsCompat insets) {
        // Now dispatch to the Behaviors
        insets = dispatchApplyWindowInsetsToBehaviors(insets);
        requestLayout();
        return insets;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private WindowInsetsCompat dispatchApplyWindowInsetsToBehaviors(WindowInsetsCompat insets) {
        if (insets.isConsumed()) {
            return insets;
        }

        for (int i = 0, z = getChildCount(); i < z; i++) {
            final View child = getChildAt(i);
            if (ViewCompat.getFitsSystemWindows(child)) {
                child.onApplyWindowInsets(insets.toWindowInsets());
            }
        }

        return insets;
    }

    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        return super.onApplyWindowInsets(insets);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).dispatchApplyWindowInsets(insets);
        }
        return insets;
    }
}
