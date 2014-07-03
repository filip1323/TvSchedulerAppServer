/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tvschedulerdebugserver;

import com.alee.managers.notification.NotificationManager;
import tvschedulerdebugserver.gui.ConsoleGUI;
import tvschedulerdebugserver.gui.GraphicalUserInterface;
import tvschedulerdebugserver.gui.UserInterface;

/**
 *
 * @author Filip
 */
public class TvSchedulerDebugServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	//create main controller
	Controller mainController = new Controller();

	//create server controller
	ServerController serverController = new ServerController();

	//create user action controller
	UserActionController userActionController = new UserActionController();

	//create server service
	ServerService serverService = new ServerService();

	//create user interface
	UserInterface ui = new UserInterface();

	//create gui
	GraphicalUserInterface gui = new GraphicalUserInterface();

	//create console
	ConsoleGUI console = new ConsoleGUI();

	//create user action responder
	UserActionResponder userActionResponder = new UserActionResponder();

	//assign arguments
	mainController.assignServerController(serverController);
	serverController.assignMainController(mainController);
	mainController.assignServerService(serverService);

	mainController.assignUserActionController(userActionController);
	userActionController.assignMainController(mainController);
	mainController.assignUserInterface(ui);

	serverService.assignMainController(mainController);

	ui.assignGUI(gui);
	ui.assignConsole(console);
	ui.assignMainController(mainController);

	gui.assignUserActionResponder(userActionResponder);

	userActionResponder.assignMainController(mainController);
	userActionResponder.assignUserInterface(ui);

	//init
	gui.initComponents();
	console.initComponents();

	//setting notofications location
	NotificationManager.setLocation(NotificationManager.SOUTH_WEST);
    }

}
