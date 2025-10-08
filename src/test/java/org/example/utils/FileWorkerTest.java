package org.example.utils;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс тестов для записи и чтения XML-файла
 */
class FileWorkerTest {

    File successFile;
    FileWorker fileWorker;

    /**
     * Метод получения данных файла
     * @return Данные_для_файла
     */
    String getSuccessData() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<dogs>\n" +
                "    <dog>\n" +
                "        <number>1</number>\n" +
                "        <name>Собака 1</name>\n" +
                "        <breed>Китайская хохлатая</breed>\n" +
                "        <owner>Хозяйка 1</owner>\n" +
                "        <judge>Главный судья 1</judge>\n" +
                "        <place>1</place>\n" +
                "    </dog>\n" +
                "    <dog>\n" +
                "        <number>2</number>\n" +
                "        <name>Собака 2</name>\n" +
                "        <breed>Такса</breed>\n" +
                "        <owner>Хозяйка 2</owner>\n" +
                "        <judge>Главный судья 2</judge>\n" +
                "        <place>2</place>\n" +
                "    </dog>\n" +
                "    <dog>\n" +
                "        <number>3</number>\n" +
                "        <name>Собака 3</name>\n" +
                "        <breed>Питбуль</breed>\n" +
                "        <owner>Хозяйка 3</owner>\n" +
                "        <judge>Главный судья 3</judge>\n" +
                "        <place>3</place>\n" +
                "    </dog>\n" +
                "</dogs>\n";
        
    }


    /**
     *  Создание корректного файла
     */
    void createSuccessFile() {
        successFile = new File("test.xml");
        try {
            successFile.createNewFile();
            FileWriter fileWriter = new FileWriter(successFile);
            String xmlContent = getSuccessData();
            fileWriter.write(xmlContent);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  Установка начальных данных
     */
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        createSuccessFile();
        fileWorker = new FileWorker("test.xml");
    }

    /**
     *  Удаление файла
     */
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        successFile.delete();
    }

    /**
     *  Тест для чтения данных из XML файла
     */
    @org.junit.jupiter.api.Test
    void parseXMLData() {
        try {
            fileWorker.parseXMLData();
        } catch (IOException | SAXException | ParserConfigurationException e) {
            fail(e.getMessage());
        }
        assertEquals(3, fileWorker.getData().size());
        assertEquals("1", fileWorker.getData().getFirst()[0]);
        assertEquals("Собака 1", fileWorker.getData().getFirst()[1]);
        assertEquals("Китайская хохлатая", fileWorker.getData().getFirst()[2]);
        assertEquals("Хозяйка 1", fileWorker.getData().getFirst()[3]);
        assertEquals("Главный судья 1", fileWorker.getData().getFirst()[4]);
        assertEquals("1", fileWorker.getData().getFirst()[5]);
        fileWorker.setData(new ArrayList<>());
    }

    /**
     *  Тест для записи данных в XML файл
     */
    @org.junit.jupiter.api.Test
    void writeXMLData() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[] { "4", "Собака 4", "Доберман", "Хозяйка 4", "Главный судья 4", "4" });
        fileWorker.setData(data);
        try {
            fileWorker.writeXMLData();
            fileWorker.parseXMLData();
        } catch (IOException | SAXException | ParserConfigurationException | TransformerException e) {
            fail(e.getMessage());
        }
        assertEquals(1, fileWorker.getData().size());
        assertEquals("4", fileWorker.getData().getFirst()[0]);
        assertEquals("Собака 4", fileWorker.getData().getFirst()[1]);
        assertEquals("Доберман", fileWorker.getData().getFirst()[2]);
        assertEquals("Хозяйка 4", fileWorker.getData().getFirst()[3]);
        assertEquals("Главный судья 4", fileWorker.getData().getFirst()[4]);
        assertEquals("4", fileWorker.getData().getFirst()[5]);
    }
}