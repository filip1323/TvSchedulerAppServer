/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tvschedulerdebugserver.gui;

import com.alee.extended.panel.GroupPanel;
import com.alee.extended.window.ComponentMoveAdapter;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import javax.swing.ImageIcon;
import misc.Utils;
import tvschedulerdebugserver.Resources;

/**
 *
 * @author Filip
 */
public class ConsoleGUI {

    WebScrollPane scrollPane;
    GroupPanel contentPanel;
    WebFrame frame;

    public void initComponents() {
	contentPanel = new GroupPanel(5, false);
	scrollPane = new WebScrollPane(contentPanel);
	scrollPane.setPreferredHeight(500);

	//creating frame
	WebLookAndFeel.setDecorateFrames(true);
	frame = new WebFrame();
	frame.add(scrollPane);
	ComponentMoveAdapter.install(frame);
	frame.setShowMinimizeButton(true);
	frame.setShowMaximizeButton(false);
	frame.setShowCloseButton(false);
	frame.setTitle("Console");
	frame.setResizable(false);
	frame.pack();
	frame.setVisible(true);
    }

    public void addOutput(String msg, String iconName) {
	ImageIcon icon = Resources.getImageIcon(iconName);
	WebLabel label = new WebLabel();
	label.setText(Utils.DateManager.getCurrentTime() + ": " + msg);
	label.setIcon(icon);
	label.setMargin(5);
	contentPanel.add(label);
	frame.pack();

    }

}
