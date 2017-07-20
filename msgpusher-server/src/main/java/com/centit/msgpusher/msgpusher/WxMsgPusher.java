package com.centit.msgpusher.msgpusher;

import com.centit.msgpusher.po.MessageDelivery;
import com.centit.msgpusher.po.UserMsgPoint;
import org.springframework.stereotype.Service;

/**
 * Created by codefan on 17-4-6.
 */
@Service("wxMsgPusher")
public class WxMsgPusher implements MsgPusher {


    @Override
    public PushResult pushMessage(MessageDelivery msg, UserMsgPoint receiver) throws Exception {
        return null;
    }

    @Override
    public PushResult pushMsgToAll(MessageDelivery msg) throws Exception {
        return null;
    }
}
