package com.centit.msgpusher.plugins;

import com.centit.framework.common.ResponseData;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.support.common.DoubleAspect;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by codefan on 17-4-6.
 */
public abstract class EMailMsgPusher implements MessageSender {

    @Setter
    public String  emailServerHost;
    @Setter
    public String  emailServerHostUser;
    @Setter
    public String  emailServerHostPwd;

    private static final Logger logger = LoggerFactory.getLogger(EMailMsgPusher.class);

    protected abstract String getReceiverEmail(String receiver);
    protected List<String> listAllUserEmail(){
        return null;
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
        String receiverEmail = getReceiverEmail(receiver);
        if (receiverEmail == null || "".equals(receiverEmail)){
            return ResponseData.makeErrorMessage(2, "该用户没有设置注册邮箱");
        }
        MultiPartEmail multMail = new MultiPartEmail();
        // SMTP
        multMail.setHostName(emailServerHost);
        // 需要提供公用的消息用户名和密码
        multMail.setAuthentication(emailServerHostUser,emailServerHostPwd);
        try {
//            multMail.setFrom(msg.getMsgSender());
            multMail.setFrom(emailServerHostUser); //管理邮箱
            multMail.addTo(receiverEmail);
            multMail.setSubject(message.getMsgSubject());
            multMail.setMsg(message.getMsgContent());
            multMail.send();
            return ResponseData.successResponse;
        } catch (EmailException e) {
            //e.printStackTrace();
            logger.error(e.getMessage(), e);
            return ResponseData.makeErrorMessage(2, e.getMessage());
        }
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

        /*if(DoubleAspect.NONE.sameAspect(userInline)){
            return ResponseData.successResponse;
        }*/

        List<String> receiversList = listAllUserEmail();
        if(receiversList==null || receiversList.isEmpty()){
            return ResponseData.successResponse;
        }
        int successNo = 0;
        int failNo = 0;
        int totalNo = receiversList.size();

        MultiPartEmail multMail = new MultiPartEmail();
        // SMTP
        multMail.setHostName(emailServerHost);
        // 需要提供公用的消息用户名和密码
        multMail.setAuthentication(emailServerHostUser, emailServerHostPwd);

        for (String email: receiversList){
            if (StringUtils.isNotBlank(email)) {
                try {
                    multMail.setFrom(emailServerHostUser); //管理邮箱
                    multMail.addTo(email);
                    multMail.setSubject(message.getMsgSubject());
                    multMail.setMsg(message.getMsgContent());
                    multMail.send();
                    successNo++;
                } catch (EmailException e) {
                    failNo ++;
                    logger.error(e.getMessage(), e);
                }
            }

        }
        return ResponseData.makeErrorMessage(failNo==0?0:(successNo==0?1:2),
           "total:"+totalNo+";success:"+successNo+"Fail:"+failNo);
    }

}

