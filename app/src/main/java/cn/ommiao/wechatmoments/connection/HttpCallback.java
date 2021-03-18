package cn.ommiao.wechatmoments.connection;

public interface HttpCallback {

    void onSuccess(String result) throws Exception;

    void onFailure(String exception);

}
