package com.centit.msgpusher.commons;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by zhang_gd on 2017/4/21.
 */
public class OSMsgPushInfo {
    private String osId;
    private String androidPkg;
    private List<OptMsgPushInfo> optInfos;

    public String getOsId() {
        return osId;
    }

    public void setOsId(String osId) {
        this.osId = osId;
    }

    public String getAndroidPkg() {
        return androidPkg;
    }

    public void setAndroidPkg(String androidPkg) {
        this.androidPkg = androidPkg;
    }

    public List<OptMsgPushInfo> getOptInfos() {
        return optInfos;
    }

    public void setOptInfos(List<OptMsgPushInfo> optInfos) {
        this.optInfos = optInfos;
    }

    public OptMsgPushInfo getOptMsgPushConfig(String optId){
        for(OptMsgPushInfo optinfo: optInfos ){
            if(StringUtils.equals(optinfo.getOptId(),optId)) {
                return optinfo;
            }
        }
        return null;
    }
}
