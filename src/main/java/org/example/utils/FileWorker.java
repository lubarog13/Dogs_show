package org.example.utils;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
     * 
     * @param path
     */
    public FileWorker(String path) {
        this.path = path;
        this.data = new ArrayList<>();
    }

    /**
     * Метод для чтения данных из файла
     * 
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
     * 
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
     * Метод для чтения данных из XML файла
     * 
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */

    public void parseXMLData() throws ParserConfigurationException, SAXException, IOException {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(new File(path));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("dog");
            data = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element element = (Element) node;
                String number = element.getElementsByTagName("number").item(0).getTextContent();
                String dogId = element.getElementsByTagName("dog_id").item(0).getTextContent();
                String name = element.getElementsByTagName("name").item(0).getTextContent();
                String breed = element.getElementsByTagName("breed").item(0).getTextContent();
                String ownerId = element.getElementsByTagName("owner_id").item(0).getTextContent();
                String judgeId = element.getElementsByTagName("judge_id").item(0).getTextContent();
                String ownerName = element.getElementsByTagName("owner_name").item(0).getTextContent();
                String judgeName = element.getElementsByTagName("judge_name").item(0).getTextContent();
                String place = element.getElementsByTagName("place").item(0).getTextContent();
                data.add(new String[] {number, dogId, name, breed, ownerId, judgeId, ownerName, judgeName, place});
            }
    }


    /**
     * Метод для записи данных в XML файл
     * 
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerException
     */
    public void writeXMLData() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = doc.createElement("dogs");
        doc.appendChild(root);
        for (String[] row : data) {
            Element dog = doc.createElement("dog");
            root.appendChild(dog);
            Element number = doc.createElement("id");
            number.appendChild(doc.createTextNode(row[0]));
            dog.appendChild(number);
            Element dogId = doc.createElement("dog_id");
            dogId.appendChild(doc.createTextNode(row[1]));
            dog.appendChild(dogId);
            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode(row[2]));
            dog.appendChild(name);
            Element breed = doc.createElement("breed");
            breed.appendChild(doc.createTextNode(row[3]));
            dog.appendChild(breed);
            Element owner = doc.createElement("owner_id");
            owner.appendChild(doc.createTextNode(row[4]));
            dog.appendChild(owner);
            Element judge = doc.createElement("judge_id");
            judge.appendChild(doc.createTextNode(row[5]));
            dog.appendChild(judge);
            Element ownerName = doc.createElement("owner_name");
            ownerName.appendChild(doc.createTextNode(row[6]));
            dog.appendChild(ownerName);
            Element judgeName = doc.createElement("judge_name");
            judgeName.appendChild(doc.createTextNode(row[7]));
            dog.appendChild(judgeName);
            Element place = doc.createElement("place");
            place.appendChild(doc.createTextNode(row[8]));
            dog.appendChild(place);
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(doc), new StreamResult(new File(path)));
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