package com.terraware.server.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NioServer implements Runnable {
    // The host:port combination to listen on
    private InetAddress hostAddress;
    private int port;

    // The channel on which we'll accept connections
    private ServerSocketChannel serverChannel;

    // The selector we'll be monitoring
    private Selector selector;

    // The buffer into which we'll read data when it's available
    private ByteBuffer readBuffer = ByteBuffer.allocate(8192);

    private EchoWorker worker;

    // A list of PendingChange instances
    private final Queue<ChangeRequest> pendingChanges = new ConcurrentLinkedQueue<>();

    // Maps a SocketChannel to a list of ByteBuffer instances
    private final Map<SocketChannel,Queue<ByteBuffer>> pendingData = new ConcurrentHashMap<>();

    private NioServer(InetAddress hostAddress, Integer port, EchoWorker worker) throws IOException {
        this.hostAddress = Objects.requireNonNull(hostAddress, "hostAddress");
        this.port = Objects.requireNonNull(port, "port");
        this.worker = Objects.requireNonNull(worker, "worker");
        this.selector = this.initSelector();
    }

    void send(SocketChannel socket, byte[] data) {

        // Indicate we want the interest ops set changed
        pendingChanges.add(new ChangeRequest(socket, ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));

        // And queue the data we want written
        Queue<ByteBuffer> queue = pendingData.computeIfAbsent(socket, k -> new ConcurrentLinkedQueue<>());
        queue.add(ByteBuffer.wrap(data));

        // Finally, wake up our selecting thread so it can make the required changes
        selector.wakeup();
    }

    public void run() {
        while (true) {
            try {
                // Process any pending changes

                ChangeRequest changeRequest;
                while ( (changeRequest = pendingChanges.poll()) != null) {

                    switch (changeRequest.getType()) {
                        case ChangeRequest.CHANGEOPS:
                            SelectionKey key = changeRequest.socket.keyFor(selector);
                            key.interestOps(changeRequest.getOps());
                    }
                }

                // Wait for an event one of the registered channels
                selector.select();

                // Iterate over the set of keys for which events are available
                Iterator selectedKeys = selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = (SelectionKey) selectedKeys.next();
                    selectedKeys.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    // Check what event is available and deal with it
                    if (key.isAcceptable()) {
                        accept(key);
                    } else if (key.isReadable()) {
                        read(key);
                    } else if (key.isWritable()) {
                        write(key);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        // For an accept to be pending the channel must be a server socket channel.
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

        // Accept the connection and make it non-blocking
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);

        // Register the new SocketChannel with our Selector, indicating
        // we'd like to be notified when there's data waiting to be read
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        // Clear out our read buffer so it's ready for new data
        readBuffer.clear();

        // Attempt to read off the channel
        int numRead;
        try {
            numRead = socketChannel.read(readBuffer);
        } catch (IOException e) {
            // The remote forcibly closed the connection, cancel
            // the selection key and close the channel.
            key.cancel();
            socketChannel.close();
            return;
        }

        if (numRead == -1) {
            // Remote entity shut the socket down cleanly. Do the
            // same from our end and cancel the channel.
            key.channel().close();
            key.cancel();
            return;
        }

        // Hand the data off to our worker thread
        worker.processData(this, socketChannel, this.readBuffer.array(), numRead);
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        Queue<ByteBuffer> queue = pendingData.get(socketChannel);

        // Write until there's not more data ...
        ByteBuffer buf;
        while ((buf = queue.poll()) != null) {
            socketChannel.write(buf);
            if (buf.remaining() > 0) {
                // ... or the socket's buffer fills up
                break;
            }
        }


        if (queue.isEmpty()) {
            // We wrote away all data, so we're no longer interested
            // in writing on this socket. Switch back to waiting for
            // data.
            key.interestOps(SelectionKey.OP_READ);
        }
    }

    private Selector initSelector() throws IOException {
        // Create a new selector
        Selector socketSelector = SelectorProvider.provider().openSelector();

        // Create a new non-blocking server socket channel
        this.serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        // Bind the server socket to the specified address and port
        InetSocketAddress isa = new InetSocketAddress(this.hostAddress, this.port);
        serverChannel.socket().bind(isa);

        // Register the server socket channel, indicating an interest in
        // accepting new connections
        serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

        return socketSelector;
    }

    public static void main(String[] args) {
        try {
            EchoWorker worker = new EchoWorker();
            new Thread(worker).start();
            new Thread(new NioServer(null, 9090, worker)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}