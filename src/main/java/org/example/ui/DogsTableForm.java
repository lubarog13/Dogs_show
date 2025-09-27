package org.example.ui;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import org.example.utils.NoResults;
import org.example.utils.EmptySearchInput;

/**
 * Класс формы
 */
public class DogsTableForm extends JFrame {

    // Список элементов формы
    private JPanel mainPanel;
    private JTable dogsTable;
    private JTextField searchField;
    private JButton searchBtn;
    private JComboBox<String> dogBreedBox;
    private JComboBox<String> judgeBox;
    private JSlider placeSlider;
    private JButton applyButton;
    private JButton clearButton;
    private JLabel placeLabel;
    private DefaultTableModel model;

    // Данные для теста и заполнения таблицы
    private String[] columns = {"Номер", "Кличка", "Порода", "ФИО владельца", "Судья", "Занятое место"};
    private String[][] data = {{"1", "Тяпка", "Русская псовая борзая", "Иван Иванов", "Иван Сусанин", "1"},
            {"2", "Барбос", "Такса", "Петр Петров", "Григорий Васильев", "2"},
            {"3", "Шарик", "Немецкая овчарка", "Сергей Сидоров", "Сергей Петров", "3"},
            {"4", "Бобик", "Доберман", "Алексей Петров", "Алексей Сидоров", "4"},
            {"5", "Кнопка", "Такса", "Николай Николаев", "Сергей Петров", "1"}};

    // Заголовок окна
    public static String APP_TITLE = "Выставка собак";

