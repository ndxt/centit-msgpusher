package com.centit.msgpusher.client;

import com.centit.framework.appclient.AppSession;
import com.centit.framework.appclient.HttpReceiveJSON;
import com.centit.framework.common.ResponseData;
import com.centit.msgpusher.client.po.MessageDelivery;
import com.centit.msgpusher.client.po.UserMsgPoint;
import com.centit.msgpusher.client.po.UserNotifySetting;
import com.centit.support.network.HttpExecutor;
import com.centit.support.network.HttpExecutorContext;
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
        return appSession.allocHttpClient();
    }

    public void releaseHttpClient(CloseableHttpClient httpClient) {
        appSession.releaseHttpClient(httpClient);
    }

    public ResponseData pushMessage(CloseableHttpClient httpClient, MessageDelivery msgdlvry) throws Exception {
        String jsonStr = HttpExecutor.jsonPost(HttpExecutorContext.create(httpClient), appSession.completeQueryUrl("/msgdlvry/push"), msgdlvry);
//        String jsonStr = HttpExecutor.formPost(httpClient, appSession.completeQueryUrl("/msgdlvry/push"), msgdlvry);
        return HttpReceiveJSON.valueOfJson(jsonStr).toResponseData();

    }

    public ResponseData pushMessage(MessageDelivery msgdlvry) throws Exception {
        CloseableHttpClient httpClient = getHttpClient();
        ResponseData result = pushMessage(httpClient, msgdlvry);
        releaseHttpClient(httpClient);
        return result;
    }

    public ResponseData pushAppMessage(String userCode, String title, String message, String osId, String optId, String msgSender) throws Exception {
        return pushMessage(userCode, title, message, "A", osId, optId, msgSender);
    }

    public ResponseData pushMessage(String userCode, String title, String message, String noticeTypes, String osId, String optId, String msgSender) throws Exception {
        MessageDelivery msgdlvry = new MessageDelivery();
        msgdlvry.setMsgReceiver(userCode);
        msgdlvry.setMsgSubject(title);
        msgdlvry.setMsgContent(message);
        msgdlvry.setOsId(osId);
        msgdlvry.setNoticeTypes(noticeTypes);
        msgdlvry.setOptId(optId);
        msgdlvry.setMsgSender(msgSender);
        return pushMessage(msgdlvry);
    }

    public ResponseData pushMsgToAll(CloseableHttpClient httpClient, MessageDelivery msgdlvry) throws Exception {
        String jsonStr = HttpExecutor.jsonPost(
            HttpExecutorContext.create(httpClient),
            appSession.completeQueryUrl("/msgdlvry/pushall"), msgdlvry);
        /*String jsonStr = HttpExecutor.formPost(httpClient, appSession.completeQueryUrl("/msgdlvry/pushall"), msgdlvry);*/
        return HttpReceiveJSON.valueOfJson(jsonStr).getDataAsObject(ResponseData.class);
    }

    public ResponseData pushMsgToAll(MessageDelivery msgdlvry) throws Exception {
        CloseableHttpClient httpClient = getHttpClient();
        ResponseData jsonStr = pushMsgToAll(httpClient, msgdlvry);
        releaseHttpClient(httpClient);
        return jsonStr;
    }

    public ResponseData pushAppMsgToAll(String title , String message, String osId, String optId, String msgSender) throws Exception {
        return pushMsgToAll(title, message, "A", osId, optId, msgSender);
    }

    public ResponseData pushMsgToAll(String title , String message, String noticeTypes, String osId, String optId, String msgSender) throws Exception {
        MessageDelivery msgdlvry = new MessageDelivery();
        msgdlvry.setMsgSubject(title);
        msgdlvry.setMsgContent(message);
        msgdlvry.setNoticeTypes(noticeTypes);
        msgdlvry.setOptId(optId);
        msgdlvry.setOsId(osId);
        msgdlvry.setMsgSender(msgSender);
        return pushMsgToAll(msgdlvry);
    }


    public String registerUser(CloseableHttpClient httpClient, UserMsgPoint msgPoint) throws Exception {
        return HttpExecutor.jsonPost(HttpExecutorContext.create(httpClient),
            appSession.completeQueryUrl("/msgdlvry/register"), msgPoint);
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
        return registerUser(msgPoint);
    }

    public String registerUser(String userCode, String osId) throws Exception{
        UserMsgPoint msgPoint = new UserMsgPoint(osId, userCode);
        return registerUser(msgPoint);
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
        return HttpExecutor.jsonPost(HttpExecutorContext.create(httpClient), appSession.completeQueryUrl("/notifysetting"), notifySetting);
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
