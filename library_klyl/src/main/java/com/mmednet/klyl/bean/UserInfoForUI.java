package com.mmednet.klyl.bean;

import java.io.Serializable;

public class UserInfoForUI implements Serializable {
    ACOrgUser userInfo = null;
    SortModel sortInfo = null;

    public UserInfoForUI() {
        super();
    }

    public UserInfoForUI(ACOrgUser userInfo, SortModel sortInfo) {
        super();
        this.userInfo = userInfo;
        this.sortInfo = sortInfo;
    }

    public SortModel getSortInfo() {
        return sortInfo;
    }

    public void setSortInfo(SortModel sortInfo) {
        this.sortInfo = sortInfo;
    }

    public ACOrgUser getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(ACOrgUser userInfo) {
        this.userInfo = userInfo;
    }

    public int describeContents() {
        return 0;
    }
}
