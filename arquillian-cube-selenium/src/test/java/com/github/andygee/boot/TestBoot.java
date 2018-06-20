package com.github.andygee.boot;

import com.github.andygee.ApplicationScs;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ApplicationScs.class})
@Ignore
public class TestBoot {

    static {
        System.setProperty("hostname", getMachineName());
        System.out.println("Hostname: " + System.getProperty("hostname", "failed"));
        System.out.println();
        System.out.println("NOTE: This test pulls the TAG docker image - To pre-cache the image (recommended) run:");
        System.out.println("sudo docker run -e -p 4444:4444 dockerregistry.europe.phoenixcontact.com/boot/standalone-chrome-debug:latest");
        System.out.println();
    }

    @LocalServerPort
    private int portPxc;

    private static String getMachineName() {

        final Map<String, String> env = System.getenv();

        if (env.containsKey("COMPUTERNAME")) {
            return env.get("COMPUTERNAME");
        } else {
            try {
                final InetAddress localHost = InetAddress.getLocalHost();
                final String hostName = localHost.getHostName();
                return env.getOrDefault("HOSTNAME", hostName);
            } catch (final UnknownHostException e) {
                throw new RuntimeException("Error getting host", e);
            }
        }
    }

    public int getPortPxc() {
        return this.portPxc;
    }
}
