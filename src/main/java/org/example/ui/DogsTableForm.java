package org.example.ui;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.example.model.Dog;
import org.example.model.Person;

import org.example.model.Competition;
import org.example.utils.*;
import org.xml.sax.SAXException;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Класс формы
 */
public class DogsTableForm extends BaseForm {

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
    private JButton loadButton;
    private JButton downloadButton;
    private List<Competition> competitions = new ArrayList<>();
    private DefaultTableModel model;

    // Данные для теста и заполнения таблицы
    private String[] columns = {"Номер", "Кличка", "Порода", "ФИО владельца", "Судья", "Занятое место"};
    private String[][] data = new String[0][];
    private List<String[]> filteredData = new ArrayList<>(Arrays.asList(data));

    // Заголовок окна
    public static String APP_TITLE = "Выставка собак";


    /**
     *
     */
    public DogsTableForm() {
        super(700, 500, true);
        setContentPane(mainPanel);
        initData();
        initTable();
        setFilters();
        initListeners();
        setVisible(true);
    }

    /**
     * Инициализация данных
     */
    private void initData() {
        try {
            this.competitions = DbManager.getCompetitions();
            data = new String[competitions.size()][columns.length];
            for (int i = 0; i < competitions.size(); i++) {
                data[i][0] = String.valueOf(competitions.get(i).getId());
                data[i][1] = competitions.get(i).getDogName();
                data[i][2] = competitions.get(i).getDogBreed();
                data[i][3] = competitions.get(i).getOwnerName();
                data[i][4] = competitions.get(i).getJudgeName();
                data[i][5] = String.valueOf(competitions.get(i).getPlace());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Произошла ошибка при загрузке данных: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
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
        dogBreedBox.removeAllItems();
        judgeBox.removeAllItems();
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
            } catch (NoResults err) {
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

        loadButton.addActionListener(e -> loadDataFromFile());
        downloadButton.addActionListener(e -> downloadDataToFile());

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
                    } catch (EmptySearchInput err) {
                        // Если введен пустой поисковый запрос, выводим сообщение об ошибке
                        JOptionPane.showMessageDialog(null, err.getMessage(), "Внимание", JOptionPane.WARNING_MESSAGE);
                    } catch (NoResults err) {
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
        // Добавляем слушатель двойного клика по строке таблицы
        dogsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && dogsTable.getSelectedRow() != -1) {
                    int row = dogsTable.getSelectedRow();
                    String[] dogData = data[row];
                    Competition competition = competitions.stream().filter(c -> c.getId() == Integer.parseInt(dogData[0])).findFirst().orElse(null);
                    if (competition != null) {
                        try {
                            Dog dog = DbManager.getDog(competition.getDogId());
                            Person judge = DbManager.getJudge(competition.getJudgeId());
                            new CompetitionAddForm(competition.getPlace(), dog, judge, competition.getId());
                        } catch (SQLException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Произошла ошибка при загрузке данных о собаке или судье: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
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
     *
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
            this.filteredData = new ArrayList<>();
            throw new NoResults();
        }

        // Заполнение модели данными
        model.setDataVector(filteredData.toArray(new String[0][]), columns);
        this.filteredData = filteredData;

    }

    /**
     * Метод для загрузки данных из файла
     */
    private void loadDataFromFile() {
        FileDialog fileDialog = new FileDialog(this, "Выберите файл", FileDialog.LOAD);
        fileDialog.setVisible(true);
        String path = fileDialog.getDirectory() + fileDialog.getFile();
        if (!path.equals("nullnull")) {
            Object mutex = new Object();
            Thread[] threads = new Thread[2];
            AtomicBoolean loaded = new AtomicBoolean(false);
            AtomicBoolean loadedError = new AtomicBoolean(false);
            threads[0] = new Thread(() -> {
                synchronized (mutex) {
                    try {
                        FileWorker fileWorker = new FileWorker(path);
                        fileWorker.parseXMLData();
                        data = fileWorker.getData().toArray(new String[0][]);
                        model.setDataVector(data, columns);
                        setFilters();
                        loaded.set(true);
                        mutex.notifyAll();
                    } catch (IOException | SAXException e) {
                        loadedError.set(true);
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(null, "Ошибка при загрузке данных из файла", "Внимание", JOptionPane.WARNING_MESSAGE)
                        );
                        mutex.notifyAll();
                    } catch (IllegalArgumentException | ParserConfigurationException e) {
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(null, "Неверный формат данных в файле\n Требуется XML файл", "Внимание", JOptionPane.WARNING_MESSAGE)
                        );
                        loadedError.set(true);
                        mutex.notifyAll();
                    }
                }
                if (loaded.get()) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(null, "Данные из файла " + path + " успешно загружены", "Данные загружены", JOptionPane.INFORMATION_MESSAGE)
                    );
                }


            });
            threads[1] = new Thread(() -> {
                synchronized (mutex) {
                    Logger.log("Загружаю данные из файла " + path);
                    while (!loaded.get() && !loadedError.get()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (loadedError.get()) {
                        Logger.log("Ошибка при загрузке данных из файла " + path);
                        mutex.notifyAll();
                    } else {
                        Logger.log("Данные из файла " + path + " успешно загружены");
                        mutex.notifyAll();
                    }
                }
            });
            threads[1].setPriority(Thread.MIN_PRIORITY);
            threads[0].start();
            threads[1].start();
            try {
                threads[0].join();
                threads[1].join();
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(null, "Ошибка при загрузке данных из файла", "Внимание", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Метод для сохранения данных в файл
     */
    private void downloadDataToFile() {

        FileDialog fileDialog = new FileDialog(this, "Выберите файл", FileDialog.SAVE);
        fileDialog.setVisible(true);
        String path = fileDialog.getDirectory() + fileDialog.getFile();
        if (path.equals("nullnull")) {
            return;
        }
        Object mutex = new Object();
        Thread[] threads = new Thread[2];
        AtomicBoolean loaded = new AtomicBoolean(false);
        AtomicBoolean loadedError = new AtomicBoolean(false);
        threads[0] = new Thread(() -> {
            synchronized (mutex) {
                try {
                    FileWorker fileWorker = new FileWorker(path);
                    fileWorker.setData(filteredData);
                    fileWorker.writeXMLData();
                    loaded.set(true);
                    mutex.notifyAll();
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(null, "Данные в файл " + path + " успешно сохранены", "Данные сохранены", JOptionPane.INFORMATION_MESSAGE)
                    );
                } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
                    loadedError.set(true);
                    mutex.notifyAll();
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(null, "Ошибка при сохранении данных в файл", "Внимание", JOptionPane.WARNING_MESSAGE)
                    );
                }
            }
        });
        threads[1] = new Thread(() -> {
            synchronized (mutex) {
                Logger.log("Сохраняю данные в файл " + path);
                while (!loaded.get() && !loadedError.get()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (loadedError.get()) {
                    Logger.log("Ошибка при сохранении данных в файл " + path);
                    mutex.notifyAll();
                } else {
                    Logger.log("Данные в файл " + path + " успешно сохранены");
                    mutex.notifyAll();
                }
            }
        });
        threads[1].setPriority(Thread.MIN_PRIORITY);
        threads[0].start();
        threads[1].start();
        try {
            threads[0].join();
            threads[1].join();
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(null, "Ошибка при сохранении данных в файл", "Внимание", JOptionPane.WARNING_MESSAGE);
        }
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
        mainPanel.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, -1, 28, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Выставка собак");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        searchField = new JTextField();
        searchField.setText("Введите текст....");
        searchField.setToolTipText("");
        panel2.add(searchField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        searchBtn = new JButton();
        searchBtn.setText("Поиск");
        panel2.add(searchBtn, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        dogBreedBox = new JComboBox();
        panel3.add(dogBreedBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Порода собаки");
        panel3.add(label2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        judgeBox = new JComboBox();
        panel3.add(judgeBox, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Судья");
        panel3.add(label3, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Занятое место");
        panel3.add(label4, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        applyButton = new JButton();
        applyButton.setText("Применить");
        panel3.add(applyButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        clearButton = new JButton();
        clearButton.setText("Сбросить");
        panel3.add(clearButton, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        placeSlider = new JSlider();
        placeSlider.setMinimum(1);
        placeSlider.setPaintLabels(true);
        placeSlider.setPaintTicks(true);
        placeSlider.setValueIsAdjusting(false);
        panel4.add(placeSlider, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        placeLabel = new JLabel();
        placeLabel.setText("До:");
        panel4.add(placeLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 50), null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, new Dimension(-1, 400), 0, false));
        dogsTable = new JTable();
        scrollPane1.setViewportView(dogsTable);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        loadButton = new JButton();
        loadButton.setText("Загрузить данные");
        panel5.add(loadButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        downloadButton = new JButton();
        downloadButton.setText("Скачать таблицу");
        panel5.add(downloadButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(50, 50), null, 0, false));
        final Spacer spacer3 = new Spacer();
        mainPanel.add(spacer3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        mainPanel.add(spacer4, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        mainPanel.add(spacer5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
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
