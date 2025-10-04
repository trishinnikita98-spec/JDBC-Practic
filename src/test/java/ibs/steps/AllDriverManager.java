package ibs.steps;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;
import java.util.Properties;

public class DriverManager {

    static Properties props = new Properties();
    static WebDriver driver = new ChromeDriver();


    public void WebDriver() {
if ("remote".equalsIgnoreCase(props.getProperty("type.driver"))){
    initRemoteDriver();
} else{
    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
    driver.manage().window().maximize();
    driver.get("http://217.74.37.176/?route=account/register&language=ru-ru");
}
    }

    private static void initRemoteDriver() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserVersion", "109.0");
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", false);
        capabilities.setBrowserName(props.getProperty("type.browser", "chrome"));
        capabilities.setVersion("109.0");
        try {
            driver = new RemoteWebDriver(
                    URI.create(props.getProperty("selenoid.url")).toURL(),
                    capabilities);
        } catch (MalformedURLException e){
                throw new RuntimeException(e);
            }
        }
    }
