/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tvschedulerdebugserver;

import com.esotericsoftware.kryonet.Connection;
import java.util.ArrayList;
import net.NetCourier;

/**
 *
 * @author Filip
 */
public class ServerController {

    //<editor-fold defaultstate="collapsed" desc="INIT">
    private Controller mainController;

    public void assignMainController(Controller mainController) {
	this.mainController = mainController;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="INTERFACE">
    public void addConnectedUser(Connection cnctn) {
	User newUser = new User(cnctn);
	mainController.addUser(newUser);
	NetCourier netCourier = new NetCourier();
	netCourier.initialize("macAddress", null, NetCourier.Type.request);
	cnctn.sendTCP(netCourier);
    }

    public void removeDisconnectedUsers() {
	ArrayList<User> allUsers = mainController.getConnectedUsers();
	ArrayList<User> usersToRemove = new ArrayList<>();
	for (User user : allUsers) {
	    if (!user.getConnection().isConnected()) {
		usersToRemove.add(user);
	    }
	}
	for (User user : usersToRemove) {
	    mainController.removeUser(user);
	}
    }

    public void processReceivedObject(Object obj, Connection cnctn) {
	if (obj instanceof NetCourier) {
	    processReceivedCourier((NetCourier) obj, cnctn);
	}
    }

    public void setServerServiceState(ServerStatus status) {
	mainController.getUserInterface().setServerServiceStatus(status);
    }

    private void processReceivedCourier(NetCourier netCourier, Connection cnctn) {
	switch (netCourier.getType()) {
	    case respond:
		if (netCourier.getHead().equals("macAddress")) {
		    User user = mainController.getUserByConnection(cnctn);
		    user.setUserName(Database.getInstance().getUserNameByMacAddress(netCourier.getBody()));
		    user.setMacAddress(netCourier.getBody());
		    mainController.getUserInterface().reloadUserTab();
		}
		break;
	}
    }
    //</editor-fold>

}
