package com.centit.msgpusher.po;

/**
 * create by scaffold 2017-04-10
 * @author codefan@sina.com

  用户消息接收端口（设备）描述。
*/

public class SimplePushMsgPoint implements IPushMsgPoint {

    private String userCode;

    public SimplePushMsgPoint(){

    }

    public SimplePushMsgPoint(String userCode){
        this.userCode = userCode;
    }

    @Override
    public String getOsId() {
        return "default";
    }


    @Override
    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @Override
    public String getUserName() {
        return null;
    }

    @Override
    public String getDeviceType() {
        return null;
    }

    @Override
    public String getEmailAddress() {
        return null;
    }

    @Override
    public String getChannelId() {
        return null;
    }

}
