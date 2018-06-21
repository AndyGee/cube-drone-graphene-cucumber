package com.github.andygee;

import com.github.andygee.cucumber.CucumberEx;
import com.github.andygee.glue.Common;
import cucumber.api.CucumberOptions;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.ArquillianTest;
import org.jboss.arquillian.junit.ArquillianTestClass;
import org.jboss.arquillian.junit.MethodRuleChain;
import org.jboss.arquillian.test.spi.annotation.ClassScoped;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.RuleChain;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;


@RunWith(CucumberEx.class)
@CucumberOptions(
        features = {"classpath:features/one.feature"},
        //glue = {"classpath:com/tomitribe/driver/glue/"},
        plugin = {"html:target/one.html", "json:target/one.json"}
)
@RunAsClient
@SuppressWarnings("ArquillianClassEnabled")
public class CucumberOneTest {

    @ClassRule
    public static final RuleChain chainClass = RuleChain.outerRule(new ArquillianTestClass() {
        @Override
        public Statement apply(final Statement base, final Description description) {

            System.out.println("base = " + base);

            return super.apply(base, description);
        }
    }).around(new SpringClassRule());

    @Rule
    public final MethodRuleChain chainMethod = MethodRuleChain.outer(new ArquillianTest()).around(new SpringMethodRule());

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @LocalServerPort
    private int portPxc;

    @ClassScoped
    @Pxc
    @Drone
    private static WebDriver driver;

    @Page
    public static PagePxc pagePxc;

    @BeforeClass
    public static void beforeClass() {
        System.out.println("beforeClass = " + driver);
        Common.Driver.set(driver);

//        GrapheneContext.setContextFor(new GrapheneConfiguration(), driver, Default.class);
//        final GrapheneRuntime instance = GrapheneRuntime.getInstance();
//        System.out.println("instance = " + instance);
    }
}
