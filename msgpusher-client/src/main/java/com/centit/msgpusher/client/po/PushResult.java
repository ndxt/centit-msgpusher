package com.centit.msgpusher.client.po;

import java.util.Map;

/**
 * Created by zhang_gd on 2017/4/18.
 */
public class PushResult {
    private String pushState;  //0:成功；1：部分成功；2：失败
    private String msgId;
    private String msgId2;
    private String errorDesc;
    private String errorDesc2;
    private String eMailRet;
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

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getErrorDesc2() {
        return errorDesc2;
    }

    public void setErrorDesc2(String errorDesc2) {
        this.errorDesc2 = errorDesc2;
    }

    public String getEMailRet() {
        return eMailRet;
    }

    public void setEMailRet(String eMailRet) {
        this.eMailRet = eMailRet;
    }
}
