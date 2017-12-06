package com.terraware.nio;
import java.nio.channels.SocketChannel;

public class ChangeRequest {
    public static final int REGISTER = 1;
    static final int CHANGEOPS = 2;

    SocketChannel socket;
    private int type;
    private int ops;

    ChangeRequest(SocketChannel socket, int type, int ops) {
        this.socket = socket;
        this.type = type;
        this.ops = ops;
    }

    int getType() {
        return type;
    }

    int getOps() {
        return ops;
    }
}
