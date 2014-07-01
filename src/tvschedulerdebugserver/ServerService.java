/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tvschedulerdebugserver;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import net.KryonetSerialisation;
import user_exceptions.DebugError;
import user_exceptions.ServerAlreadyDisabled;
import user_exceptions.ServerAlreadyEnabled;

/**
 *
 * @author Filip
 */
public class ServerService {

    //<editor-fold defaultstate="collapsed" desc="INIT">
    private Controller mainController;

    private Server server;

    private Thread serverThread = null;

    public ServerService() {
	com.esotericsoftware.minlog.Log.set(com.esotericsoftware.minlog.Log.LEVEL_NONE);
	server = new Server(16384, 16384);
	server.addListener(new Listener() {
	    @Override
	    public void connected(Connection cnctn) {
		super.connected(cnctn);
		System.out.println("[SERVER]: CONNECTED: " + cnctn.getRemoteAddressTCP());
		mainController.getServerController().addConnectedUser(cnctn);
	    }

	    @Override
	    public void received(Connection cnctn, Object object) {
		super.received(cnctn, object);
		if (!(object instanceof com.esotericsoftware.kryonet.FrameworkMessage)) {
		    System.out.println("[SERVER]: OBJECT RECEIVED: " + object.getClass() + object);
		    mainController.getServerController().processReceivedObject(object, cnctn);
		}
	    }

	    @Override
	    public void disconnected(Connection cnctn) {
		super.disconnected(cnctn); //To change body of generated methods, choose Tools | Templates.
		System.out.println("[SERVER]: DISCONNECTED");
		mainController.getServerController().removeDisconnectedUsers();
	    }
	});
	Kryo kryo = server.getKryo();
	new KryonetSerialisation(kryo);
    }

    public void assignMainController(Controller mainController) {
	this.mainController = mainController;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="INTERFACE">
    public ServerStatus getStatus() {
	if (serverThread == null || !serverThread.isAlive()) {
	    return ServerStatus.disabled;
	} else if (serverThread.isAlive()) {
	    return ServerStatus.enabled;
	} else {
	    throw new DebugError();
	}
    }

    public void start() throws ServerAlreadyEnabled {
	if (serverThread != null) {
	    throw new ServerAlreadyEnabled();
	}
	serverThread = new Thread(server);
	serverThread.start();
	try {
	    server.bind(54555, 54555);
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
	mainController.getServerController().setServerServiceState(ServerStatus.enabled);
	System.out.println("[SERVER]: ON");
    }

    public void stop() throws ServerAlreadyDisabled {
	if (serverThread == null || !serverThread.isAlive()) {
	    throw new ServerAlreadyDisabled();
	}
	server.stop();
	serverThread = null;
	mainController.getServerController().setServerServiceState(ServerStatus.disabled);
	mainController.getServerController().removeDisconnectedUsers();
	System.out.println("[SERVER]: OFF");
    }

    public void sendTo(Connection cnctn, Object object) {
	System.out.println("[SERVER]: OBJECT SEND: " + object.getClass() + ": \t" + object);
	cnctn.sendTCP(object);
    }
//</editor-fold>

}
