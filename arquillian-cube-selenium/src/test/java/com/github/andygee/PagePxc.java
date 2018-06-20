package com.github.andygee;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.test.spi.annotation.ClassScoped;
import org.openqa.selenium.WebDriver;

import java.net.URL;

public class PagePxc extends AbstractPage {

    @ClassScoped
    @Pxc
    @Drone
    private static WebDriver pxc;

    public void open(final String host, final int port) throws Exception {
        this.open(pxc, new URL("http", host, port, "/scs/"));
    }
}
