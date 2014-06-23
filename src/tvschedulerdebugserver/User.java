/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tvschedulerdebugserver;

import com.esotericsoftware.kryonet.Connection;
import misc.Bundles;

/**
 *
 * @author Filip
 */
public class User {

    private final Connection cnctn;
    private final String ipAddress;
    private final String connectionTime;
    private String macAddress;
    private String userName;

    public User(Connection cnctn) {
	this.cnctn = cnctn;
	this.ipAddress = cnctn.getRemoteAddressTCP().getAddress().getHostAddress();
	this.connectionTime = Bundles.getCurrentDateTimeInUnifiedFormat();
    }

    public void setMacAddress(String macAddress) {
	this.macAddress = macAddress;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public Connection getConnection() {
	return cnctn;
    }

    public String getName() {
	if (userName == null) {
	    return ipAddress;
	} else {
	    return userName;
	}
    }

    public String getIpAddress() {
	return ipAddress;
    }

    public String getConnectionTime() {
	return connectionTime;
    }

    public String getMacAddress() {
	return macAddress;
    }

    @Override
    public String toString() {
	return (userName == null) ? ipAddress : userName;
    }

}
