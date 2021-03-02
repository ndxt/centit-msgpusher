package com.centit.msgpusher.commons;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang_gd on 2017/4/21.
 */
@Service
public class MsgPushConfigImpl implements MsgPushConfig{
    public MsgPushConfigImpl(){

    }
    private List<OSMsgPushInfo> osInfos;

    public List<OSMsgPushInfo> getOsInfos() {
        return osInfos;
    }

    public void setOsInfos(List<OSMsgPushInfo> osInfos) {
        this.osInfos = osInfos;
    }

    @Resource
    private MsgPushConfig msgPushConfig;

    @PostConstruct
    public void init(){
        osInfos = new ArrayList<>();
        reloadConfig();
    }

    @Override
    public void reloadConfig() {
        BufferedReader reader = null;
        String lastStr = "";
        try {
            String path = this.getClass().getClassLoader().getResource("").toURI().getPath();
            System.out.println("path=" + path);
            FileInputStream fileInputStream = new FileInputStream(path + "msg_pusher_metadata.json");
            InputStreamReader inputStreamReader = new InputStreamReader(
                    fileInputStream, "utf-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                lastStr += tempString;
            }
            reader.close();
            JSONObject jo= JSONObject.parseObject(lastStr);
            System.out.println("jo=" + jo);
            String con = jo.get("osInfos").toString();
            JSONArray jsonArray = JSONObject.parseArray(con);
            System.out.println("con=" + con);
            for (int i = 0; i<jsonArray.size();i++){
                OSMsgPushInfo osMsgPushInfo = new OSMsgPushInfo();
                JSONObject json= JSONObject.parseObject(jsonArray.get(i).toString());
                osMsgPushInfo.setOsId(json.get("osId").toString());
                osMsgPushInfo.setAndroidPkg(json.get("androidPkg").toString());
                String opt = json.get("optInfos").toString();
                JSONArray jsonOptArray = JSONObject.parseArray(opt);
                List<OptMsgPushInfo> optInfos = new ArrayList<>();
                for (int j = 0; j<jsonOptArray.size();j++){
                    OptMsgPushInfo optMsgPushInfo = new OptMsgPushInfo();
                    JSONObject jsonOpt= JSONObject.parseObject(jsonOptArray.get(j).toString());
                    optMsgPushInfo.setOptId(jsonOpt.get("optId").toString());
                    optMsgPushInfo.setAndroidView(jsonOpt.get("androidView").toString());
                    optInfos.add(optMsgPushInfo);
                }
                osMsgPushInfo.setOptInfos(optInfos);
                osInfos.add(osMsgPushInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public OSMsgPushInfo getOSConfig(String osId) {
        for(OSMsgPushInfo osinfo: osInfos ){
            if(StringUtils.equals(osinfo.getOsId(),osId)) {
                return osinfo;
            }
        }
        return null;
    }

    @Override
    public List<OSMsgPushInfo> getOsConfig(){
        List<OSMsgPushInfo> list = new ArrayList<>();
        for(OSMsgPushInfo osinfo: osInfos ){
            list.add(osinfo);
        }
        return list;
    }
}
