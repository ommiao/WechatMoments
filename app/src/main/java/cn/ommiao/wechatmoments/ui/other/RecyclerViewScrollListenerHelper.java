package cn.ommiao.wechatmoments.ui.other;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewScrollListenerHelper {

    private int totalDy;

    private boolean canScrollVerticalUp = true;
    private boolean canScrollVerticalDown = true;

    private boolean notifiedEnd;
    private boolean notifiedStart;

    private final RecyclerView rv;
    private final OnScrollListener listener;

    private RecyclerViewScrollListenerHelper(RecyclerView rv, OnScrollListener listener){
        this.rv = rv;
        this.listener = listener;
    }

    public interface OnScrollListener {

        void onScrollUpOnEnd();

        void onScrollDownOnStart();

        void onScrolled(int totalY);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void listenScroll(){
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalDy += dy;
                canScrollVerticalDown = rv.canScrollVertically(1);
                canScrollVerticalUp = rv.canScrollVertically(-1);
                listener.onScrolled(totalDy);
            }
        });

        rv.setOnTouchListener(new View.OnTouchListener() {

            private int initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        initialTouchY = (int) (event.getY() + 0.5);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dy = (int) (event.getY() + 0.5) - initialTouchY;
                        if(dy > 5){
                            if(!canScrollVerticalUp){
                                listener.onScrollDownOnStart();
                            }
                        } else if(dy < -5){
                            if(!canScrollVerticalDown && !notifiedEnd){
                                listener.onScrollUpOnEnd();
                                notifiedEnd = true;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        notifiedEnd = false;
                        break;
                }
                return false;
            }
        });
    }

    public static void bindListener(RecyclerView rv, OnScrollListener listener){

        RecyclerViewScrollListenerHelper helper = new RecyclerViewScrollListenerHelper(rv, listener);
        helper.listenScroll();

    }

}
