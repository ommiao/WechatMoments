package cn.ommiao.wechatmoments.connection;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import cn.ommiao.wechatmoments.util.ThreadPool;

public class HttpProxy {

    public static final String HTTP_RESPONSE_EMPTY = "result is empty!";

    public static void get(final String url, final HttpCallback callback) {
        ThreadPool.getHttpExecutor().execute(() -> {
            try {
                String result = getSync(url);
                if (callback != null) {
                    callback.onSuccess(result);
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onFailure(e.getMessage());
                }
            }
        });
    }

    public static String getSync(final String url) throws Exception {
        String result = HttpManager.sendGet(url);
        String exceptionMsg;
        if (!TextUtils.isEmpty(result)) {
            if (!HttpManager.REQUEST_FAILED.equals(result)) {
                try {
                    Integer.parseInt(result);
                    exceptionMsg = "response code is: " + result;
                } catch (Exception e) {
                    Logger.d("response from url: " + url);
                    Logger.d(result);
                    return result;
                }
            } else {
                exceptionMsg = "request failed!";
            }
        } else {
            exceptionMsg = HTTP_RESPONSE_EMPTY;
        }
        throw new RuntimeException(exceptionMsg);
    }

}
