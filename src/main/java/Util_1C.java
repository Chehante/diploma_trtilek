import com._1c.v8.ibis.admin.*;
import com._1c.v8.ibis.admin.client.IAgentAdminConnector;
import com._1c.v8.ibis.admin.client.IAgentAdminConnectorFactory;
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

public class Util_1C {

    private String basesPath;
    private String adressOfRES;
    private int portOfRES;
    private final IAgentAdminConnectorFactory factory;
    private IAgentAdminConnector connector;
    private IAgentAdminConnection connection;

    public Util_1C(String basesPath, IAgentAdminConnectorFactory factory){
        this.basesPath = basesPath;
        this.factory = factory;
        this.adressOfRES = "localhost";
        this.portOfRES = 1545;
    }

    public List<Base_1C> getListOfBases() throws ParserConfigurationException, IOException, SAXException {

        List<Base_1C> baseList = new LinkedList<Base_1C>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        File file = new File(basesPath);
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

    public List<Base_1C> checkingOfBases(List<Base_1C> commonListOfBases){

        List<Base_1C> warningBasesList = new LinkedList<Base_1C>();

        connect(adressOfRES, portOfRES, 0);

        List<IClusterInfo> clusterList = getClusterInfoList();

        IClusterInfo clusterInfo = clusterList.get(0);

        if (clusterInfo != null){

            UUID uuid = clusterInfo.getClusterId();
            authenticateCluster(uuid, "", "");
            List<IInfoBaseInfoShort> infoBaseInfoShort = getInfoBaseShortInfos(uuid);

            for (int i = 0; i < commonListOfBases.size(); i++) {
                Base_1C currentBase = commonListOfBases.get(i);
                String baseName = currentBase.getName();

                for (int j = 0; j < infoBaseInfoShort.size(); j++) {
                    if(infoBaseInfoShort.get(j).getName().equals(baseName)){
                        addInfoBaseCredentials(uuid, commonListOfBases.get(i).getUser(), commonListOfBases.get(i).getPassword());
                        IInfoBaseInfo iInfoBaseInfo = getInfoBaseInfo(uuid, infoBaseInfoShort.get(0).getInfoBaseId());
                        if (iInfoBaseInfo.isSessionsDenied()){
                            warningBasesList.add(currentBase);
                        }
                    }
                }
            }
        }

        return warningBasesList;

    }

    public void connect(String address, int port, long timeout) {
        if (connection != null)
        {
            throw new IllegalStateException("The connection is already established.");
        }

        connector = factory.createConnector(timeout);
        connection = connector.connect(address, port);
    }


    public void authenticateCluster(UUID clusterId, String userName, String password)
    {
        if (connection == null)
        {
            throw new IllegalStateException("The connection is not established.");
        }

        connection.authenticate(clusterId, userName, password);
    }

    public void disconnect(){
        if (connection == null)
        {
            throw new IllegalStateException("The connection is not established.");
        }

        try
        {
            connector.shutdown();
        }
        finally
        {
            connection = null;
            connector = null;
        }
    }

    public List<IClusterInfo> getClusterInfoList(){
        if (connection == null)
        {
            throw new IllegalStateException("The connection is not established.");
        }

        return connection.getClusters();
    }

    public List<IInfoBaseInfoShort> getInfoBaseShortInfos(UUID clusterId){
        if (connection == null)
        {
            throw new IllegalStateException("The connection is not established.");
        }

        return connection.getInfoBasesShort(clusterId);
    }

    public IInfoBaseInfo getInfoBaseInfo(UUID clusterId, UUID infoBaseId){
        if (connection == null)
        {
            throw new IllegalStateException("The connection is not established.");
        }

        return connection.getInfoBaseInfo(clusterId, infoBaseId);
    }

    public void addInfoBaseCredentials(UUID clusterId, String userName,
                                       String password){
        if (connection == null)
        {
            throw new IllegalStateException("The connection is not established.");
        }

        connection.addAuthentication(clusterId, userName, password);
    }

    /**
     * Updates infobase parameters
     *
     * @param clusterId cluster ID
     * @param info infobase parameters
     */
    public void updateInfoBase(UUID clusterId, IInfoBaseInfo info){
        if (connection == null)
        {
            throw new IllegalStateException("The connection is not established.");
        }

        connection.updateInfoBase(clusterId, info);
    }
}
