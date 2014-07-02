/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tvschedulerdebugserver;

import com.esotericsoftware.kryonet.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import misc.Utils;
import net.NetCourier;
import net.file_manager.FileManager;

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

    private String updatePath = "..\\TvSchedulerApp\\dist\\";

    private void processReceivedCourier(NetCourier incomingCourier, Connection cnctn) {
	switch (incomingCourier.getType()) {
	    case respond:
		if (incomingCourier.getHead().equals("macAddress")) {
		    User user = mainController.getUserByConnection(cnctn);
		    user.setUserName(Database.getInstance().getUserNameByMacAddress(incomingCourier.getBody()));
		    user.setMacAddress(incomingCourier.getBody());
		    mainController.getUserInterface().reloadUserTab();
		}
		break;
	    case request:
		switch (incomingCourier.getHead()) {
		    case "clientHashcode":
			mainController.getUserInterface().showNotification(mainController.getUserByConnection(cnctn).getName() + " is checking app version", "dial.png");
			try {
			    String hashcode = Utils.Files.getMD5Checksum(updatePath + "TvSchedulerApp.jar");
			    NetCourier courier = new NetCourier();
			    courier.initialize("clientHashcode", hashcode, NetCourier.Type.respond);
			    mainController.getServerService().sendTo(cnctn, courier);
			} catch (Exception ex) {
			    ex.printStackTrace();
			}
			break;
		    case "applicationHashcodes":
			mainController.getUserInterface().showNotification(mainController.getUserByConnection(cnctn).getName() + " is updating app version", "data-transfer-download.png");
			HashMap<String, String> hashcodes = Utils.Files.getDirectoryListingHashcodes(updatePath, updatePath + "\\\\");
			for (String key : hashcodes.keySet()) {
			    NetCourier hash = new NetCourier();
			    hash.initialize(key, hashcodes.get(key), NetCourier.Type.hashcode);
			    mainController.getServerService().sendTo(cnctn, hash);
			}
			NetCourier finalCourier = new NetCourier();
			finalCourier.initialize("hashcodesSended", NetCourier.Type.respond);
			mainController.getServerService().sendTo(cnctn, finalCourier);
			break;
		}
		break;
	    case requestFile:
		FileManager.getInstance().sendFile(updatePath + incomingCourier.getHead(), incomingCourier.getHead(), cnctn);
		break;
	    case requestAuth:
		mainController.requestAuthorization(cnctn);
		break;

	}
    }

}
