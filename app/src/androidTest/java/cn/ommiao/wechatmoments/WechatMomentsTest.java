package cn.ommiao.wechatmoments;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.Direction;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObject2Condition;
import androidx.test.uiautomator.Until;

import com.orhanobut.logger.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class WechatMomentsTest {

    @Test
    public void run() throws InterruptedException {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("cn.ommiao.wechatmoments", appContext.getPackageName());

        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        //launch Wechat Moments
        try {
            uiDevice.executeShellCommand("am start -n cn.ommiao.wechatmoments/.ui.activity.LauncherActivity");
        } catch (IOException e) {
            e.printStackTrace();
            shortToast(appContext, "Can not start Wechat Moments");
        }

        boolean tweetsLoaded = false;

        int retryCount = 3;
        while (retryCount > 0){
            boolean loaded = uiDevice.wait(Until.gone(By.text("Loading data from server…")), 120 * 1000);
            if(loaded){
                UiObject2 uiObject2Retry = uiDevice.findObject(By.text("RETRY"));
                if(uiObject2Retry != null){
                    uiObject2Retry.click();
                    Thread.sleep(200);
                    retryCount -= 1;
                } else {
                    tweetsLoaded = true;
                    break;
                }
            }
        }

        if(!tweetsLoaded){
            shortToast(appContext, "Can not load tweets, test failed!");
        }


        UiObject2 uiObject2TweetsList = uiDevice.findObject(By.res("cn.ommiao.wechatmoments:id/rv"));

        uiObject2TweetsList.setGestureMargin(200);

        for (int i = 0; i < 5; i++) {
            uiObject2TweetsList.scroll(Direction.DOWN, 0.8f, 1000);
            Thread.sleep(1000);
        }

        for (int i = 0; i < 6; i++) {
            uiObject2TweetsList.scroll(Direction.UP, 0.8f, 1000);
            Thread.sleep(1000);
        }

        Thread.sleep(5000);

        for (int i = 0; i < 3; i++) {
            uiObject2TweetsList.scroll(Direction.DOWN, 0.8f, 1000);
            Thread.sleep(1000);
        }

    }

    private void shortToast(Context context, String msg){
        Looper.prepare();
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

}