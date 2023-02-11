package com.centit.msgpusher.plugins;

import java.util.List;

/**
 * Created by codefan on 19-12-11.
 * 对百度推送进行封装
 */
public interface IUserEmailSupport {
    String getReceiverEmail(String topUnit, String receiver);

    default List<String> listAllUserEmail(String topUnit){
        return null;
    }
}
