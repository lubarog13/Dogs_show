package org.example;
import org.example.ui.CompetitionAddForm;
import org.example.ui.DogAddForm;
import org.example.ui.DogsTableForm;
import javax.swing.*;

import org.example.ui.PersonAddForm;
import org.example.utils.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Главный класс программы для управления выставкой собак.
 * Инициализирует приложение и создает главное окно формы.
 */
public class Main {

    /**
     * Путь к файлу журнала событий приложения.
     */
    public static String LOG_PATH = "/tmp/log_dogs.txt";
    
    /**
     * Логгер для записи событий приложения.
     */
    public static Logger logger = new Logger(LOG_PATH);

    /**
     * Точка входа в приложение.
     * Устанавливает системный Look and Feel и создает главное окно формы.
     *
     * @param args аргументы командной строки (не используются)
     * @throws Exception если произошла ошибка при инициализации приложения
     */
        public static void main(String[] args) throws Exception {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new DogsTableForm();
        }

        /**
         * Получает соединение с базой данных MySQL.
         *
         * @return объект Connection для работы с базой данных, или null при ошибке
         */
        public static Connection getConnection() {
            try {
                return DriverManager.getConnection("jdbc:mysql://localhost:3306/dogs_show", "new_cybrid", "1234");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Закрывает соединение с базой данных.
         *
         * @param connection соединение с базой данных для закрытия
         * @throws SQLException если произошла ошибка при закрытии соединения
         */
        public static void closeConnection(Connection connection) throws SQLException {
            if (connection != null) {
                connection.close();
            }
        }
}
