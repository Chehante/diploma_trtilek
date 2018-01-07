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
    private final IAgentAdminConnectorFactory factory;
    private IAgentAdminConnector connector;
    private IAgentAdminConnection connection;

    public Util_1C(String basesPath, IAgentAdminConnectorFactory factory){
        this.factory = factory;
        this.basesPath = basesPath;

    }

    public void connect(String address, int port, long timeout) {
        if (connection != null)
        {
            throw new IllegalStateException("The connection is already established.");
        }

        connector = factory.createConnector(timeout);
        connection = connector.connect(address, port);
    }

    public void authenticateCluster(UUID clusterId, String userName, String password){
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

    public void updateInfoBase(UUID clusterId, IInfoBaseInfo info){
        if (connection == null)
        {
            throw new IllegalStateException("The connection is not established.");
        }

        connection.updateInfoBase(clusterId, info);
    }
}
