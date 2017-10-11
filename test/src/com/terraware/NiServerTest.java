package com.terraware;

import com.terraware.server.nio.EchoWorker;
import com.terraware.server.nio.NioServer;
import com.terraware.server.nio.ServerDataEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.mockito.Mockito.verify;

/**
 * @author Daniel Terranova <mailto:daniel.terranova@so4it.com>
 * @since 0.8.0
 */
public class NiServerTest {
    BlockingQueue<ServerDataEvent> queue;

    @BeforeEach
    void setUp() throws Exception {

    }

    @Test
    void testThatWeCallSend() throws UnknownHostException, InterruptedException {
        queue = new LinkedBlockingQueue<>();
        NioServer nioServer = Mockito.mock(NioServer.class);
        SocketChannel socketChannel = Mockito.mock(SocketChannel.class);
        byte[] hej = "hej".getBytes();
        queue.add(new ServerDataEvent(nioServer,socketChannel,  hej));
        EchoWorker e = new EchoWorker(queue);
        Thread thread = new Thread(e);
        thread.start();
        verify(nioServer).send(socketChannel, hej);
    }

    @Test
    void test() throws IOException, InterruptedException {
        EchoWorker echoWorker = Mockito.mock(EchoWorker.class);
        InetAddress inetAddress = Mockito.mock(InetAddress.class);
        NioServer nioServer = new NioServer(inetAddress, 9090, echoWorker );
        SocketChannel socketChannel = Mockito.mock(SocketChannel.class);
        nioServer.send(socketChannel, "hej".getBytes());

    }
}
