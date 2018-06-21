package com.github.andygee.glue;

import com.github.andygee.boot.TestBoot;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Ignore;
import org.openqa.selenium.WebDriver;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

@Ignore
public class Common extends TestBoot {

    public static final ThreadLocal<WebDriver> Driver = new ThreadLocal<>();

    /**
     * Cucumber before
     */
    @Before
    public void before() {
        System.out.println("port = " + this.getPortPxc());
    }

    @When("^the page opens in the docker container$")
    public void test_002_pxc() throws Exception {
        Driver.get().get(new URL("http", "172.17.0.1", this.getPortPxc(), "/scs/").toExternalForm());
        // PagePxc.get().open("172.17.0.1", 1234);

        System.out.println("Page should be available via VNC at: " + System.getProperty("arq.cube.docker.browser.internal.ip") + ":5900");
        System.out.println("The VNC password is 'secret");
    }


    @Given("^the system properties are printed$")
    public void test_001_properties() {
        final Properties properties = System.getProperties();
        for (final Map.Entry<Object, Object> entry: properties.entrySet()) {
            System.out.println(String.format("%60s%120s", entry.getKey(), entry.getValue()));
        }
    }

    @Then("^wait for curl localhost:6666 if -Dwait=true$")
    public void test_999_wait() throws Exception {

        if (System.getProperty("wait", "false").equals("true")) {
            System.out.println("To kill the test environment `curl localhost:6666`");
            System.out.println("DO NOT KILL THE PROCESS, or you'll end up with dangling containers - See the README.adoc");
            try (final ServerSocket ss = new ServerSocket(6666)) {
                try (final Socket s = ss.accept()) {
                    //no-op - just wait
                }
            }
        } else {
            System.out.println("Test environment completed");
            System.out.println("If you add the -Dwait=true property, then the test will pause here...");
        }

        System.out.println("Closing everything");
    }

    @Then("^just finish the test$")
    public void justFinishTheTest() {
        System.out.println("I'm just cleaning up now....");
    }
}
