package com.centit.msgpusher.plugins;

import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.model.basedata.IUserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codefan on 19-12-11.
 * 对百度推送进行封装
 */
public class SystemUserEmailSupport implements IUserEmailSupport{
    @Override
    public String getReceiverEmail(String topUnit, String receiver){
        IUserInfo userinfo = CodeRepositoryUtil.getUserInfoByCode(topUnit, receiver);
        return userinfo.getRegEmail();
    }

    @Override
    public List<String> listAllUserEmail(String topUnit){
        List<IUserInfo> allUsers = CodeRepositoryUtil.getAllUsers(
            topUnit,"A");
        List<String>  allEmails = new ArrayList<>();
        for (IUserInfo allUser : allUsers) {
            allEmails.add(allUser.getRegEmail());
        }
        return allEmails;
    }
}
