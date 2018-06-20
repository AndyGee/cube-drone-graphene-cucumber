package com.github.andygee.cucumber;

import cucumber.api.junit.Cucumber;
import cucumber.runtime.junit.FeatureRunner;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CucumberEx extends Cucumber {

    private final Class clazz;

    /**
     * Constructor called by JUnit.
     *
     * @param clazz the class with the @RunWith annotation.
     * @throws IOException         if there is a problem
     * @throws InitializationError if there is another problem
     */
    public CucumberEx(final Class clazz) throws InitializationError, IOException {
        super(clazz);
        this.clazz = clazz;
        System.out.println("clazz = " + this.clazz.getName());
    }

    @Override
    protected void runChild(final FeatureRunner child, final RunNotifier notifier) {

        final RunListener listener = new RunListener() {
            @Override
            public void testStarted(final Description description) throws Exception {
                System.out.println("description1 = " + description);
                System.out.println("child1 = " + child);
                System.out.println("notifier1 = " + notifier);
                super.testStarted(description);
            }
        };
        notifier.addListener(listener);

        child.run(notifier);
    }

    @Override
    public void run(final RunNotifier notifier) {

        notifier.addListener(new RunListener() {
            @Override
            public void testStarted(final Description description) throws Exception {
                System.out.println("description2 = " + description);
                System.out.println("notifier2 = " + notifier);
                super.testStarted(description);
            }
        });

        try {
            super.run(notifier);
        } catch (final Exception e) {
            e.printStackTrace();
            Logger.getLogger(CucumberEx.class.getName()).log(Level.WARNING, "Failed to run", e);
        }
    }
}
