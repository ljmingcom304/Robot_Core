package com.mmednet.klyl.mina;

public class StringBean {
    private byte cmd;
    private String data;

    public StringBean() {
        super();
    }

    public StringBean(byte cmd, String data) {
        super();
        this.cmd = cmd;
        this.data = data;
    }

    public byte getCmd() {
        return cmd;
    }

    public void setCmd(byte cmd) {
        this.cmd = cmd;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StringBean [cmd=" + cmd + ", data=" + data + "]";
    }

}
