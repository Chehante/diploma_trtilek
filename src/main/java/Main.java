import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        List basesList = getListOfBases("list bases.xml");

    }

    public static List<Base_1C> getListOfBases(String filePath) throws ParserConfigurationException, IOException, SAXException {

        List<Base_1C> baseList = new LinkedList<Base_1C>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        File file = new File(filePath);
        Boolean exists = file.exists();
        Document document = builder.parse(file);
        Element element = document.getDocumentElement();
        NodeList basesNodes = element.getChildNodes();

        for (int i = 0; i < basesNodes.getLength(); i++) {
            Node baseNode = basesNodes.item(i);

            if (baseNode instanceof Element) {
                Base_1C base = new Base_1C();

                NamedNodeMap attrs = baseNode.getAttributes();

                base.setName(attrs.getNamedItem("name").getNodeValue());

                NodeList baseDetails = baseNode.getChildNodes();

                for (int j = 0; j < baseDetails.getLength(); j++) {
                    Node detailNode = baseDetails.item(j);
                    if (detailNode instanceof Element) {
                        String detailName = detailNode.getNodeName();
                        String detailValue = detailNode.getTextContent();

                        if(detailName.equals("user"))
                            base.setUser(detailValue);
                        else if(detailName.equals("pass"))
                            base.setPassword(detailValue);
                    }
                }
                baseList.add(base);
            }
        }

        return baseList;
    }
}
