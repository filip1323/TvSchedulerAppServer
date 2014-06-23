/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tvschedulerdebugserver;

import com.esotericsoftware.kryonet.Connection;
import java.util.ArrayList;
import user_exceptions.DebugError;

/**
 *
 * @author Filip
 */
public class Controller {

    //<editor-fold defaultstate="collapsed" desc="INIT">
    private ArrayList<User> usersConnected;
    private ServerController serverController;
    private ServerService serverService;
    private UserActionController userActionController;

    private UserInterface userInterface;

    public Controller() {
	usersConnected = new ArrayList<>();
    }

    public void assignServerController(ServerController serverController) {
	this.serverController = serverController;
    }

    public void assignServerService(ServerService serverService) {
	this.serverService = serverService;
    }

    public void assignUserActionController(UserActionController userActionController) {
	this.userActionController = userActionController;
    }

    public void assignUserInterface(UserInterface ui) {
	userInterface = ui;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="INTERFACE">
    public UserActionController getUserActionController() {
	return userActionController;
    }

    public ServerController getServerController() {
	return serverController;
    }

    public ServerService getServerService() {
	return serverService;
    }

    public UserInterface getUserInterface() {
	return userInterface;
    }

    public void addUser(User newUser) {
	usersConnected.add(newUser);
	userInterface.addUser(newUser);
    }

    public ArrayList<User> getConnectedUsers() {
	return usersConnected;
    }

    public void removeUser(User user) {
	usersConnected.remove(user);
	userInterface.removeUser(user);
    }

    public User getUserByName(String name) {
	for (User user : usersConnected) {
	    if (user.getName().equals(name)) {
		return user;
	    }
	}
	throw new DebugError();
    }

    public User getUserByConnection(Connection cnctn) {
	for (User user : usersConnected) {
	    if (user.getConnection().equals(cnctn)) {
		return user;
	    }
	}
	throw new DebugError();
    }
//</editor-fold>

}
