package Main;

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

        Notificator notificator = new Notificator("127.0.0.1", 1545);
        NotifierView notifierView = new NotifierView(notificator);
        WarningMessageWorker warningMessageWorker = new WarningMessageWorker();
        WarningMessageView warningMessageView = new WarningMessageView(warningMessageWorker);
        ConsoleTabbed mainTabbed = new ConsoleTabbed(notifierView, warningMessageView);
        EventQueue.invokeLater(new MainFrame(mainTabbed, TITLE));

    }
}
