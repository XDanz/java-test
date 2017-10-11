package com.terraware.server.nio;

import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class EchoWorker implements Runnable {
    private static final Logger log = Logger.getLogger(NioServer.class.getName());
    private BlockingQueue<ServerDataEvent> queue = new LinkedBlockingQueue<>();

    public EchoWorker(BlockingQueue<ServerDataEvent> queue) {
        this.queue = queue;
    }

    public EchoWorker() {

    }

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
                    log.info( "sending");
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