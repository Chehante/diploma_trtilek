import com._1c.v8.ibis.admin.client.AgentAdminConnectorFactory;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class Notificator {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        Util_1C util1C = new Util_1C("list bases.xml", new AgentAdminConnectorFactory());

        List basesList = util1C.getListOfBases();

        if (basesList.size() > 0) {
            System.out.println("Bases with session denied" + (basesList.size() == 1 ? " is: " : " are: "));
            List<Base_1C> warningBases = util1C.checkingOfBases(basesList);
            for (Base_1C warningBase: warningBases) {
                System.out.println(warningBase.getName());
            }
        }

        util1C.disconnect();
    }
}
