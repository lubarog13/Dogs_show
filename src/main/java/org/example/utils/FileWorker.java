package org.example.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Класс для работы с файлами
 */
public class FileWorker {

    /**
     * Путь к файлу
     */
    private String path;
    /**
     * Данные
     */
    private List<String[]> data;
    /**
     * Конструктор класса
     * @param path
     */
    public FileWorker(String path) {
        this.path = path;
        this.data = new ArrayList<>();
    }

    /**
     * Метод для чтения данных из файла
     * @throws FileNotFoundException
     * @throws IllegalArgumentException
     */
    public void readDataFromFile() throws FileNotFoundException, IllegalArgumentException {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] row = line.split(";");
                if (row.length != 6) {
                    scanner.close();
                    throw new IllegalArgumentException("Неверный формат данных в файле");
                }
                data.add(row);
            }
            scanner.close();
    }

    /**
     * Метод для записи данных в файл
     * @throws FileNotFoundException
     */
    public void writeDataToFile() throws FileNotFoundException {
        File file = new File(path);
        PrintWriter writer = new PrintWriter(file);
        for (String[] row : data) {
            writer.println(String.join(";", row));
        }
        writer.close();
    }


    /**
     * @return
     */
    public List<String[]> getData() {
        return data;
    }

    /**
     * @param data
     */
    public void setData(List<String[]> data) {
        this.data = data;
    }

    /**
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     *
     * @param path 
     */
    public void setPath(String path) {
        this.path = path;
    }
    
}