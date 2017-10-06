package com.terraware.server.nio;

import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EchoWorker implements Runnable {
    private final BlockingQueue<ServerDataEvent> queue = new LinkedBlockingQueue<>();

    void processData(NioServer server, SocketChannel socket, byte[] data, int count) {
        byte[] dataCopy = new byte[count];
        System.arraycopy(data, 0, dataCopy, 0, count);
        queue.add(new ServerDataEvent(server, socket, dataCopy));
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            ServerDataEvent dataEvent;

            try {
                while ((dataEvent = queue.take()) != null) {
                    // Wait for data to become available

                    // TODO: handle

                    // Return to sender
                    dataEvent.server.send(dataEvent.socket, dataEvent.data);
                }
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                Thread.currentThread().interrupt();
            }  catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}