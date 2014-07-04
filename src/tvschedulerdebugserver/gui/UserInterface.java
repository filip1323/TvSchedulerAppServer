/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tvschedulerdebugserver.gui;

import com.alee.laf.optionpane.WebOptionPane;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebNotificationPopup;
import misc.Utils;
import tvschedulerdebugserver.Controller;
import tvschedulerdebugserver.ServerStatus;
import tvschedulerdebugserver.User;

/**
 *
 * @author Filip
 */
public class UserInterface {

    //<editor--fold defaultstate="collapsed" desc="INIT">
    private GraphicalUserInterface graphicalUserInterface;
    private Controller mainController;
    private ConsoleGUI console;

    public void assignConsole(ConsoleGUI console) {
	this.console = console;
    }

    public void assignGUI(GraphicalUserInterface gui) {
	graphicalUserInterface = gui;
    }

    public void assignMainController(Controller mainController) {
	this.mainController = mainController;
    }
    //</editor-fold>

    public void deauthorize() {
	User user = getCurrentUser();
	mainController.deauthorize(user);
	showNotification(user.getName() + " acces denied", "lock-locked.png");
    }

    //<editor--fold defaultstate="collapsed" desc="INTERFACE">
    //<editor--fold defaultstate="collapsed" desc="SERVER TAB">
    public ServerStatus getServerState() {
	if (graphicalUserInterface.getServerSwitch().isSelected()) {
	    return ServerStatus.enabled;
	} else {
	    return ServerStatus.disabled;
	}
    }

    public void setServerServiceStatus(ServerStatus status) {
	if (!status.equals(getServerState())) {
	    graphicalUserInterface.getServerSwitch().setSelected(status.equals(ServerStatus.enabled));
	}
    }

//</editor-fold>
    //<editor--fold defaultstate="collapsed" desc="USER TAB">
    public void addUser(User user) {
	graphicalUserInterface.getUsersComboBoxModel().addElement(user);
	graphicalUserInterface.enableUserPanel();
	showNotification("User connected: " + user.getName(), "user-add.png");
    }

    public void removeUser(User user) {
	graphicalUserInterface.getUsersComboBoxModel().removeElement(user);
	if (graphicalUserInterface.getUsersComboBoxModel().getSize() == 0) {
	    graphicalUserInterface.disableUserPanel();
	}
	showNotification("User disconnected: " + user.getName(), "user-remove.png");
    }

    public void reloadUserTab() {
	if (graphicalUserInterface.getUsersComboBox().getSelectedItem() != null) {
	    User user = mainController.getUserByName(graphicalUserInterface.getUsersComboBox().getSelectedItem().toString());
	    graphicalUserInterface.reloadUserTab(user.getMacAddress(), user.getIpAddress(), user.getConnectionTime());
	}
	graphicalUserInterface.getUsersComboBox().repaint();
    }

    public void changeUserName() {
	User user = getCurrentUser();
	String userName = (WebOptionPane.showInputDialog("Change name for " + user.getName()));
	if (!userName.equals("")) {
	    mainController.getUserActionController().changeUserName(user, userName);
	}
	reloadUserTab();
    }

    public void sendUserMessage() {
	User user = getCurrentUser();
	String message = (WebOptionPane.showInputDialog("Message for " + user.getName()));
	mainController.getUserActionController().sendMessageToUser(user, message);
    }

    public void getUserLogs() {
	mainController.getUserActionController().getLogs(getCurrentUser());
    }
//</editor-fold>

    public void reloadClientTab() {
	throw new UnsupportedOperationException();
    }

//</editor-fold>
    public void showNotification(String msg, String iconName) {
	console.addOutput(Utils.DateManager.getCurrentDateTimeInMilis(), msg, iconName);
    }

    public void consoleOut(long time, String msg, String iconName) {
	console.addOutput(time, msg, iconName);
    }

    public void showNotification(WebNotificationPopup notification) {
	NotificationManager.showNotification(graphicalUserInterface.getActiveWindow(), notification);
    }

    private User getCurrentUser() {
	return (User) graphicalUserInterface.getUsersComboBoxModel().getSelectedItem();
    }

}
