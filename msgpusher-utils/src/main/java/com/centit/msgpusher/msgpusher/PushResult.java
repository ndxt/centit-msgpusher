package com.centit.msgpusher.msgpusher;

import java.util.Map;

/**
 * Created by zhang_gd on 2017/4/18.
 */
public class PushResult {
    private String pushState;  //1:成功；3：部分成功；2：失败
    private String msgId;
    private String msgId2;
    private Map<String,String> map;  //错误信息和返回值

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public Map<String, String> getMap() {

        return map;
    }

    public String getPushState() {
        return pushState;
    }

    public void setPushState(String pushState) {
        this.pushState = pushState;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgId2() {
        return msgId2;
    }

    public void setMsgId2(String msgId2) {
        this.msgId2 = msgId2;
    }

}
