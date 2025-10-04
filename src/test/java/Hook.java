package ibs.steps;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    @Before
    public void beforeScenario(Scenario scenario) {
        System.out.println("Starting scenario: " + scenario.getName());
        // Сбрасываем сессию перед каждым сценарием
        AllDriverManager.resetSession();
    }

    @After
    public void afterScenario(Scenario scenario) {
        System.out.println("Finished scenario: " + scenario.getName() + " - " + scenario.getStatus());

        // НЕ закрываем драйвер здесь, чтобы он переиспользовался между тестами
        // Только сбрасываем состояние
        AllDriverManager.resetSession();
    }

    // Закрываем драйвер только после всех тестов
    @AfterAll
    public static void afterAll() {
        AllDriverManager.quitDriver();
    }
}
