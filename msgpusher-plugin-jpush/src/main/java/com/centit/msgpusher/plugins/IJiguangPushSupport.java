package com.centit.msgpusher.plugins;

/**
 * Created by codefan on 19-12-11.
 * 对百度推送进行封装
 */
public interface IJiguangPushSupport {
    default String getReceiverAlias(String receiver){
        return receiver;
    }

    default String [] getReceiversAlias(String [] receivers){
        return receivers;
    }
}
