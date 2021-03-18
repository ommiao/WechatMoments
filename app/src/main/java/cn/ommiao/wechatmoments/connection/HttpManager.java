package cn.ommiao.wechatmoments.connection;

import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpManager {

    private static final int CONNECTION_TIMEOUT = 120 * 60 * 1000;
    public static final String REQUEST_FAILED = "-1";

    public static String sendGet(String requestUrl) {
        try {
            Logger.i("get url: " + requestUrl);
            //build a connection and connect
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(CONNECTION_TIMEOUT);
            connection.connect();

            //get response code
            int responseCode = connection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                StringBuffer buffer = new StringBuffer();
                String readLine;
                BufferedReader responseReader;
                responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((readLine = responseReader.readLine()) != null) {
                    buffer.append(readLine);
                }
                responseReader.close();
                return buffer.toString();
            } else {
                return String.valueOf(responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return REQUEST_FAILED;
    }

}
