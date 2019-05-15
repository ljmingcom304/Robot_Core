package com.mmednet.klyl.bean;

import java.io.Serializable;

public class ACOrgUser implements Serializable {
	public int m_uiversionid;
	public int m_uideleteflag;
	public int m_uiorgid;//群组ID
	public String m_struseraccount;//用户ID
	public int m_uiusertype;
	public int m_iuserstatus;//在线状态
	public int m_uibinduser;
	public String m_strusername;
	public String m_strpassword;
	public String m_strphone;
	public String m_stremail;
	public long m_dtbirthday;
	public long m_dtcreatetime;
	public String m_strsettingjson;
	public String m_strextendjson;
	public long m_iuserperm;
    public int getM_uiversionid() {
        return m_uiversionid;
    }
    
    
    public ACOrgUser() {
        super();
    }


    public ACOrgUser(int m_uiversionid, int m_uideleteflag, int m_uiorgid,
            String m_struseraccount, int m_uiusertype, int m_iuserstatus,
            int m_uibinduser, String m_strusername, String m_strpassword,
            String m_strphone, String m_stremail, long m_dtbirthday,
            long m_dtcreatetime, String m_strsettingjson,
            String m_strextendjson, long m_iuserperm) {
        super();
        this.m_uiversionid = m_uiversionid;
        this.m_uideleteflag = m_uideleteflag;
        this.m_uiorgid = m_uiorgid;
        this.m_struseraccount = m_struseraccount;
        this.m_uiusertype = m_uiusertype;
        this.m_iuserstatus = m_iuserstatus;
        this.m_uibinduser = m_uibinduser;
        this.m_strusername = m_strusername;
        this.m_strpassword = m_strpassword;
        this.m_strphone = m_strphone;
        this.m_stremail = m_stremail;
        this.m_dtbirthday = m_dtbirthday;
        this.m_dtcreatetime = m_dtcreatetime;
        this.m_strsettingjson = m_strsettingjson;
        this.m_strextendjson = m_strextendjson;
        this.m_iuserperm = m_iuserperm;
    }


    public void setM_uiversionid(int m_uiversionid) {
        this.m_uiversionid = m_uiversionid;
    }
    public int getM_uideleteflag() {
        return m_uideleteflag;
    }
    public void setM_uideleteflag(int m_uideleteflag) {
        this.m_uideleteflag = m_uideleteflag;
    }
    public int getM_uiorgid() {
        return m_uiorgid;
    }
    public void setM_uiorgid(int m_uiorgid) {
        this.m_uiorgid = m_uiorgid;
    }
    public String getM_struseraccount() {
        return m_struseraccount;
    }
    public void setM_struseraccount(String m_struseraccount) {
        this.m_struseraccount = m_struseraccount;
    }
    public int getM_uiusertype() {
        return m_uiusertype;
    }
    public void setM_uiusertype(int m_uiusertype) {
        this.m_uiusertype = m_uiusertype;
    }
    public int getM_iuserstatus() {
        return m_iuserstatus;
    }
    public void setM_iuserstatus(int m_iuserstatus) {
        this.m_iuserstatus = m_iuserstatus;
    }
    public int getM_uibinduser() {
        return m_uibinduser;
    }
    public void setM_uibinduser(int m_uibinduser) {
        this.m_uibinduser = m_uibinduser;
    }
    public String getM_strusername() {
        return m_strusername;
    }
    public void setM_strusername(String m_strusername) {
        this.m_strusername = m_strusername;
    }
    public String getM_strpassword() {
        return m_strpassword;
    }
    public void setM_strpassword(String m_strpassword) {
        this.m_strpassword = m_strpassword;
    }
    public String getM_strphone() {
        return m_strphone;
    }
    public void setM_strphone(String m_strphone) {
        this.m_strphone = m_strphone;
    }
    public String getM_stremail() {
        return m_stremail;
    }
    public void setM_stremail(String m_stremail) {
        this.m_stremail = m_stremail;
    }
    public long getM_dtbirthday() {
        return m_dtbirthday;
    }
    public void setM_dtbirthday(long m_dtbirthday) {
        this.m_dtbirthday = m_dtbirthday;
    }
    public long getM_dtcreatetime() {
        return m_dtcreatetime;
    }
    public void setM_dtcreatetime(long m_dtcreatetime) {
        this.m_dtcreatetime = m_dtcreatetime;
    }
    public String getM_strsettingjson() {
        return m_strsettingjson;
    }
    public void setM_strsettingjson(String m_strsettingjson) {
        this.m_strsettingjson = m_strsettingjson;
    }
    public String getM_strextendjson() {
        return m_strextendjson;
    }
    public void setM_strextendjson(String m_strextendjson) {
        this.m_strextendjson = m_strextendjson;
    }
    public long getM_iuserperm() {
        return m_iuserperm;
    }
    public void setM_iuserperm(long m_iuserperm) {
        this.m_iuserperm = m_iuserperm;
    }
}
