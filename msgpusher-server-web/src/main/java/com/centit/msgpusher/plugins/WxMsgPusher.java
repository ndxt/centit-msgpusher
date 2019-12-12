package com.centit.msgpusher.plugins;

import com.centit.msgpusher.commons.MsgPusher;
import com.centit.msgpusher.commons.PushResult;
import com.centit.msgpusher.po.IPushMessage;
import com.centit.msgpusher.po.IPushMsgPoint;
import org.springframework.stereotype.Service;

/**
 * Created by codefan on 17-4-6.
 */
@Service("wxMsgPusher")
public class WxMsgPusher implements MsgPusher {


    @Override
    public PushResult pushMessage(IPushMessage msg, IPushMsgPoint receiver) throws Exception {
        return null;
    }

    @Override
    public PushResult pushMsgToAll(IPushMessage msg) throws Exception {
        return null;
    }
}
