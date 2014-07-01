/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tvschedulerdebugserver;

import com.esotericsoftware.kryonet.Connection;
import java.util.ArrayList;
import misc.Utils;
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

    private String updatePath = "../TvSchedulerApp/dist/";

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
	    case request:
		if (netCourier.getHead().equals("clientHashcode")) {
		    try {
			String hashcode = Utils.Files.getMD5Checksum(updatePath + "TvSchedulerApp.jar");
			NetCourier courier = new NetCourier();
			courier.initialize("clientHashcode", hashcode, NetCourier.Type.respond);
			mainController.getServerService().sendTo(cnctn, courier);

		    } catch (Exception ex) {
			ex.printStackTrace();
		    }

		}
		break;
	}
    }
    //</editor-fold>

}
