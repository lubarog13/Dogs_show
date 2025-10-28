package org.example.ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import org.example.Main;
import org.example.model.Competition;
import org.example.model.Dog;
import org.example.utils.BaseEditForm;
import org.example.model.Person;
import org.example.utils.DbManager;
import java.sql.SQLException;   
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

public class CompetitionAddForm extends BaseEditForm {
    private JPanel panel1;
    private JLabel titleLabel;

    private JButton saveButton;
    private JButton deleteButton;
    private JButton cancelButton;
    private JComboBox<String> dogBox;
    private JComboBox<String> judgeBox;
    private JButton addJudgeBtn;
    private JSpinner placeSpinner;
    private JButton addDogBtn;
    private JButton editDog;
    private JButton editJudge;

    private Person judge;
    private Dog dog;
    private int place;
    private int id;
    private List<Dog> dogs = new ArrayList<>();
    private List<Person> judges = new ArrayList<>();

    public CompetitionAddForm() {
        super();
        setContentPane(panel1);
        baseInit();
    }

    public CompetitionAddForm(int place, Dog dog, Person judge, int id) {
        this.dog = dog;
        this.judge = judge;
        this.place = place;
        this.id = id;
        super();
        titleLabel.setText("Редактирование соревнования");
        baseInit();
    }

    @Override
    protected void cancelClick() {
        super.cancelClick();
        new DogsTableForm();
    }
    

    @Override
    protected void baseInit() {
        setContentPane(panel1);
        deleteButton.setVisible(true);
        super.saveButton = this.saveButton;
        super.cancelButton = this.cancelButton;
        super.deleteButton = this.deleteButton;
        initButtons();
        initFields();
        setVisible(true);
    }

    @Override
    public void reload() {
        initDogs();
        initJudges();
    }

