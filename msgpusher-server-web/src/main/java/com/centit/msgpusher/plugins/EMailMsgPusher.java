package com.centit.msgpusher.plugins;

import com.centit.msgpusher.commons.MsgPusher;
import com.centit.msgpusher.commons.PushResult;
import com.centit.msgpusher.po.IPushMessage;
import com.centit.msgpusher.po.IPushMsgPoint;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by codefan on 17-4-6.
 */
@Service("emailMsgPusher")
public class EMailMsgPusher implements MsgPusher {

    @Value("${EmailServerHost}")
    public String  emailServerHost;
    @Value("${EmailServerHostUser}")
    public String  emailServerHostUser;
    @Value("${EmailServerHostPwd}")
    public String  emailServerHostPwd;

    @Override
    public PushResult pushMessage(IPushMessage msg, IPushMsgPoint receiver) throws Exception {
        PushResult pushResult = new PushResult();
        Map<String,String> emailMap =new HashMap<>();
        String result = "OK";
        String state = null;
        String EMailAddress = receiver.getEmailAddress();
        if (EMailAddress == null || "".equals(EMailAddress)){
            result = "该用户没有设置注册邮箱";
            state = "2";
            pushResult.setPushState(state);
            emailMap.put("eMail",result);
            pushResult.setMap(emailMap);
            return pushResult;
        }
        MultiPartEmail multMail = new MultiPartEmail();
        // SMTP
        multMail.setHostName(emailServerHost);
        // 需要提供公用的消息用户名和密码
        multMail.setAuthentication(emailServerHostUser,emailServerHostPwd);
        try {
//            multMail.setFrom(msg.getMsgSender());
            multMail.setFrom(emailServerHostUser); //管理邮箱
            multMail.addTo(EMailAddress);
            multMail.setSubject(msg.getMsgSubject());
            multMail.setMsg(msg.getMsgContent());
            multMail.send();
            state = "0";
            result = "OK";
            emailMap.put("eMail",result);
        } catch (EmailException e) {
            result=e.getMessage();
            state = "2";
            emailMap.put("eMail",result);
            e.printStackTrace();
        }
        pushResult.setPushState(state);
        pushResult.setMap(emailMap);
//        pushResult.setEMailRet(result);
        return pushResult;
    }

    @Override
    public PushResult pushMsgToAll(IPushMessage msg) throws Exception {
        PushResult pushResult = new PushResult();
        Map<String,String> emailMap =new HashMap<>();
        List<String> receiversList = new ArrayList<>();
        String result = "OK";
        String state = null;
        int successNo = 0;
        int failNo = 0;
        int totalNo = receiversList.size();

        MultiPartEmail multMail = new MultiPartEmail();
        // SMTP
        multMail.setHostName(emailServerHost);
        // 需要提供公用的消息用户名和密码
        multMail.setAuthentication(emailServerHostUser,emailServerHostPwd);

        for (String EMailAddress:receiversList){
            if (EMailAddress == null || "".equals(EMailAddress)){
                result = "该用户没有设置注册邮箱";
                state = "2";
                pushResult.setPushState(state);
                emailMap.put("eMail",result);
            }else{
                try {
                    multMail.setFrom(emailServerHostUser); //管理邮箱
                    multMail.addTo(EMailAddress);
                    multMail.setSubject(msg.getMsgSubject());
                    multMail.setMsg(msg.getMsgContent());
                    multMail.send();
                    successNo++;
                    state = "1";
//                    result = "OK";
//                    emailMap.put("eMail",result);
                } catch (EmailException e) {
                    result=e.getMessage();
                    failNo++;
                    state = "2";
//                    emailMap.put("eMail",result);
//                    e.printStackTrace();
                }
            }

        }
        emailMap.put("eMail","total:"+totalNo+";OK:"+successNo+"Fail:"+failNo);
        pushResult.setPushState(state);
        pushResult.setMap(emailMap);
//        pushResult.setEMailRet(result);
        return null;
    }
}

