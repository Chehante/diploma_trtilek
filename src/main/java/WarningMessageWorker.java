import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class WarningMessageWorker {

    private String warningMessagesPath;

    public WarningMessageWorker(String warningMessagesPath){
        this.warningMessagesPath = warningMessagesPath;
    }

    List<WarningMessage_1C> getListOfWarningMessages() throws ParserConfigurationException, IOException, SAXException {

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
                    }
                }
                warningMessagesList.add(warningMessage1C);
            }
        }

        return warningMessagesList;
    }

    public void sendingWarningMessages(List<WarningMessage_1C> warningMessageList){
        for (WarningMessage_1C warningMessage1C: warningMessageList) {
            Connection connection = Util_SQL.getConnect(warningMessage1C.getBaseName());
            try {
                String sql = "insert into _reference21 (_idrref, _code, _description, _marked, _predefinedid) values (bytea('\\xbf582016d8a2186d11e7f2d40514a6ef'), mvarchar('000000004'), mvarchar('Тест4'), ?, bytea('\\x00000000000000000000000000000000'))";
                PreparedStatement prepSt = connection.prepareStatement(sql);
                prepSt.setBoolean(1, false);
                prepSt.execute();

            } catch (SQLException e) {
                System.out.println("Failed to create new statement connection");
                e.printStackTrace();
            }
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Failed to close connection");
                e.printStackTrace();
            }
        }
    }

    public void clearWarningMessage(){

    }
}
