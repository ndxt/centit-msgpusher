package com.centit.msgpusher.controller;

import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.msgpusher.po.MessageDelivery;
import com.centit.msgpusher.service.MessageDeliveryManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * IPushMessage  Controller.
 * create by scaffold 2017-04-07
 * @author codefan@sina.com
 * 消息推送null
*/


@Controller
@RequestMapping("/testMsgPush")
public class TestMsgPushController extends BaseController {
    private static final Log log = LogFactory.getLog(TestMsgPushController.class);

    @Resource
    private MessageDeliveryManager messageDeliveryManager;

    @RequestMapping(value = "/pushmsg/{userCode}", method = RequestMethod.GET)
    public void testPushMessage(@PathVariable String userCode, HttpServletResponse response) throws IOException {

        try {
            MessageDelivery msg = new MessageDelivery();
            msg.setPushType("A");
            msg.setMsgSender("system");
            msg.setMsgType("message");
            msg.setMsgReceiver(userCode);
            msg.setMsgSubject("测试消息");
            msg.setMsgContent("您收到一条测试消息，请点击查看：");
            msg.setRelUrl("http://www.baidu.com");
            messageDeliveryManager.pushMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonResultUtils.writeSuccessJson(response);
    }


    @RequestMapping(value = "/push/{msgType}/{userCode}", method = RequestMethod.GET)
    public void testPushStatus(@PathVariable String msgType,
                               @PathVariable String userCode,
                               @RequestParam("content" ) String content,
                               HttpServletResponse response) {
        MessageDelivery msg = new MessageDelivery();
        try {
            msg.setPushType("A");
            msg.setMsgSender("system");
            msg.setMsgType(msgType);
            msg.setMsgReceiver(userCode );
            msg.setMsgSubject("测试其他类别消息");
            msg.setMsgContent(content);
            msg.setRelUrl("http://www.baidu.com");
            messageDeliveryManager.pushMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonResultUtils.writeSingleDataJson(msg,response);
    }
}
