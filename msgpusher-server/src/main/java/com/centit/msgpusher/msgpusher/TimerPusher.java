package com.centit.msgpusher.msgpusher;

import com.centit.msgpusher.service.MessageDeliveryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zhang_gd on 2017/4/26.
 */
@Service("timerPusher")
public class TimerPusher{

    @Resource
    private MessageDeliveryManager messageDeliveryManager;


    private static final Logger logger = LoggerFactory.getLogger(TimerPusher.class);

    @Scheduled(cron="0 0/5 9-18  * * ? ")   //每5分钟执行一次
    public void timePushMsg(){
        messageDeliveryManager.timerPusher();
    }

}
