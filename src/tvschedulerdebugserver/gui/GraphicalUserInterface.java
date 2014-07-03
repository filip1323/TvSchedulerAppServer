/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tvschedulerdebugserver.gui;

import com.alee.extended.button.WebSwitch;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.extended.window.ComponentMoveAdapter;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.separator.WebSeparator;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.TooltipWay;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import tvschedulerdebugserver.UserActionResponder;

/**
 *
 * @author Filip
 */
public class GraphicalUserInterface {

    //<editor--fold defaultstate="collapsed" desc="INIT">
    private UserActionResponder userActionResponder;

    public GraphicalUserInterface() {
	try {
	    UIManager.setLookAndFeel(new WebLookAndFeel());
	} catch (UnsupportedLookAndFeelException ex) {
	    ex.printStackTrace();
	}
    }

    public void assignUserActionResponder(UserActionResponder userActionResponder) {
	this.userActionResponder = userActionResponder;
    }
    //</editor-fold>

    //<editor--fold defaultstate="collapsed" desc="GUI CREATOR">
    private GroupPanel contentGroupPanel;
    private JPanel contentPanel;
    private GroupPanel userPanel;
    private WebComboBox usersComboBox;
    private DefaultComboBoxModel usersComboBoxModel;
    private WebLabel usersComboLabel;
    private WebButton usersChangeNameButton;
    private WebButton usersGetLogsButton;
    private WebButton usersSendMessageButton;
    private WebButton usersDeauthorizeButton;
    private WebLabel usersMacAddressLabel;
    private WebLabel usersIpAddressLabel;
    private WebLabel usersConnectionStatusLabel;

    private GroupPanel serverPanel;
    private WebLabel serverPanelLabel;
    private WebLabel serverStatusLabel;
    private WebButton serverRestartButton;
    private WebSwitch serverSwitchButton;
    private WebButton exitButton;
    private JWindow windowContainer;
    private GroupPanel mainLabelGroup;

    public void initComponents() {
	//-----------------------------BODY-----------------------------//
	//creating label
	WebLabel mainLabel = new WebLabel("Debugger");
	mainLabel.setFontSize(20);
	mainLabelGroup = new GroupPanel(false, mainLabel, new WebSeparator(true, true));
	//creating user tab
	usersComboBoxModel = new DefaultComboBoxModel();
	usersComboBox = new WebComboBox(usersComboBoxModel);
	usersComboBox.setPreferredWidth(100);
	usersComboBox.setMaximumSize(new Dimension(300, 400));
	usersComboBox.setMaximumRowCount(10);
	usersComboBox.addActionListener(userActionResponder);

	usersComboLabel = new WebLabel("User: ");
	usersComboLabel.setMargin(0, 5, 0, 0);

	usersChangeNameButton = new WebButton("Change name");
	usersChangeNameButton.addActionListener(userActionResponder);
	usersGetLogsButton = new WebButton("Get logs");
	usersGetLogsButton.addActionListener(userActionResponder);
	usersSendMessageButton = new WebButton("Send message");
	usersSendMessageButton.addActionListener(userActionResponder);
	usersDeauthorizeButton = new WebButton("Deauthorize");
	usersDeauthorizeButton.addActionListener(userActionResponder);

	usersMacAddressLabel = new WebLabel("Mac address", WebLabel.RIGHT);
	TooltipManager.setTooltip(usersMacAddressLabel, "00-19-66-90-45-CA", TooltipWay.left, 0);
	usersIpAddressLabel = new WebLabel("IP address", WebLabel.RIGHT);
	TooltipManager.setTooltip(usersIpAddressLabel, "192.168.1.100", TooltipWay.left, 0);
	usersConnectionStatusLabel = new WebLabel("Connected since", WebLabel.RIGHT);
	TooltipManager.setTooltip(usersConnectionStatusLabel, "21:15", TooltipWay.left, 0);

	userPanel = new GroupPanel(GroupingType.fillAll, 0, false, new GroupPanel(GroupingType.fillLast, usersComboLabel, usersComboBox), usersChangeNameButton, usersGetLogsButton, usersSendMessageButton, usersDeauthorizeButton, usersMacAddressLabel, usersIpAddressLabel, usersConnectionStatusLabel);
	disableUserPanel();

	//creating server status tab
	serverPanelLabel = new WebLabel("Server");
	serverPanelLabel.setMargin(0, 5, 0, 0);
	serverPanelLabel.setFontSize(16);
	serverStatusLabel = new WebLabel("Status: ");
	serverStatusLabel.setMargin(0, 5, 0, 0);
	serverSwitchButton = new WebSwitch();
	serverSwitchButton.addActionListener(userActionResponder);
	serverRestartButton = new WebButton("Restart");
	serverRestartButton.addActionListener(userActionResponder);

	serverPanel = new GroupPanel(0, false, serverPanelLabel, new GroupPanel(serverStatusLabel, serverSwitchButton), serverRestartButton);

	//creating exit button
	exitButton = new WebButton("Exit");
	exitButton.addActionListener(userActionResponder);

	//-----------------------------CONTENT-----------------------------//
	//creating contentGroupPanel panel with description, input field, ok and cancel
	contentGroupPanel = new GroupPanel(0, false,
		mainLabelGroup,
		new GroupPanel(GroupingType.fillAll, userPanel),
		new WebSeparator(),
		new GroupPanel(GroupingType.fillAll, serverPanel),
		new WebSeparator(),
		new GroupPanel(GroupingType.fillAll, exitButton)
	);
	contentGroupPanel.setMargin(5);
	//-----------------------------FINAL-----------------------------//

	contentPanel = new JPanel();
	contentPanel.add(contentGroupPanel, this);
	contentPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
	showUI();
    }

