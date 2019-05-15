package com.mmednet.reader.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.sunrise.icardreader.model.IdentityCardZ;

import java.io.File;
import java.io.Serializable;

import sunrise.IDImageUtil;

/**
 * Title:PersonBean
 * <p>
 * Description:读取身份返回信息
 * </p>
 * Author Jming.L
 * Date 2019/3/13 16:33
 */
public class PersonBean implements Parcelable {

    public String name;             //姓名
    public String sex;              //性别
    public String cardNo;           //身份证号
    public String nation;           //民族
    public String birthday;         //出生日期
    public String address;          //家庭住址
    public String authority;        //签发机关
    public String period;           //有效期限yyyyMMdd-yyyyMMdd
    public Bitmap icon;             //头像

    public PersonBean() {

    }

    protected PersonBean(Parcel in) {
        name = in.readString();
        sex = in.readString();
        cardNo = in.readString();
        nation = in.readString();
        birthday = in.readString();
        address = in.readString();
        authority = in.readString();
        period = in.readString();
        icon = in.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(sex);
        dest.writeString(cardNo);
        dest.writeString(nation);
        dest.writeString(birthday);
        dest.writeString(address);
        dest.writeString(authority);
        dest.writeString(period);
        dest.writeParcelable(icon, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PersonBean> CREATOR = new Creator<PersonBean>() {
        @Override
        public PersonBean createFromParcel(Parcel in) {
            return new PersonBean(in);
        }

        @Override
        public PersonBean[] newArray(int size) {
            return new PersonBean[size];
        }
    };

    public static PersonBean initPersonBean(IdentityCardZ cardZ) {
        if (cardZ != null) {
            PersonBean bean = new PersonBean();
            //通过idType来判断是否是外国人身份证，外国人身份证是I,港澳台身份证是J
            if (TextUtils.equals(cardZ.idType, "I")) {
                bean.name = cardZ.chineseName;
                bean.authority = cardZ.authorityCode;
                bean.period = cardZ.issuanceDate + " - " + cardZ.expiryDate;
            } else if (TextUtils.equals(cardZ.idType, "J")) {
                bean.name = cardZ.name;
                bean.address = cardZ.address;
                bean.authority = cardZ.authority;
                bean.period = cardZ.period;
            } else {
                bean.name = cardZ.name;
                bean.nation = cardZ.ethnicity;
                bean.address = cardZ.address;
                bean.authority = cardZ.authority;
                bean.period = cardZ.period;
            }

            bean.sex = cardZ.sex;
            bean.cardNo = cardZ.cardNo;
            bean.birthday = cardZ.birth;
            bean.icon = IDImageUtil.dealIDImage(cardZ.avatar);
            return bean;
        }
        return null;
    }

    @Override
    public String toString() {
        return "PersonBean{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", nation='" + nation + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", authority='" + authority + '\'' +
                ", period='" + period + '\'' +
                ", icon=" + icon +
                '}';
    }
}
