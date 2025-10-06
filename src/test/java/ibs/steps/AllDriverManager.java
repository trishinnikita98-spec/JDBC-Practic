package ibs.steps;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class AllDriverManager {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private  static String browserName = System.getProperty("browser");

    public static WebDriver getDriver() {
        if (driver == null) {
            initializeDriver();
        }
        return driver;
    }

    public static WebDriverWait getWait() {
        if (wait == null) {
            wait = new WebDriverWait(getDriver(),
                    Duration.ofSeconds(10));
        }
        return wait;
    }

    private static void initializeDriver() {
        if (ConfigManager.isSelenoidMode()) {
            driver = createSelenoidDriver(browserName);
            System.out.println("Режим: Selenoid");
        } else {
            driver = createLocalDriver();
            System.out.println("Режим: Локальный Chrome");
        }

        // Настройки драйвера
        driver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    private static WebDriver createSelenoidDriver(String browser) {
        try {


            DesiredCapabilities caps = new DesiredCapabilities();

            switch (browserName.toLowerCase()) {
                case "chrome":
                    caps.setBrowserName("chrome");
                    break;
                case "firefox":
                    caps.setBrowserName("firefox");
                    break;
                default:
                    throw new IllegalArgumentException("Неизвестный браузер: " + browser);
            }

            // Selenoid capabilities
            Map<String, Object> selenoidOptions = new HashMap<>();
            selenoidOptions.put("enableVNC", true);  // Видеть что происходит в браузере
            selenoidOptions.put("enableVideo", false); // Не записывать видео
            caps.setCapability("selenoid:options", selenoidOptions);

            String hubUrl = ConfigManager.getProperty("selenoid.url");
            System.out.println("Подключаюсь к Selenoid Hub: " + hubUrl);
            System.out.println("Мониторинг: " + ConfigManager.getProperty("selenoid.uiurl"));

            return new RemoteWebDriver(new URL(hubUrl), caps);

        } catch (Exception e) {
            System.err.println("Ошибка подключения к Selenoid Hub: " + e.getMessage());
            throw new RuntimeException("Failed to connect to Selenoid", e);
        }
    }

    private static WebDriver createLocalDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        System.setProperty("webdriver.chrome.driver", "chromedriver");

        return new ChromeDriver(options);
    }

    public static void openRegistrationPage() {
        getDriver().get(ConfigManager.getProperty("base.url"));
        System.out.println("Открыта страница: " + ConfigManager.getProperty("base.url"));
    }

    public static void closeDriver() {
        if (driver != null) {
            driver.manage().deleteAllCookies();
            driver.close();
            driver = null;
            wait = null;
            System.out.println("окно закрыто");
        }
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
            wait = null;
            System.out.println("окно закрыто");
        }
    }

    public static String getRunMode() {
        return ConfigManager.isSelenoidMode() ? "selenoid" : "local";
    }
}