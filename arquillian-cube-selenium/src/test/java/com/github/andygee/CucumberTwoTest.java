package com.github.andygee;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {
                "classpath:com.tomitribe.pxc.glue/",
        },
        features = {"classpath:features/two.feature"},
        plugin = {"html:target/two.html", "json:target/two.json"}
)
public class CucumberTwoTest {
}
