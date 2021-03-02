package com.centit.msgpusher.config;

import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.components.impl.NotificationCenterImpl;
import com.centit.framework.components.impl.TextOperationLogWriterImpl;
import com.centit.framework.config.SpringSecurityDaoConfig;
import com.centit.framework.ip.app.config.IPAppSystemBeanConfig;
import com.centit.framework.jdbc.config.JdbcConfig;
import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.framework.model.adapter.OperationLogWriter;
import com.centit.msgpusher.plugins.BaiduMsgPusher;
import com.centit.msgpusher.plugins.EMailMsgPusher;
import com.centit.msgpusher.plugins.JiguangMsgPusher;
import com.centit.msgpusher.plugins.SocketMsgPusherImpl;
import com.centit.msgpusher.service.MsgPusherCenter;
import com.centit.msgpusher.service.impl.MsgPusherCenterImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = {"com.centit.*"})
@Import({SpringSecurityDaoConfig.class,
    //SpringSecurityCasConfig.class,
    IPAppSystemBeanConfig.class, JdbcConfig.class})

public class ServiceConfig {

    @Value("${app.home:./}")
    private String appHome;

    @Value("${email.hostName}")
    public String  emailServerHost;
    @Value("${email.userName}")
    public String  emailServerHostUser;
    @Value("${email.userPassword}")
    public String  emailServerHostPwd;


    @Bean(value = "notificationCenter")
    @Primary
    public NotificationCenter notificationCenter() {
        NotificationCenterImpl notificationCenter = new NotificationCenterImpl();
        notificationCenter.initDummyMsgSenders();
        //notificationCenter.registerMessageSender("innerMsg",innerMessageManager);
        return notificationCenter;
    }

    @Bean
    @Lazy(value = false)
    public OperationLogWriter operationLogWriter() {
        TextOperationLogWriterImpl operationLog = new TextOperationLogWriterImpl();
        operationLog.setOptLogHomePath(appHome+"/logs");
        operationLog.init();
        return operationLog;
    }

    @Bean
    public InstantiationServiceBeanPostProcessor instantiationServiceBeanPostProcessor() {
        return new InstantiationServiceBeanPostProcessor();
    }

    @Bean(value = "jiguangMsgPusher")
    public JiguangMsgPusher jiguangMsgPusher() {
        return new JiguangMsgPusher();
    }

   @Bean(value = "socketMsgPusher")
    public SocketMsgPusherImpl socketMsgPusher() {
        return new SocketMsgPusherImpl();
    }

    @Bean(value = "emailPusher")
    public EMailMsgPusher emailPusher() {
        return new EMailMsgPusher();
    }

    @Bean(value = "baiduMsgPusher")
    public BaiduMsgPusher baiduMsgPusher() {
        return new BaiduMsgPusher();
    }

    @Bean
    public MsgPusherCenter msgPusherCenter() {
        MsgPusherCenterImpl msgPusher =  new MsgPusherCenterImpl();
        //设置默认的推送方式   邮件推送
        msgPusher.registerMessageSender("email", emailPusher());
        msgPusher.registerMessageSender("socket", socketMsgPusher());
        msgPusher.registerMessageSender("baidu", baiduMsgPusher());
        msgPusher.registerMessageSender("jiguang", jiguangMsgPusher());
        emailPusher().setUserEmailSupport( (userCode)->
                CodeRepositoryUtil.getUserInfoByCode(userCode).getRegEmail());
        emailPusher().setEmailServerHost(emailServerHost);
        emailPusher().setEmailServerHostUser(emailServerHostUser);
        emailPusher().setEmailServerHostPwd(emailServerHostPwd);
        return msgPusher;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
