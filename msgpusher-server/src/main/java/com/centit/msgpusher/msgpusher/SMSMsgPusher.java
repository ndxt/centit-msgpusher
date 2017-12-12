package com.centit.msgpusher.msgpusher;

import com.centit.msgpusher.msgpusher.po.IPushMessage;
import com.centit.msgpusher.msgpusher.po.IPushMsgPoint;
import org.springframework.stereotype.Service;

/**
 * Created by codefan on 17-4-6.
 */
@Service("smsMsgPusher")
public class SMSMsgPusher implements MsgPusher {


    @Override
    public PushResult pushMessage(IPushMessage msg, IPushMsgPoint receiver) throws Exception {
        return null;
    }

    @Override
    public PushResult pushMsgToAll(IPushMessage msg) throws Exception {
        return null;
    }
}
