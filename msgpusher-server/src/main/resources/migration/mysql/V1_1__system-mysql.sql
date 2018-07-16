
DROP TABLE IF EXISTS F_USER_NOTIFY_SETTING;
DROP TABLE IF EXISTS F_MESSAGE_DELIVERY;
DROP TABLE IF EXISTS F_USER_MSG_POINT;


CREATE TABLE F_USER_MSG_POINT
(
   OS_ID                VARCHAR(20) NOT NULL,
   USER_CODE            VARCHAR(32) NOT NULL,
   USER_NAME            VARCHAR(50),
   DEVICE_TYPE          CHAR COMMENT '3 ：android ； 4：apple ， 0： 没有',
   DEVICE_ID            VARCHAR(100),
   OS_VERSION           VARCHAR(100),
   CHANNEL_ID           VARCHAR(100),
   WX_TOKEN             VARCHAR(100) COMMENT '公众号下的令牌',
   MOBILE_PHONE         VARCHAR(13) COMMENT '用于短信',
   EMAIL_ADDRESS        VARCHAR(100),
   PRIMARY KEY (USER_CODE, OS_ID)
);
ALTER TABLE F_USER_MSG_POINT COMMENT '用户设置 自己接收 通知的方式。';


CREATE TABLE F_MESSAGE_DELIVERY
(
   MSG_ID               VARCHAR(32) NOT NULL,
   MSG_SENDER           VARCHAR(100),
   PUSH_TYPE            CHAR(1) COMMENT '点对点、群发',
   MSG_RECEIVER         VARCHAR(100) NOT NULL,
   MSG_TYPE             VARCHAR(32) COMMENT '客户端用来解析',
   MSG_SUBJECT          VARCHAR(100),
   MSG_CONTENT          VARCHAR(1000) NOT NULL,
   REL_URL              VARCHAR(500),
   NOTICE_TYPES         VARCHAR(100) COMMENT '可以多种方式  A：app推送， S：短信  C：微信  N：内部通知系统',
   PUSH_STATE           CHAR(1) COMMENT '0 成功， 1 失败 2 部分成功',
   PUSH_RESULT          VARCHAR(500) COMMENT '成功为 第三方 msgid 失败为 失败原因',
   PLAN_PUSH_TIME       DATETIME,
   PUSH_TIME            DATETIME,
   VALID_PERIOD         DATETIME,
   OS_ID                VARCHAR(20),
   OPT_ID               VARCHAR(64) NOT NULL COMMENT '模块，或者表',
   OPT_METHOD           VARCHAR(64) COMMENT '方法，或者字段',
   OPT_TAG              VARCHAR(200) COMMENT '一般用于关联到业务主体',
   PRIMARY KEY (MSG_ID)
);


CREATE TABLE F_USER_NOTIFY_SETTING
(
   USER_SETTING_ID      VARCHAR(32) NOT NULL,
   USER_CODE            VARCHAR(32),
   OS_ID                VARCHAR(20) COMMENT '"Default" 表示所有系统的默认值',
   OPT_ID               VARCHAR(64) NOT NULL COMMENT '"Default" 表示所有系统的默认值模块',
   NOTIFY_TYPES         VARCHAR(100) COMMENT '接收通知的方式，可以有多种方式',
   PRIMARY KEY (USER_SETTING_ID)
);
ALTER TABLE F_USER_NOTIFY_SETTING COMMENT '用户设置 自己接收 通知的方式。';

