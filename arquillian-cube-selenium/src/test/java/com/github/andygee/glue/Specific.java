package com.github.andygee.glue;

import cucumber.api.java.en.Then;

public class Specific {

    /**
     * I can put Glue anywhere, once the common glue is bootsrapped
     */
    @Then("^just finish the test$")
    public void justFinishTheTest() {
        System.out.println("I'm just cleaning up now....");
    }
}
