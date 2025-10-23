package org.example;
import org.example.ui.CompetitionAddForm;
import org.example.ui.DogAddForm;
import org.example.ui.DogsTableForm;
import javax.swing.*;

import org.example.ui.PersonAddForm;
import org.example.utils.Logger;
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

            new CompetitionAddForm();
        }
}
