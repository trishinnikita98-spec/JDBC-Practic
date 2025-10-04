package ibs.steps;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.ru.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.Properties;

public class RegistrationStep {


    private WebDriverWait wait;
    public WebDriver driver = AllDriverManager.getDriver();
    public DataTest data = new DataTest();
    static Properties props = new Properties();
    public String URI = "http://217.74.37.176/?route=account/register&language=ru-ru";
    public String SELENOID_URL = "https://selenoid-ui.applineselenoid.fvds.ru/#/";

    public RegistrationStep() {
        if (System.getProperty("type.browser") != null &&
                System.getProperty("type.browser").equals("chrome")) {
            // Конфигурация для Jenkins
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--remote-allow-origins=*");
            options.setBinary("/usr/bin/google-chrome"); // правильный путь для Linux

            this.driver = new ChromeDriver(options);
        } else {
            // Ваша текущая конфигурация для локального запуска
            this.driver = AllDriverManager.getDriver();
        }
    }
    @Before
    public void setUp() {
        this.driver = AllDriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        AllDriverManager.openRegistrationPage();

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@id='input-firstname' or @name='firstname']")
        ));
    }
    @Когда("пользователь вводит данные с email {string}")
    public void пользователь_вводит_данные(String email) {

        driver.findElement(By.id("input-firstname")).sendKeys(data.Firstname);
        driver.findElement(By.id("input-lastname")).sendKeys(data.Lastname);
        driver.findElement(By.id("input-email")).sendKeys(email);
        driver.findElement(By.id("input-password")).sendKeys(data.correctPassword);
    }

    @И("пользователь подписывает соглашение")
    public void пользователь_подписывает_соглашение() {
        //Соглашение
        if (data.policyAgree == true) {
            WebElement policy =
                    driver.findElement(By.xpath("//input[@name='agree']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", policy);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", policy);
        }
    }

    @И("пользователь подписывает новости")
    public void пользователь_подписывается_на_новости() {
        //Подписка на новости
        if (data.newsAgree == true) {
            WebElement policy =
                    driver.findElement(By.xpath("//*[contains(@id, 'news')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", policy);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", policy);
        }
    }

    @И("пользователь нажимает кнопку продолжить")
    public void регистрация() {
        WebElement btnContinue =
                driver.findElement(By.xpath("//*[contains(@type, 'submit')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btnContinue);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnContinue);
    }

    @И("проверка регистрации")
    public void проверка_регистрация() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String currentUrlAfter = driver.getCurrentUrl();
        Assertions.assertNotEquals("http://217.74.37.176/?route=account/register&language=ru-ru",
                currentUrlAfter, "Ссылка не изменилась, регистрация не пройдена.");

    }

    @И("проверка неудачной регистрации")
    public void проверка_неудачной_регистрация() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String currentUrlAfter = driver.getCurrentUrl();
        Assertions.assertEquals("http://217.74.37.176/?route=account/register&language=ru-ru",
                currentUrlAfter, "Ссылка не изменилась, регистрация не пройдена.");

    }


    @И("пользователь переходит на другую страницу сайта")
    public void переход() {
        WebElement btnBasket =
                driver.findElement(By.xpath("//span[contains(text(), 'Корзина')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnBasket);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertNotEquals("http://217.74.37.176/?route=account/register&language=ru-ru", currentUrl,
                "URL должны быть разные");
    }

    @И("пользователь возвращается")
    public void возврат() {
        driver.navigate().back();
        String currentUrl2 = driver.getCurrentUrl();
        Assertions.assertEquals("http://217.74.37.176/?route=account/register&language=ru-ru", currentUrl2,
                "URL должны быть одинаковые");
    }

    @И("проверка сохранения данных с email {string}")
    public void сохранение_данных(String mail) {
        WebElement firstname = driver.findElement(By.id("input-firstname"));
        String Firstname = firstname.getAttribute("value");
        Assertions.assertEquals(Firstname, data.Firstname, "Поле Имя не должно быть пустым");
        WebElement lastname = driver.findElement(By.id("input-lastname"));
        String Lastname = lastname.getAttribute("value");
        Assertions.assertEquals(Lastname, data.Lastname, "Поле Пароль не должно быть пустым");
        WebElement email = driver.findElement(By.id("input-email"));
        String Email = email.getAttribute("value");
        Assertions.assertEquals(Email, mail, "Поле Email не должно быть пустым");
        WebElement password = driver.findElement(By.id("input-password"));
        String Password = password.getAttribute("value");
        Assertions.assertEquals(Password, data.correctPassword, "Поле Пароль не должно быть пустым");
    }
}