    private void initJudges() {
        Person selectedJudge = null;
        if (this.judge != null) {
            selectedJudge = this.judge;
        }
        try {
            judges = DbManager.getJudges();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Произошла ошибка при загрузке судей: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
        judgeBox.removeAllItems();
        judges.forEach(judge -> judgeBox.addItem(judge.toString()));
        if (selectedJudge != null) {
            judgeBox.setSelectedItem(selectedJudge.toString());
        } else if (!judges.isEmpty()) {
            selectedJudge = judges.getFirst();
            judgeBox.setSelectedItem(selectedJudge.toString());
        }
        judgeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                judge = judges.stream().filter(judge -> judge.toString().equals(judgeBox.getSelectedItem())).findFirst().orElse(null);
            }
        });
    }

    private void initDogs() {
        Dog selectedDog = null;
        if (this.dog != null) {
            selectedDog = this.dog;
        }
        try {
            dogs = DbManager.getDogs();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Произошла ошибка при загрузке собак: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
        dogBox.removeAllItems();
        dogs.forEach(dog -> dogBox.addItem(dog.toString()));
        if (selectedDog != null) {
            dogBox.setSelectedItem(selectedDog.toString());
        } else if (!dogs.isEmpty()) {
            selectedDog = dogs.getFirst();
            dogBox.setSelectedItem(selectedDog.toString());
        }
        dogBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dog = dogs.stream().filter(dog -> dog.toString().equals(dogBox.getSelectedItem())).findFirst().orElse(null);
            }
        });
    }

    @Override
    protected void initFields() {
        initJudges();
        initDogs();
        if (this.place != 0) {
            placeSpinner.setValue(this.place);
        }
        if (this.dog != null) {
            dogBox.setSelectedItem(this.dog.toString());
        } else if (!dogs.isEmpty()) {
            this.dog = dogs.getFirst();
            dogBox.setSelectedItem(this.dog.toString());
        }
        if (this.judge != null) {
            judgeBox.setSelectedItem(this.judge.toString());
        } else if (!judges.isEmpty()) {
            this.judge = judges.getFirst();
            judgeBox.setSelectedItem(this.judge.toString());
        }
        addJudgeBtn.addActionListener(e -> addJudge());
        addDogBtn.addActionListener(e -> addDog());
        editDog.addActionListener(e -> editDog());
        editJudge.addActionListener(e -> editJudge());
    }

    private void addJudge() {
        new PersonAddForm("judge", this);
    }

    private void addDog() {
        new DogAddForm(this);
    }

    private void editDog() {
        System.out.println("editDog");
        new DogAddForm(this.dog, this);
    }

    private void editJudge() {
        new PersonAddForm(this.judge, this);
    }

    @Override
    protected void saveClick() {
        try {
            this.place = (int) placeSpinner.getValue();
            if (this.place == 0) {
                JOptionPane.showMessageDialog(null, "Необходимо выбрать занятое место", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (this.dog == null) {
                JOptionPane.showMessageDialog(null, "Необходимо выбрать собаку", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (this.judge == null) {
                JOptionPane.showMessageDialog(null, "Необходимо выбрать судью", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (this.id == 0) {
                DbManager.addCompetition(new Competition(0, this.place, this.dog.getId(), this.judge.getId(), this.dog.getOwner().getId(), this.dog.getName(), this.dog.getBreed(), this.dog.getOwner().getName() + " " + this.dog.getOwner().getSurname() + " " + this.dog.getOwner().getMiddlename(), this.judge.getName() + " " + this.judge.getSurname() + " " + this.judge.getMiddlename()));
                JOptionPane.showMessageDialog(null, "Соревнование успешно добавлено", "Успешно", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new DogsTableForm();
            } else {
                DbManager.updateCompetition(new Competition(this.id, this.place, this.dog.getId(), this.judge.getId(), this.dog.getOwner().getId(), this.dog.getName(), this.dog.getBreed(), this.dog.getOwner().getName() + " " + this.dog.getOwner().getSurname() + " " + this.dog.getOwner().getMiddlename(), this.judge.getName() + " " + this.judge.getSurname() + " " + this.judge.getMiddlename()));
                JOptionPane.showMessageDialog(null, "Соревнование успешно обновлено", "Успешно", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new DogsTableForm();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Произошла ошибка при сохранении соревнования: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void deleteClick() {
        try {
            DbManager.deleteCompetition(this.id);
            JOptionPane.showMessageDialog(null, "Соревнование успешно удалено", "Успешно", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new DogsTableForm();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Произошла ошибка при удалении соревнования: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
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
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        titleLabel = new JLabel();
        Font titleLabelFont = this.$$$getFont$$$(null, -1, 28, titleLabel.getFont());
        if (titleLabelFont != null) titleLabel.setFont(titleLabelFont);
        titleLabel.setText("Добавление  соревнования");
        panel2.add(titleLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Судья");
        panel3.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        saveButton = new JButton();
        saveButton.setText("Сохранить");
        panel4.add(saveButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteButton = new JButton();
        deleteButton.setText("Удалить");
        panel4.add(deleteButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cancelButton = new JButton();
        cancelButton.setText("Отменить");
        panel4.add(cancelButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel5, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        judgeBox = new JComboBox();
        panel5.add(judgeBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addJudgeBtn = new JButton();
        addJudgeBtn.setText("Добавить нового");
        panel5.add(addJudgeBtn, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        editJudge = new JButton();
        editJudge.setText("Изменить");
        panel5.add(editJudge, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        placeSpinner = new JSpinner();
        panel3.add(placeSpinner, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Занятое место");
        panel3.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel6, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        dogBox = new JComboBox();
        panel6.add(dogBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addDogBtn = new JButton();
        addDogBtn.setText("Добавить другую");
        panel6.add(addDogBtn, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        editDog = new JButton();
        editDog.setText("Изменить");
        panel6.add(editDog, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Собака");
        panel3.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel1.add(spacer4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel1.add(spacer5, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel1.add(spacer6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
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
        return panel1;
    }

}
