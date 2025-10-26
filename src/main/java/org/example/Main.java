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
 * Главный класс программы, вызов окна формы
 */
public class Main {

    public static String LOG_PATH = "/tmp/log_dogs.txt";
    public static Logger logger = new Logger(LOG_PATH);

    /**
     * @param args
     * @throws Exception
     */
        public static void main(String[] args) throws Exception {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new DogsTableForm();
        }

        public static Connection getConnection() {
            try {
                return DriverManager.getConnection("jdbc:mysql://localhost:3306/dogs_show", "new_cybrid", "1234");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
}
