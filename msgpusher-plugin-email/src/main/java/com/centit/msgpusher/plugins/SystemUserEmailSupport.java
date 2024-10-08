package com.centit.msgpusher.plugins;

import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.model.basedata.UserInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by codefan on 19-12-11.
 * 对百度推送进行封装
 */
public class SystemUserEmailSupport implements IUserEmailSupport{
    @Override
    public String getReceiverEmail(String topUnit, String receiver){
        if(receiver.indexOf('@')>0){
            return receiver;
        }
        UserInfo userinfo = CodeRepositoryUtil.getUserInfoByCode(topUnit, receiver);
        if(userinfo==null)
            return null;
        return userinfo.getRegEmail();
    }

    @Override
    public List<String> listAllUserEmail(String topUnit){
        List<UserInfo> allUsers = CodeRepositoryUtil.getAllUsers(
            topUnit,"A");
        List<String>  allEmails = new ArrayList<>();
        for (UserInfo allUser : allUsers) {
            allEmails.add(allUser.getRegEmail());
        }
        return allEmails;
    }
}
