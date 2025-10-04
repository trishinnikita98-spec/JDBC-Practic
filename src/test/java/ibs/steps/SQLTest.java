package ibs.steps;
import io.cucumber.java.After;
import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.И;
import org.junit.jupiter.api.*;
import java.sql.*;


import static org.junit.jupiter.api.Assertions.fail;


public class SQLTest {
    private Connection connection;
    private final String url = "jdbc:h2:tcp://qualit.applineselenoid.fvds.ru/mem:testdb";
    private final String user = "user";
    private final String password = "pass";

    private final String insert = "INSERT INTO FOOD(FOOD_NAME,FOOD_TYPE,FOOD_EXOTIC) values ('Дьявольский фрукт', 'Фрукт', 1)";
    private final String select = "SELECT * FROM FOOD WHERE FOOD_NAME = 'Дьявольский фрукт'";
    private final String delete = "DELETE FROM FOOD WHERE FOOD_NAME = 'Дьявольский фрукт'";
    private final String selectAll = "SELECT * FROM FOOD";

    @Дано("Подключить базу и удалить добавляемый товар \\(при наличии)")
    public void setUp() {
        try {
            // Сначала подключаемся
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Успешное соединение");

            // Теперь можно работать с connection
            try (Statement statement = connection.createStatement()) {
                // Удаляем тестовый товар
                int deletedRows = statement.executeUpdate(delete);
                if (deletedRows > 0) {
                    System.out.println("Удалено " + deletedRows + " тестовых товаров");
                }

                // Выводим содержимое таблицы
                try (ResultSet rs = statement.executeQuery(selectAll)) {
                    System.out.println("Текущие данные в таблице:");
                    while (rs.next()) {
                        int id = rs.getInt("FOOD_ID");
                        String name = rs.getString("FOOD_NAME");
                        String type = rs.getString("FOOD_TYPE");
                        int exotic = rs.getInt("FOOD_EXOTIC");
                        System.out.printf("ID: %d, Name: %s, Type: %s, Exotic: %d%n",
                                id, name, type, exotic);
                    }
                }
            }
        } catch (Exception e) {
            fail("Тест упал с исключением: " + e.getMessage());
        }
    }

    @И("Удаление добавленного товара")
    public void delete() {
        try (Statement statement = connection.createStatement()) {
            // Удаляем только тестовый товар
            int deletedRows = statement.executeUpdate(delete);
            if (deletedRows > 0) {
                System.out.println("Товар удален после теста");
            } else {
                System.out.println("Товар не найден для удаления после теста");
            }

            // Показываем финальное состояние таблицы
            try (ResultSet rs1 = statement.executeQuery(selectAll)) {
                System.out.println("Финальные данные в таблице:");
                System.out.println("----------------------------------------");

                while (rs1.next()) {
                    int id = rs1.getInt("FOOD_ID");
                    String name = rs1.getString("FOOD_NAME");
                    String type = rs1.getString("FOOD_TYPE");
                    int exotic = rs1.getInt("FOOD_EXOTIC");
                    System.out.printf("ID: %d, Name: %s, Type: %s, Exotic: %d%n",
                            id, name, type, exotic);
                }
                System.out.println("----------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Закрываем соединение
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (Exception e) {
                fail("Тест упал с исключением: " + e.getMessage());
            }
        }
    }

    @И("Отправить запрос с добавлением")
    public void AddTest() {
        try (Statement statement = connection.createStatement()) {
            // 1. Вставляем запись
            int affectedRows = statement.executeUpdate(insert);
            Assertions.assertEquals(1, affectedRows, "Должна быть добавлена одна запись");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Тест упал с исключением: " + e.getMessage());
        }
    }

    @И("Отправить запрос с проверкой изменений")
    public void AddTest2() {
        try (Statement statement = connection.createStatement()) {

            // 2. Проверяем что запись добавилась
            try (ResultSet resultSet = statement.executeQuery(select)) {
                System.out.println("Товар добавлен:");
                System.out.println("----------------------------------------");

                Assertions.assertTrue(resultSet.next(), "Товар должен быть найден в базе");

                int id = resultSet.getInt("FOOD_ID");
                String name = resultSet.getString("FOOD_NAME");
                String type = resultSet.getString("FOOD_TYPE");
                int exotic = resultSet.getInt("FOOD_EXOTIC");

                System.out.printf("ID: %d, Name: %s, Type: %s, Exotic: %d%n",
                        id, name, type, exotic);


                // Проверяем значения
                Assertions.assertEquals("Дьявольский фрукт", name);
                Assertions.assertEquals("Фрукт", type);
                Assertions.assertEquals(1, exotic);

                // Проверяем что больше нет записей с таким именем
                Assertions.assertFalse(resultSet.next(), "Должна быть только одна запись");
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Тест упал с исключением: " + e.getMessage());
        }
    }
    @After
    public void tearDown() {
        // Закрываем только SQL соединения, если нужно
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        AllDriverManager.closeDriver();
    }
}




