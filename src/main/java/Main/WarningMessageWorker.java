package Main;

import dao.MessageService;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class WarningMessageWorker {

    public WarningMessageWorker(){}

    public List<WarningMessage_1C> getListOfWarningMessages(String warningMessagesPath) throws ParserConfigurationException, IOException, SAXException {

        List<WarningMessage_1C> warningMessagesList = new LinkedList<WarningMessage_1C>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        File file = new File(warningMessagesPath);
        Document document = builder.parse(file);
        Element element = document.getDocumentElement();
        NodeList messagesNodes = element.getChildNodes();

        for (int i = 0; i < messagesNodes.getLength(); i++) {
            Node messageNode = messagesNodes.item(i);

            if (messageNode instanceof Element) {
                WarningMessage_1C warningMessage1C = new WarningMessage_1C();

                NodeList messageDetails = messageNode.getChildNodes();

                for (int j = 0; j < messageDetails.getLength(); j++) {
                    Node detailNode = messageDetails.item(j);
                    if (detailNode instanceof Element) {
                        String detailName = detailNode.getNodeName();
                        String detailValue = detailNode.getTextContent();

                        if(detailName.equals("base"))
                            warningMessage1C.setBaseName(detailValue);
                        else if(detailName.equals("messagetext"))
                            warningMessage1C.setMessageText(detailValue);
                        else if(detailName.equals("tablename"))
                            warningMessage1C.setTableName(detailValue);
                    }
                }
                warningMessagesList.add(warningMessage1C);
            }
        }

        return warningMessagesList;
    }

    public void sendingWarningMessages(List<WarningMessage_1C> warningMessageList){
        for (WarningMessage_1C warningMessage1C: warningMessageList) {
            MessageService messageService = new MessageService(warningMessage1C.getBaseName());
            WarningMessage_1C currentMessage = messageService.getFirst(warningMessage1C.getTableName());
            messageService.deleteFirst(currentMessage);
            currentMessage.setMessageText(warningMessage1C.getMessageText());
            messageService.add(currentMessage);
            messageService.closeConnection();
        }
    }

    public void refreshBasesListFile(String filePath, List<WarningMessage_1C> warningMessagesList) throws ParserConfigurationException, TransformerException, FileNotFoundException {
        // Инициализируем фабрику и билдер
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Создаем документ и указываем версию
        Document document = builder.newDocument();
        document.setXmlVersion("1.0");
        // Создаем корневой элемент
        Element catalogEl = document.createElement("messages");
        document.appendChild(catalogEl);
        // Добавляем подузлы
        for (WarningMessage_1C warningMessage1C : warningMessagesList) {
            Element messageEl = document.createElement("message");
            catalogEl.appendChild(messageEl);
            appendDataNode(document, messageEl, "base", warningMessage1C.getBaseName());
            appendDataNode(document, messageEl, "messagetext", warningMessage1C.getMessageText());
            appendDataNode(document, messageEl, "tablename", warningMessage1C.getTableName());
        }

        // Создаем трансформер
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 4);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        // Оборачиваем докумен
        DOMSource src = new DOMSource(document);
        StreamResult res = new StreamResult(new FileOutputStream(filePath));
        // Создаем конечный XML
        transformer.transform(src, res);

    }

    private static void appendDataNode(Document document, Element parent, String tagName,
                                       String value) {
        Element element = document.createElement(tagName);
        element.appendChild(document.createTextNode(value));
        parent.appendChild(element);
    }
}
