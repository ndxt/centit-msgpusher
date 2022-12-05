package com.centit.msgpusher.plugins;

import com.centit.framework.common.ResponseData;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.framework.model.basedata.NoticeMessage;

public class TioMsgPusher implements MessageSender {

    @Override
    public ResponseData sendMessage(String sender, String receiver, NoticeMessage message) {
        //TODO 添加谭聊推送接口
        return null;
    }
}
