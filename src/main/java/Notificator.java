import com._1c.v8.ibis.admin.IClusterInfo;
import com._1c.v8.ibis.admin.IInfoBaseInfo;
import com._1c.v8.ibis.admin.IInfoBaseInfoShort;
import com._1c.v8.ibis.admin.client.AgentAdminConnectorFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Notificator {

    private String basesPath;
    private Util_1C util1C;
    private String adressOfRES;
    private int portOfRES;


    Notificator(String basesPath){
        this.basesPath = basesPath;
        this.adressOfRES = "localhost";
        this.portOfRES = 1545;
    }

    List<Base_1C> getListOfBases() throws ParserConfigurationException, IOException, SAXException {

        List<Base_1C> baseList = new LinkedList<Base_1C>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        File file = new File(basesPath);
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

    List<Base_1C> checkingOfBases(List<Base_1C> commonListOfBases){

        util1C = new Util_1C(basesPath, new AgentAdminConnectorFactory());
        List<Base_1C> warningBasesList = new LinkedList<Base_1C>();

        util1C.connect(adressOfRES, portOfRES, 0);

        List<IClusterInfo> clusterList = util1C.getClusterInfoList();

        IClusterInfo clusterInfo = clusterList.get(0);

        if (clusterInfo != null){

            UUID uuid = clusterInfo.getClusterId();
            util1C.authenticateCluster(uuid, "", "");
            List<IInfoBaseInfoShort> infoBaseInfoShort = util1C.getInfoBaseShortInfos(uuid);

            for (int i = 0; i < commonListOfBases.size(); i++) {
                Base_1C currentBase = commonListOfBases.get(i);
                String baseName = currentBase.getName();

                for (int j = 0; j < infoBaseInfoShort.size(); j++) {
                    if(infoBaseInfoShort.get(j).getName().equals(baseName)){
                        util1C.addInfoBaseCredentials(uuid, commonListOfBases.get(i).getUser(), commonListOfBases.get(i).getPassword());
                        IInfoBaseInfo iInfoBaseInfo = util1C.getInfoBaseInfo(uuid, infoBaseInfoShort.get(j).getInfoBaseId());
                        if (iInfoBaseInfo.isSessionsDenied()){
                            warningBasesList.add(currentBase);
                        }
                    }
                }
            }
        }

        util1C.disconnect();

        return warningBasesList;

    }
}
