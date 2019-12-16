package com.centit.msgpusher.plugins;

import javafx.util.Pair;

/**
 * Created by codefan on 19-12-11.
 * 对百度推送进行封装
 */
public interface IBaiduPushSupport {
    String getAndroidPkg();
    String getAndroidView();
    Pair<String, String> getReceiverDeviceAndChannel(String receive);
}
