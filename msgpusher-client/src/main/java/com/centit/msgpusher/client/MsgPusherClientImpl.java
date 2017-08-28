package com.centit.msgpusher.client;

import com.centit.framework.appclient.AppSession;
import com.centit.framework.common.ResponseJSON;
import com.centit.msgpusher.client.po.MessageDelivery;
import com.centit.msgpusher.client.po.PushResult;
import com.centit.msgpusher.client.po.UserMsgPoint;
import com.centit.msgpusher.client.po.UserNotifySetting;
import com.centit.support.network.HttpExecutor;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Created by codefan on 17-4-11.
 */
public class MsgPusherClientImpl implements MsgPusherClient {

    private AppSession appSession;

//    @Value("${pusherserver.url}")
//    private String appServerUrl;
//    @Value("${pusherserver.userCode}")
//    private String userCode;
//    @Value("${pusherserver.password}")
//    private String password;
//    @Value("${pusherserver.export.url}")
//    private String appServerExportUrl;

    public void initAppSession(String appServerUrl, String userCode,String password ){
        appSession = new AppSession(appServerUrl,false,userCode,password);
    }

    public CloseableHttpClient getHttpClient() throws Exception {
        return appSession.getHttpClient();
    }

    public void releaseHttpClient(CloseableHttpClient httpClient) {
        appSession.releaseHttpClient(httpClient);
    }

    public PushResult pushMessage(CloseableHttpClient httpClient, MessageDelivery msgdlvry) throws Exception {
        String jsonStr = HttpExecutor.jsonPost(httpClient, appSession.completeQueryUrl("/msgdlvry/push"), msgdlvry);
//        String jsonStr = HttpExecutor.formPost(httpClient, appSession.completeQueryUrl("/msgdlvry/push"), msgdlvry);
        return ResponseJSON.valueOfJson(jsonStr).getDataAsObject(PushResult.class);

    }

    public PushResult pushMessage(MessageDelivery msgdlvry) throws Exception {
        CloseableHttpClient httpClient = getHttpClient();
        PushResult result = pushMessage(httpClient, msgdlvry);
        releaseHttpClient(httpClient);
        return result;
    }

    public PushResult pushAppMessage(String userCode, String title, String message, String osId, String optId, String msgSender) throws Exception {
        PushResult jsonStr = pushMessage(userCode, title, message, "A", osId, optId, msgSender);
        return jsonStr;
    }

    public PushResult pushMessage(String userCode, String title, String message, String noticeTypes, String osId, String optId, String msgSender) throws Exception {

        MessageDelivery msgdlvry = new MessageDelivery();
        msgdlvry.setMsgReceiver(userCode);
        msgdlvry.setMsgSubject(title);
        msgdlvry.setMsgContent(message);
        msgdlvry.setOsId(osId);
        msgdlvry.setNoticeTypes(noticeTypes);
        msgdlvry.setOptId(optId);
        msgdlvry.setMsgSender(msgSender);
        PushResult jsonStr = pushMessage(msgdlvry);
        return jsonStr;
    }

    public PushResult pushMsgToAll(CloseableHttpClient httpClient, MessageDelivery msgdlvry) throws Exception {
        String jsonStr = HttpExecutor.jsonPost(httpClient, appSession.completeQueryUrl("/msgdlvry/pushall"), msgdlvry);
        /*String jsonStr = HttpExecutor.formPost(httpClient, appSession.completeQueryUrl("/msgdlvry/pushall"), msgdlvry);*/
        return ResponseJSON.valueOfJson(jsonStr).getDataAsObject(PushResult.class);
    }

    public PushResult pushMsgToAll(MessageDelivery msgdlvry) throws Exception {
        CloseableHttpClient httpClient = getHttpClient();
        PushResult jsonStr = pushMsgToAll(httpClient, msgdlvry);
        releaseHttpClient(httpClient);
        return jsonStr;
    }

    public PushResult pushAppMsgToAll(String title , String message, String osId, String optId, String msgSender) throws Exception {

        PushResult jsonStr = pushMsgToAll(title, message, "A", osId, optId, msgSender);
        return jsonStr;
    }

    public PushResult pushMsgToAll(String title , String message, String noticeTypes, String osId, String optId, String msgSender) throws Exception {
        MessageDelivery msgdlvry = new MessageDelivery();
        msgdlvry.setMsgSubject(title);
        msgdlvry.setMsgContent(message);
        msgdlvry.setNoticeTypes(noticeTypes);
        msgdlvry.setOptId(optId);
        msgdlvry.setOsId(osId);
        msgdlvry.setMsgSender(msgSender);
        PushResult jsonStr = pushMsgToAll(msgdlvry);
        return jsonStr;
    }


    public String registerUser(CloseableHttpClient httpClient, UserMsgPoint msgPoint) throws Exception {
        String jsonStr = HttpExecutor.jsonPost(httpClient, appSession.completeQueryUrl("/msgdlvry/register"), msgPoint);
        return jsonStr;
    }

    public String registerUser(UserMsgPoint msgPoint)throws Exception {
        CloseableHttpClient httpClient = getHttpClient();
        String jsonStr = registerUser(httpClient, msgPoint);
        releaseHttpClient(httpClient);
        return jsonStr;
    }

    public String registerUser(String userCode, String osId, String deviceId, String deviceType, String channelId) throws Exception{
        UserMsgPoint msgPoint = new UserMsgPoint(osId, userCode);
        msgPoint.setDeviceId(deviceId);
        msgPoint.setDeviceType(deviceType);
        msgPoint.setChannelId(channelId);
        String jsonStr = registerUser(msgPoint);
        return jsonStr;
    }

    public String registerUser(String userCode, String osId) throws Exception{
        UserMsgPoint msgPoint = new UserMsgPoint(osId, userCode);

        String jsonStr = registerUser(msgPoint);
        return jsonStr;
    }

    public String registerUserEmail(String userCode, String osId, String emailAddress) throws Exception{
        UserMsgPoint msgPoint = new UserMsgPoint(osId, userCode);
        msgPoint.setEmailAddress(emailAddress);
        return registerUser(msgPoint);
    }

    public String registerUserPhone(String userCode, String osId, String mobilePhone) throws Exception{
        UserMsgPoint msgPoint = new UserMsgPoint(osId, userCode);
        msgPoint.setMobilePhone(mobilePhone);
        return registerUser(msgPoint);
    }

    public String registerUserWeChat(String userCode, String osId, String wxToken) throws Exception{
        UserMsgPoint msgPoint = new UserMsgPoint(osId, userCode);
        msgPoint.setWxToken(wxToken);
        return registerUser(msgPoint);
    }

    public String userNotifySetting(CloseableHttpClient httpClient, UserNotifySetting notifySetting) throws Exception {
        String jsonStr = HttpExecutor.jsonPost(httpClient, appSession.completeQueryUrl("/msgdlvry/userNotifySetting"), notifySetting);
        return jsonStr;
    }

    public String userNotifySetting(UserNotifySetting notifySetting) throws Exception {
        CloseableHttpClient httpClient = getHttpClient();
        String jsonStr = userNotifySetting(httpClient, notifySetting);
        releaseHttpClient(httpClient);
        return jsonStr;
    }

    public String userNotifySetting(String userCode, String osId, String notityTypes) throws Exception{
        UserNotifySetting notifySetting = new UserNotifySetting();
        notifySetting.setUserCode(userCode);
        notifySetting.setOsId(osId);
        notifySetting.setNotifyTypes(notityTypes);
        return userNotifySetting(notifySetting);
    }
}
