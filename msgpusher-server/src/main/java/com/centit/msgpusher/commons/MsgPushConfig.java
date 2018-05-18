package com.centit.msgpusher.commons;

import java.util.List;

/**
 * Created by zhang_gd on 2017/4/21.
 */
public interface MsgPushConfig {

    void reloadConfig();

    OSMsgPushInfo getOSConfig(String osId);

    List<OSMsgPushInfo> getOsConfig();

}
