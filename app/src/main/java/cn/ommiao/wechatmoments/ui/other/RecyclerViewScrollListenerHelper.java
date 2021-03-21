package cn.ommiao.wechatmoments.ui.other;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.logger.Logger;

public class RecyclerViewScrollListenerHelper {

    private int totalDy;

    private boolean canScrollVerticalUp = false;
    private boolean canScrollVerticalDown = false;

    private boolean notifiedEnd;
    private boolean notifiedStart;

    private boolean canScrollVerticalUpOnFingerDown = true;

    private final RecyclerView rv;
    private final OnScrollListener listener;

    private RecyclerViewScrollListenerHelper(RecyclerView rv, OnScrollListener listener){
        this.rv = rv;
        this.listener = listener;
    }

    public interface OnScrollListener {

        void onScrollUpOnEnd();

        void onScrollDownOnStart(int dy);

        void onScrolled(int totalY);

        void onScrollDownOnStartRelease();

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

            private int initialTouchY = -1;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Logger.d("down");
                        initialTouchY = (int) (event.getY() + 0.5);
                        canScrollVerticalUpOnFingerDown = canScrollVerticalUp;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(initialTouchY == -1){
                            initialTouchY = (int) (event.getY() + 0.5);
                            canScrollVerticalUpOnFingerDown = canScrollVerticalUp;
                        }
                        int dy = (int) (event.getY() + 0.5) - initialTouchY;
                        if(dy > 0){
                            if(!canScrollVerticalUp){
                                if(canScrollVerticalUpOnFingerDown){
                                    initialTouchY = (int) (event.getY() + 0.5);
                                    canScrollVerticalUpOnFingerDown = false;
                                    dy = 0;
                                }
                                listener.onScrollDownOnStart(dy);
                                notifiedStart = true;
                                Logger.d("move, initialY: " + initialTouchY + ", stop1: " + dy);
                                return true;
                            }
                        } else if(dy < 0){
                            if(notifiedStart){
                                Logger.d("move, initialY: " + initialTouchY + ", stop2: " + dy);
                                return true;
                            }
                            if(!canScrollVerticalDown && !notifiedEnd){
                                listener.onScrollUpOnEnd();
                                notifiedEnd = true;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        initialTouchY = -1;
                        if(notifiedStart){
                            listener.onScrollDownOnStartRelease();
                        }
                        notifiedEnd = false;
                        notifiedStart = false;
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
