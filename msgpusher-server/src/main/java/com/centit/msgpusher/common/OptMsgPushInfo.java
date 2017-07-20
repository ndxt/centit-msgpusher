package com.centit.msgpusher.common;

/**
 * Created by zhang_gd on 2017/4/21.
 */
public class OptMsgPushInfo {
    private String optId;
    private String androidView;

    public String getOptId() {
        return optId;
    }

    public void setOptId(String optId) {
        this.optId = optId;
    }

    public String getAndroidView() {
        return androidView;
    }

    public void setAndroidView(String androidView) {
        this.androidView = androidView;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OptMsgPushInfo that = (OptMsgPushInfo) o;

        return optId.equals(that.optId);

    }

    @Override
    public int hashCode() {
        return optId.hashCode();
    }
}
