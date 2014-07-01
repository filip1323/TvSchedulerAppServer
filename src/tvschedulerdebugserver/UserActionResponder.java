/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tvschedulerdebugserver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Filip
 */
public class UserActionResponder implements ActionListener {

    //<editor-fold defaultstate="collapsed" desc="INIT">
    private Controller mainController;
    private UserInterface userInterface;

    public void assignMainController(Controller mainController) {
	this.mainController = mainController;
    }

    public void assignUserInterface(UserInterface userInterface) {
	this.userInterface = userInterface;
    }
    //</editor-fold>

    @Override
    public void actionPerformed(ActionEvent e) {
	switch (e.getActionCommand()) {
	    case "comboBoxChanged": //user selection
		userInterface.reloadUserTab();
		break;
	    case "Change name":
		userInterface.changeUserName();
		break;
	    case "Get logs":
		userInterface.getUserLogs();
		break;
	    case "Send message":
		userInterface.sendUserMessage();
		break;
	    case "Selection changed": //server state
		if (userInterface.getServerState().equals(ServerStatus.enabled)) {
		    mainController.getUserActionController().startServer();
		} else {
		    mainController.getUserActionController().stopServer();
		}
		break;
	    case "Restart":
		mainController.getUserActionController().restartServer();
		break;
	    case "Exit":
		mainController.getUserActionController().shutdown();
		break;

	}
    }
}
