package org.example;
import org.example.ui.DogsTableForm;
import javax.swing.*;

/**
 * Главный класс программы, вызов окна формы
 */
public class Main {

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

            new DogsTableForm(700, 500);
        }
}
