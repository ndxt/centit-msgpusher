package com.centit.msgpusher.client;

import com.centit.framework.appclient.AppSession;
import com.centit.framework.common.ResponseData;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.msgpusher.client.po.UserMsgPoint;
import com.centit.support.common.DoubleAspect;
import com.centit.support.network.HttpExecutor;
import com.centit.support.network.HttpExecutorContext;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.Collection;

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

    /**
     * 发送内部系统消息
     *
     * @param sender   发送人内部用户编码
     * @param receiver 接收人内部用户编码
     * @param message  消息主体
     * @return "OK" 表示成功，其他的为错误信息
     */
    @Override
    public ResponseData sendMessage(String sender, String receiver, NoticeMessage message) {
        return null;
    }

    /**
     * 批量发送内部系统消息
     *
     * @param sender    发送人内部用户编码
     * @param receivers 接收人内部用户编码
     * @param message   消息主体
     * @return "OK" 表示成功，其他的为错误信息
     */
    @Override
    public ResponseData sendMessage(String sender, Collection<String> receivers, NoticeMessage message) {
        return null;
    }

    /**
     * 广播信息
     *
     * @param sender     发送人内部用户编码
     * @param message    消息主体
     * @param userInline DoubleAspec.ON 在线用户  OFF 离线用户 BOTH 所有用户
     * @return 默认没有实现
     */
    @Override
    public ResponseData broadcastMessage(String sender, NoticeMessage message, DoubleAspect userInline) {
        return null;
    }
}
