package cn.ommiao.wechatmoments.log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.PrettyFormatStrategy;

public class SimpleLogger {
    public static void initLogger(){
        PrettyFormatStrategy strategy =
                PrettyFormatStrategy
                .newBuilder()
                .tag("WECHAT_MOMENTS")
                .build();
        com.orhanobut.logger.Logger.addLogAdapter(new AndroidLogAdapter(strategy) {});
    }
}
