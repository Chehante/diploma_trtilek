import Main.Notificator;
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
//
//
//            }
//        }
//////////////////////////////////////////WARNINGMESSAGEWORKER//////////////////////////////////////////////
//        WarningMessageWorker warningMessageWorker = new WarningMessageWorker("list warning messages.xml");
//        List<WarningMessage_1C> warningMessages = warningMessageWorker.getListOfWarningMessages();
//        warningMessageWorker.sendingWarningMessages(warningMessages);

        Notificator notificator = new Notificator();
        NotifierView notifierView = new NotifierView(notificator);
        WarningMessageView warningMessageView = new WarningMessageView();
        ConsoleTabbed mainTabbed =
                new ConsoleTabbed(notifierView, warningMessageView);
        EventQueue.invokeLater(new MainFrame(mainTabbed, TITLE));

    }
}
