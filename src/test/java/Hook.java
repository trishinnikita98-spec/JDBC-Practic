import ibs.steps.AllDriverManager;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hook {

    @Before
    public void beforeScenario(Scenario scenario) {
        System.out.println("BEFORE: " + scenario.getName());
        System.out.println("Режим запуска: " + AllDriverManager.getRunMode());
    }

    @After
    public void afterScenario(Scenario scenario) {
        AllDriverManager.quitDriver();
    System.out.println("AFTER: " + scenario.getName() + " - " + scenario.getStatus());
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("AFTER ALL: Завершение тестов");
        AllDriverManager.quitDriver();
    }
}