    /**
     * Инициализация и размещения окна формы
     *
     * @param width
     * @param height
     */
    private void baseInit(int width, int height) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(width, height));
        setLocation(
                Toolkit.getDefaultToolkit().getScreenSize().width / 2 - width / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - height / 2
        );

        setTitle(APP_TITLE);
    }

    /**
     * Конструктор формы
     *
     * @param width
     * @param height
     */
    public DogsTableForm(int width, int height) {
        baseInit(width, height);
        setContentPane(mainPanel);
        initTable();
        setFilters();
        initListeners();
        setVisible(true);
    }

    /**
     * Инициализация данных в таблице, ее заполнение и оформление
     */
    private void initTable() {
        dogsTable.getTableHeader().setReorderingAllowed(false);
        dogsTable.setRowHeight(50);

        model = new DefaultTableModel(data, columns);
        dogsTable.setModel(model);
    }

    /**
     * Инициализация списков и слайдеров, заполнение их данными
     */
    private void setFilters() {
        Set<String> breeds = new HashSet<>();
        Set<String> judges = new HashSet<>();
        Set<String> places = new HashSet<>();
        for (String[] row : data) {
            breeds.add(row[2]);
            judges.add(row[4]);
            places.add(row[5]);
        }
        dogBreedBox.addItem("Все породы");
        judgeBox.addItem("Все судьи");
        dogBreedBox.setSelectedIndex(0);
        judgeBox.setSelectedIndex(0);
        breeds.forEach(dogBreedBox::addItem);
        judges.forEach(judgeBox::addItem);
        placeSlider.setMinimum(places.stream().mapToInt(Integer::parseInt).min().orElse(0));
        placeSlider.setMaximum(places.stream().mapToInt(Integer::parseInt).max().orElse(0));
        placeSlider.setValue(placeSlider.getMaximum());
        placeLabel.setText("До: " + placeSlider.getMaximum());
    }


    /**
     * Инициализация слушателей событий
     */
    private void initListeners() {
        // Слушатели для кнопок
        searchBtn.addActionListener(e -> {
            try {
                if (searchField.getText().equals("Введите текст....") || searchField.getText().isEmpty()) {
                    throw new EmptySearchInput();
                }
                applyFilters();
            } catch (EmptySearchInput err) {
                // Если введен пустой поисковый запрос, выводим сообщение об ошибке
                JOptionPane.showMessageDialog(null, err.getMessage(), "Внимание", JOptionPane.WARNING_MESSAGE);
            }
            catch (NoResults err) {
                // Если не найдено ничего, выводим сообщение об ошибке
                JOptionPane.showMessageDialog(null, err.getMessage(), "Внимание", JOptionPane.WARNING_MESSAGE);
            }
        });
        clearButton.addActionListener(e -> resetFilters());
        applyButton.addActionListener(e -> {
            try {
                applyFilters();
            } catch (NoResults err) {
                // Если не найдено ничего, выводим сообщение об ошибке
                JOptionPane.showMessageDialog(null, err.getMessage(), "Внимание", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Слушатель
        //  для слайдера
        placeSlider.addChangeListener(e -> {
            placeLabel.setText("До: " + placeSlider.getValue());
        });

        // Слушатель для поля поиска (фокус и потеря фокуса)
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Введите текст....")) {
                    searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Введите текст....");
                }
            }
        });

        // Слушатель для поля поиска (нажатие Enter)
        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    try {
                        if (searchField.getText().equals("Введите текст....") || searchField.getText().isEmpty()) {
                            throw new EmptySearchInput();
                        }
                        applyFilters();
                    }
                    catch (EmptySearchInput err) {
                        // Если введен пустой поисковый запрос, выводим сообщение об ошибке
                        JOptionPane.showMessageDialog(null, err.getMessage(), "Внимание", JOptionPane.WARNING_MESSAGE);
                    }
                     catch (NoResults err) {
                        // Если не найдено ничего, выводим сообщение об ошибке
                        JOptionPane.showMessageDialog(null, err.getMessage(), "Внимание", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    /**
     * Сброс фильтров
     */
    private void resetFilters() {
        searchField.setText("Введите текст....");
        dogBreedBox.setSelectedIndex(0);
        judgeBox.setSelectedIndex(0);
        placeSlider.setValue(placeSlider.getMaximum());
        try {
            applyFilters();
        } catch (NoResults err) {
            // Ничего не делаем
        }
    }


    /**
     * Применение фильтров
     * @throws NoResults
     */
    private void applyFilters() throws NoResults {
        // Получение данных из полей
        String searchText = searchField.getText().toLowerCase();
        String breed = (String) dogBreedBox.getSelectedItem();
        String judge = (String) judgeBox.getSelectedItem();
        int place = placeSlider.getValue();

        // Фильтрация данных
        List<String[]> filteredData = new ArrayList<>();
        for (String[] row : data) {
            if (!judge.equals("Все судьи") && !judge.equals(row[4])) {
                continue;
            }
            if (!breed.equals("Все породы") && !breed.equals(row[2])) {
                continue;
            }
            if (Integer.parseInt(row[5]) > place) {
                continue;
            }
            if (!searchText.isEmpty() && !searchText.equals("введите текст....") && !Objects.equals(row[0], searchText.trim()) && !row[1].toLowerCase().contains(searchText) && !row[2].toLowerCase().contains(searchText) && !row[3].toLowerCase().contains(searchText) && !row[4].toLowerCase().contains(searchText)) {
                continue;
            }
            filteredData.add(row);
        }

        if (filteredData.isEmpty()) {
            model.setDataVector(new String[0][], columns);
            throw new NoResults();
        }

        // Заполнение модели данными
        model.setDataVector(filteredData.toArray(new String[0][]), columns);

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, -1, 28, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Выставка собак");
        panel1.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        searchField = new JTextField();
        searchField.setText("Введите текст....");
        searchField.setToolTipText("");
        panel2.add(searchField, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        searchBtn = new JButton();
        searchBtn.setText("Поиск");
        panel2.add(searchBtn, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        dogBreedBox = new JComboBox();
        panel3.add(dogBreedBox, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Порода собаки");
        panel3.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        judgeBox = new JComboBox();
        panel3.add(judgeBox, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Судья");
        panel3.add(label3, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Занятое место");
        panel3.add(label4, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        applyButton = new JButton();
        applyButton.setText("Применить");
        panel3.add(applyButton, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        clearButton = new JButton();
        clearButton.setText("Сбросить");
        panel3.add(clearButton, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        placeSlider = new JSlider();
        placeSlider.setMinimum(1);
        placeSlider.setPaintLabels(true);
        placeSlider.setPaintTicks(true);
        placeSlider.setValueIsAdjusting(false);
        panel4.add(placeSlider, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        placeLabel = new JLabel();
        placeLabel.setText("До:");
        panel4.add(placeLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, new Dimension(-1, 400), 0, false));
        dogsTable = new JTable();
        scrollPane1.setViewportView(dogsTable);
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(50, 50), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
