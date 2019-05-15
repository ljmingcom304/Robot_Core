package com.mmednet.klyl.mina;

import java.util.Arrays;

public class ByteBean {
    private byte cmd;
    private byte[] data;

    public ByteBean() {
        super();
    }

    public byte getCmd() {
        return cmd;
    }

    public void setCmd(byte cmd) {
        this.cmd = cmd;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public ByteBean(byte cmd, byte[] data) {
        super();
        this.cmd = cmd;
        this.data = data;
    }

    @Override
    public String toString() {
        return "MsgBean [cmd=" + cmd + ", data=" + Arrays.toString(data) + "]";
    }

}
