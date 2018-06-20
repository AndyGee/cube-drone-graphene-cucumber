package com.github.andygee;

import org.arquillian.cube.docker.impl.client.config.Await;
import org.arquillian.cube.docker.impl.docker.DockerClientExecutor;
import org.arquillian.cube.spi.Cube;
import org.arquillian.cube.spi.await.AwaitStrategy;
import org.arquillian.cube.spi.metadata.HasPortBindings;

import java.net.Socket;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("FieldCanBeLocal")
public class AwaitSelenium implements AwaitStrategy {

    private Await params;
    private DockerClientExecutor dockerClientExecutor;
    private Cube<?> cube;

    public void setCube(final Cube<?> cube) {
        this.cube = cube;
    }

    public void setDockerClientExecutor(final DockerClientExecutor dockerClientExecutor) {
        this.dockerClientExecutor = dockerClientExecutor;
    }

    public void setParams(final Await params) {
        this.params = params;
    }

    @Override
    public boolean await() {
        final HasPortBindings hpb = this.cube.getMetadata(HasPortBindings.class);
        final Set<Integer> ports = hpb.getBoundPorts();

        Integer port = ports.iterator().next();
        final HasPortBindings.PortAddress address = hpb.getMappedAddress(port);
        final int mapped = null != address ? address.getPort() : -1;

        if (mapped > 0) {
            port = mapped;
        }

        final int connect = port;
        final CountDownLatch latch = new CountDownLatch(1);

        final Thread t = new Thread("Connect") {
            @Override
            public void run() {
                while (true) {
                    if (AwaitSelenium.this.connect(connect)) {
                        latch.countDown();
                        break;
                    } else {
                        try {
                            Thread.sleep(1000);
                        } catch (final InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        t.start();

        try {
            return latch.await(30, TimeUnit.SECONDS);
        } catch (final InterruptedException e) {
            return false;
        } finally {
            t.interrupt();
        }
    }

    private boolean connect(final int port) {

        try (final Socket s = new Socket("localhost", port)) {

            final boolean connected = s.isConnected();
            System.out.println("connected = " + connected);
            return connected;

        } catch (final Throwable e) {
            System.out.println("Waiting for server to be ready: " + e.getMessage());
            System.out.println("Run 'docker ps' to see the containers firing up");
        }
        return false;
    }
}
