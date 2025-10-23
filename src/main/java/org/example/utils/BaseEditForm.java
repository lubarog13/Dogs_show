package org.example.utils;

import javax.swing.*;

public abstract class BaseEditForm extends BaseForm{
    protected JButton saveButton;
    protected JButton deleteButton;
    protected JButton cancelButton;
    public BaseEditForm() {
        super(600, 500, false);
    }

    public void initButtons() {
        saveButton.addActionListener(e -> saveClick());
        deleteButton.addActionListener(e -> deleteClick());
        cancelButton.addActionListener(e -> cancelClick());
    }

    abstract protected void baseInit();
    abstract protected void initFields();
    abstract protected void saveClick();
    abstract protected void deleteClick();


    protected void cancelClick() {
        dispose();
    }
}
