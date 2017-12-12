package com.centit.msgpusher.msgpusher;

import com.centit.msgpusher.msgpusher.po.IPushMessage;
import com.centit.msgpusher.msgpusher.po.IPushMsgPoint;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by codefan on 17-4-10.
 */
@Service("appMsgPusher")
public class AppMsgPusher implements MsgPusher {

    @Resource(name="androidMsgPusher")
    private MsgPusher androidPusher;

    @Resource(name="iosMsgPusher")
    private MsgPusher iosPusher;

    @Resource(name="socketMsgPusher")
    private MsgPusher socketPusher;

    @Override
    public PushResult pushMessage(IPushMessage msg, IPushMsgPoint receiver) throws Exception {
        PushResult pushResult = new PushResult();
        Map<String, String> appMap = new HashMap<>();
        String deviceType = receiver.getDeviceType();
        PushResult pcPushResult = socketPusher.pushMessage(msg, receiver);
        if ("3".equals(deviceType)) {           //Android设备推送
            pushResult = androidPusher.pushMessage(msg, receiver);
        } else if ("4".equals(deviceType)) {     //IOS设备推送
            pushResult = iosPusher.pushMessage(msg, receiver);
        }
        if (pcPushResult != null) {
            appMap.put("pc", pcPushResult.getMap().get("pc"));
        }
        if (pushResult != null) {
            if (pushResult.getMap().get("app") != null) {
                appMap.put("app", pushResult.getMap().get("app"));
            }
        }
        if (!pcPushResult.getPushState().equals(pushResult.getPushState())){
            pushResult.setPushState("3");//推送部分成功
        }
        pushResult.setMap(appMap);
        return pushResult;
    }

    @Override
    public PushResult pushMsgToAll(IPushMessage msg) throws Exception {
        PushResult pushResult = new PushResult();
        PushResult androidPushResult = androidPusher.pushMsgToAll(msg);
        PushResult iosPushResult = iosPusher.pushMsgToAll(msg);
        PushResult pcPushResult = socketPusher.pushMsgToAll(msg);
        String androidPushState = null;
        String iosPushState = null;
        String pcPushState = null;
        Map<String,String> appMap = new HashMap<>();
        //解析app推送结果
        if (androidPushResult != null){
            androidPushState = androidPushResult.getPushState();
            pushResult.setMsgId(androidPushResult.getMsgId());
            if (StringUtils.isBlank(androidPushResult.getMsgId())){
                appMap.put("android_error",appMap.get("android_error"));
            }
        }
        if (iosPushResult != null){
            iosPushState = iosPushResult.getPushState();
            pushResult.setMsgId2(iosPushResult.getMsgId2());
            if (StringUtils.isBlank(iosPushResult.getMsgId2())){
                appMap.put("ios_error",appMap.get("ios_error"));
            }
        }
        if (pcPushResult != null){
            pcPushState = pcPushResult.getPushState();
            appMap.put("pc",pcPushResult.getMap().get("pc"));
        }

        if (androidPushState.equals(iosPushState) && androidPushState.equals(pcPushState)){
            pushResult.setPushState(androidPushState);
        }else {
            pushResult.setPushState("3");//推送部分成功
        }
        pushResult.setMap(appMap);
        return pushResult;
    }

}
