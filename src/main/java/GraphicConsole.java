import gui.view.ConsoleTabbed;
import gui.view.MainFrame;
import gui.view.NotifierView;
import gui.view.WarningMessageView;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;

public class GraphicConsole {

    private static final String TITLE =
            "Auxiliary administrative 1C console(KSRC&IFMO)";

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException{
//////////////////////////////////////////////NOTIFICATOR//////////////////////////////////////////////
//        Notificator notificator = new Notificator("list bases.xml");
//
//        List<Base_1C> basesList = notificator.getListOfBases();
//
//        if (basesList.size() > 0) {
//            System.out.println("Bases with session denied" + (basesList.size() == 1 ? " is: " : " are: "));
//            List<Base_1C> warningBases = notificator.checkingOfBases(basesList);
//            for (Base_1C warningBase: warningBases) {
//                System.out.println(warningBase.getName());
//            }
//        }
//////////////////////////////////////////WARNINGMESSAGEWORKER//////////////////////////////////////////////
//        WarningMessageWorker warningMessageWorker = new WarningMessageWorker("list warning messages.xml");
//        List<WarningMessage_1C> warningMessages = warningMessageWorker.getListOfWarningMessages();
//        warningMessageWorker.sendingWarningMessages(warningMessages);

        NotifierView notifierView = new NotifierView();
        WarningMessageView warningMessageView = new WarningMessageView();
        ConsoleTabbed mainTabbed =
                new ConsoleTabbed(notifierView, warningMessageView);
        EventQueue.invokeLater(new MainFrame(mainTabbed, TITLE));

    }
}