    //<editor--fold defaultstate="collapsed" desc="old jframe version">
    /* private void showUI() {
     //creating transparent jframe
     final JFrame windowContainer = new JFrame("");
     windowContainer.setExtendedState(JFrame.MAXIMIZED_BOTH);
     windowContainer.setUndecorated(true);
     windowContainer.setBackground(new Color(0, 0, 0, 0));
     windowContainer.setVisible(true);
     windowContainer.setLayout(null);
     windowContainer.setAlwaysOnTop(true);
     //adding content to windowContainer
     windowContainer.add(contentPanel);
     contentPanel.setVisible(true);
     contentPanel.setBounds(new Rectangle(contentPanel.getPreferredSize()));

     //getting taskbar size
     Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
     Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
     int taskBarHeight = scrnSize.height - winSize.height;

     //setting ui location on screen
     int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
     int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
     int xLocation = width - (int) contentPanel.getPreferredSize().getWidth();
     int yLocation = height - (int) contentPanel.getPreferredSize().getHeight() - taskBarHeight;
     contentPanel.setLocation(xLocation, yLocation);
     }*/
//</editor-fold>
    private void showUI() {
	//getting taskbar size
	Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
	Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
	int taskBarHeight = scrnSize.height - winSize.height;

	//getting windows size
	int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

	//creating transparent jwindow
	windowContainer = new JWindow();
	windowContainer.setAlwaysOnTop(true);
	windowContainer.setSize(width, height);
	windowContainer.setBackground(new Color(0, 0, 0, 0));
	windowContainer.setLayout(null);

	contentPanel.setBounds(new Rectangle(contentPanel.getPreferredSize()));
//
//	//setting ui location on screen
//	int xLocation = width - (int) contentPanel.getPreferredSize().getWidth();
//	int yLocation = height - (int) contentPanel.getPreferredSize().getHeight() - taskBarHeight;
//	contentPanel.setLocation(xLocation, yLocation);
//
//	//adding ui to windowContainer
//	windowContainer.add(contentPanel);
	WebLookAndFeel.setDecorateFrames(true);
	WebFrame frame = new WebFrame();
	frame.add(contentPanel);
	ComponentMoveAdapter.install(frame);
	frame.setShowMinimizeButton(true);
	frame.setShowMaximizeButton(false);
	frame.setShowCloseButton(false);
	frame.setTitle("Server");
	frame.setResizable(false);
	frame.pack();
	frame.setVisible(true);
	frame.setAlwaysOnTop(true);

	//showing ui
	windowContainer.setVisible(true);
    }
    //</editor-fold>

    //<editor--fold defaultstate="collapsed" desc="INTERFACE">
    public void reloadUserTab(String macAddress, String ipAddress, String connectionTime) {
	TooltipManager.removeTooltips(usersMacAddressLabel);
	TooltipManager.removeTooltips(usersIpAddressLabel);
	TooltipManager.removeTooltips(usersConnectionStatusLabel);

	TooltipManager.addTooltip(usersMacAddressLabel, macAddress, TooltipWay.left, 0);
	TooltipManager.addTooltip(usersIpAddressLabel, ipAddress, TooltipWay.left, 0);
	TooltipManager.addTooltip(usersConnectionStatusLabel, connectionTime + "", TooltipWay.left, 0);
    }

    public void reloadClientTab() {

    }

    public synchronized DefaultComboBoxModel getUsersComboBoxModel() {
	return usersComboBoxModel;
    }

    public WebComboBox getUsersComboBox() {
	return usersComboBox;
    }

    public void enableUserPanel() {
	for (Component c : userPanel.getComponents()) {
	    c.setEnabled(true);
	}
	usersComboLabel.setEnabled(true);
	usersComboBox.setEnabled(true);
    }

    public void disableUserPanel() {
	for (Component c : userPanel.getComponents()) {
	    c.setEnabled(false);
	}
	usersComboLabel.setEnabled(false);
	usersComboBox.setEnabled(false);
    }

    public WebSwitch getServerSwitch() {
	return serverSwitchButton;
    }

    public JWindow getActiveWindow() {
	return windowContainer;
    }
    //</editor-fold>

}
