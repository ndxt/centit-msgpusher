package com.centit.msgpusher.client;

import com.alibaba.fastjson.JSON;
import com.centit.framework.appclient.HttpReceiveJSON;
import com.centit.msgpusher.client.po.PushResult;

/**
 * Created by codefan on 17-4-11.
 */
public class TestMsgPusher {

    private static MsgPusherClientImpl client;

    public static void init(){

        client = new MsgPusherClientImpl();
    }

    public static void main(String[] args) throws Exception {

        init();

        client.initAppSession("http://106.15.39.61:8080/message-delivery/service","u0000000","000000");

        /*register(client);*/

        pushMessage(client);

//        sendEmail();


    }

    public static void register(MsgPusherClientImpl client) throws Exception {
        String jsonStr = client.registerUser("001","osId", "deviceId", "deviceType", "channelId");
        HttpReceiveJSON resJson = HttpReceiveJSON.valueOfJson(jsonStr);
        System.out.print("result:"+ JSON.toJSONString(resJson));
    }

    public static void pushMessage(MsgPusherClientImpl client) throws Exception {
        PushResult jsonStr = client.pushAppMessage("001", "test", "testContent","df","zou_wy","centit.1");
        System.out.print("result:"+ JSON.toJSONString(jsonStr));
    }

}
