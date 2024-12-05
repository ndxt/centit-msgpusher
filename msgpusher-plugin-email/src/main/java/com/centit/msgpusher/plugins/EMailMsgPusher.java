package com.centit.msgpusher.plugins;

import com.centit.framework.common.GlobalConstValue;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.support.common.DoubleAspect;
import com.centit.support.common.ObjectException;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

/**
 * Created by codefan on 17-4-6.
 */
public class EMailMsgPusher implements MessageSender {
    private static final Logger logger = LoggerFactory.getLogger(EMailMsgPusher.class);

    @Setter
    public String  emailServerHost;
    @Setter
    private int  emailServerPort;
    @Setter
    private String  emailServerUser;
    @Setter
    private String  emailServerPwd;
    @Setter
    private String  topUnit;
    @Setter
    private IUserEmailSupport userEmailSupport;

    public EMailMsgPusher(){
        emailServerPort = 25;
        topUnit = GlobalConstValue.NO_TENANT_TOP_UNIT;
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
        String currentTopUnit = StringUtils.isBlank(message.getTopUnit())? topUnit : message.getTopUnit();
        String receiverEmail = userEmailSupport.getReceiverEmail(currentTopUnit, receiver);
        String sendEmail = userEmailSupport.getReceiverEmail(currentTopUnit, sender);
        if(StringUtils.isBlank(sendEmail)){
            sendEmail = emailServerUser;
        }

        if (StringUtils.isBlank(receiverEmail)){
            return ResponseData.makeErrorMessage(2, "该用户没有设置注册邮箱");
        }

        try {
            sendEmail(new String[]{receiverEmail}, sendEmail,
                message.getMsgSubject(), message.getMsgContent(), null);
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
        String currentTopUnit = StringUtils.isBlank(message.getTopUnit())? topUnit : message.getTopUnit();

        List<String> receiversList = userEmailSupport.listAllUserEmail(currentTopUnit);
        String sendEmail = userEmailSupport.getReceiverEmail(currentTopUnit, sender);
        if(StringUtils.isBlank(sendEmail)){
            sendEmail = emailServerUser;
        }

        if(receiversList==null || receiversList.isEmpty()){
            return ResponseData.makeErrorMessage(ObjectException.DATA_NOT_FOUND_EXCEPTION,
                "没有找到对应的用户，可能时没有正确的配置用户信息支持接口 IUserEmailSupport 。");
        }

        try {
            sendEmail(receiversList.toArray(new String[receiversList.size()]), sendEmail,
                message.getMsgSubject(), message.getMsgContent(), null);
            return ResponseData.successResponse;
        } catch (EmailException e) {
            //e.printStackTrace();
            logger.error(e.getMessage(), e);
            return ResponseData.makeErrorMessage(2, e.getMessage());
        }

    }

    public void sendEmail(String[] mailTo, String mailFrom,
                          String msgSubject, String msgContent, List<File> annexs)
        throws EmailException {
        MultiPartEmail multMail = new MultiPartEmail();
        // SMTP
        multMail.setHostName(emailServerHost);
        multMail.setSmtpPort(emailServerPort);
        // 需要提供公用的邮件用户名和密码
        multMail.setAuthentication(emailServerUser, emailServerPwd);
        //multMail.setFrom(CodeRepositoryUtil.getRight("SysMail", "admin_email"));
        multMail.setFrom(mailFrom);
        multMail.addTo(mailTo);
        multMail.setCharset("utf-8");
        multMail.setSubject(msgSubject);
        msgContent = msgContent.trim();
        if(msgContent.endsWith("</html>") || msgContent.endsWith("</HTML>")){
            multMail.addPart(msgContent, "text/html;charset=utf-8");
        }else{
            multMail.setContent(msgContent, "text/plain;charset=gb2312");
        }
        if(annexs!=null) {
            for (File attachment : annexs) {
                multMail.attach(attachment);
            }
        }
        multMail.send();
    }
}

