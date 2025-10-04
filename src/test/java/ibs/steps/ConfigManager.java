import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final Properties props = new Properties();
public boolean Selenoid;
    static {
        loadProperties();
    }
    private static void loadProperties() {
        try (InputStream input = ConfigManager.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input != null) {
                props.load(input);
                System.out.println("✅ application.properties загружен");
            }
        }catch (IOException e) {
            System.out.println("❌ Ошибка загрузки application.properties: " + e.getMessage());
        }
    }


    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    public static boolean isSelenoidMode() {
        String runMode = getProperty("run.mode");
        return "selenoid".equalsIgnoreCase(runMode.trim());
    }

    public static boolean isLocalMode() {
        return !isSelenoidMode();
    }
}