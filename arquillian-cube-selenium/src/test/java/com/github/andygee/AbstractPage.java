package com.github.andygee;

import org.openqa.selenium.WebDriver;

import java.net.URL;

public abstract class AbstractPage {

    public abstract void open(final String host, final int port) throws Exception;

    protected void open(final WebDriver wd, final URL uri) {
        wd.get(uri.toExternalForm());
    }
}
