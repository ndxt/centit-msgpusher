package com.centit.msgpusher.plugins;

import com.centit.framework.common.GlobalConstValue;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.model.basedata.IUserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codefan on 19-12-11.
 * 对百度推送进行封装
 */
public interface IUserEmailSupport {
    String getReceiverEmail(String receiver);

    default List<String> listAllUserEmail(){
        List<IUserInfo> allUsers = CodeRepositoryUtil.getAllUsers(
            GlobalConstValue.NO_TENANT_TOP_UNIT,"A");
        List<String>  allEmails = new ArrayList<>();
        for (IUserInfo allUser : allUsers) {
            allEmails.add(allUser.getRegEmail());
        }
        return allEmails;
    }
}
