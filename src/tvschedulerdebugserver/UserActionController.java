/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tvschedulerdebugserver;

import net.NetCourier;
import user_exceptions.ServerAlreadyDisabled;
import user_exceptions.ServerAlreadyEnabled;

/**
 *
 * @author Filip
 */
public class UserActionController {

    //<editor--fold defaultstate="collapsed" desc="INIT">
    private Controller mainController;

    public void assignMainController(Controller mainController) {
	this.mainController = mainController;
    }
    //</editor-fold>

    public void restartServer() {
	stopServer();
	startServer();
    }

    public void stopServer() {
	ServerService serverService = mainController.getServerService();
	try {
	    if (serverService.getStatus().equals(ServerStatus.enabled)) {
		serverService.stop();
	    }
	} catch (ServerAlreadyDisabled ex) {
	    ex.printStackTrace();
	}
	//mainController.removeAllUsers();
    }

    public void startServer() {
	ServerService serverService = mainController.getServerService();
	try {
	    if (serverService.getStatus().equals(ServerStatus.disabled)) {
		serverService.start();
	    }
	} catch (ServerAlreadyEnabled ex) {
	    ex.printStackTrace();
	}
    }

    public void changeUserName(User user, String name) {
	user.setUserName(name);
	Database.getInstance().changeUserName(user.getMacAddress(), user.getName());
    }

    public void sendMessageToUser(User user, String message) {
	NetCourier netCourier = new NetCourier();
	netCourier.initialize(message, NetCourier.Type.message);
	mainController.getServerService().sendTo(user.getConnection(), netCourier);
    }

    public void getLogs(User user) {
	throw new UnsupportedOperationException();
    }

    public void updateClient() {
	throw new UnsupportedOperationException();
    }

    public void shutdown() {
	System.exit(0);
    }
}
