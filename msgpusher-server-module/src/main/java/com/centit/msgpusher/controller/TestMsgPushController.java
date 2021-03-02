package com.centit.msgpusher.controller;

import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.msgpusher.po.MessageDelivery;
import com.centit.msgpusher.service.MessageDeliveryManager;
import com.centit.msgpusher.service.MsgPusherCenter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * IPushMessage  Controller.
 * create by scaffold 2017-04-07
 * @author codefan@sina.com
 * 消息推送null
*/


@RestController
@RequestMapping("/testMsgPush")
@Api(tags = "测试控制器")
public class TestMsgPushController extends BaseController {
    private static final Log log = LogFactory.getLog(TestMsgPushController.class);

    @Resource
    private MessageDeliveryManager messageDeliveryManager;

    @Resource
    MsgPusherCenter msgPusherCenter;

    @ApiOperation("点对点消息推送测试")
    @RequestMapping(value = "/pushmsg/{userCode}", method = RequestMethod.GET)
    public ResponseData testPushMessage(@PathVariable String userCode, HttpServletResponse response) throws IOException {
        try {
            //注册消息推送方式  app
          //  msgPusherCenter.registerMessageSender("A",socketMsgPusher);
            MessageDelivery msg = new MessageDelivery();
            msg.setPushType("A");
            msg.setOptId("test");
            msg.setNoticeTypes("email,socket");//app,email推送
            msg.setMsgSender("system");
            msg.setMsgType("message");
            msg.setMsgReceiver(userCode);
            msg.setMsgSubject("测试消息");
            msg.setMsgContent("您收到一条测试消息，请点击查看：");
            //msg.setRelUrl("http://www.baidu.com");
            return messageDeliveryManager.pushMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseData.makeErrorMessage(ResponseData.ERROR_INTERNAL_SERVER_ERROR,"服务器处理异常！");
    }

    @ApiOperation("群发消息推送测试")
    @RequestMapping(value = "/pushallmsg", method = RequestMethod.GET)
    public ResponseData testPushMessage(HttpServletResponse response) throws IOException {
        try {
            //注册消息推送方式  app
            //msgPusherCenter.registerMessageSender("A",socketMsgPusher);
            MessageDelivery msg = new MessageDelivery();
            msg.setPushType("B");
            msg.setOptId("testall");
            msg.setNoticeTypes("email,socket");//app,email推送
            msg.setMsgSender("zhouchao");
            msg.setMsgType("message");
           // msg.setMsgReceiver(userCode);
            msg.setMsgSubject("群发测试消息");
            msg.setMsgContent("您收到一条测试消息，请点击查看：");
            //msg.setRelUrl("http://www.baidu.com");
            return messageDeliveryManager.pushMsgToAll(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseData.makeErrorMessage(ResponseData.ERROR_INTERNAL_SERVER_ERROR,"服务器处理异常！");
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
            //msg.setRelUrl("http://www.baidu.com");
            messageDeliveryManager.pushMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonResultUtils.writeSingleDataJson(msg,response);
    }
}
