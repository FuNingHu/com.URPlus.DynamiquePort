package com.URPlus.DynamiquePort.impl;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
//import javax.swing.JTextPane;
//import javax.swing.text.SimpleAttributeSet;
//import javax.swing.text.StyleConstants;

import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;

public class DPInstallationNodeView implements SwingInstallationNodeView<DPInstallationNodeContribution>{
	
	private final Style style;
	private JTextField portInputField = new JTextField();
	private JButton enableButton = new JButton();
	private JButton disableButton = new JButton();
	private JLabel statusLabel = new JLabel();
	
	private Dimension buttonDimension = new Dimension(200,40);
	private Dimension inputDimension = new Dimension(160,40);
	
	private static final ImageIcon TIP_ICON = new ImageIcon(DPInstallationNodeContribution.class.getResource("/icon/tip.png"));
	
	public DPInstallationNodeView(Style style) { //public constructor for Service usage
		// TODO Auto-generated constructor stub
		this.style = style;
	}

	@Override
	public void buildUI(JPanel panel, DPInstallationNodeContribution contribution) {
		// TODO Auto-generated method stub
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.add(createInfo());
		panel.add(createVerticalSpacing(50));
		
		panel.add(createPortInputField(portInputField, contribution));
		panel.add(createVerticalSpacing(50));
		
		panel.add(createStartdisableButtons(contribution));
		panel.add(createStatusLabel(statusLabel));
		
		
		
	}
	
	private Box createInfo() {
		Box infoBox = Box.createHorizontalBox();
		infoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel pane = new JLabel("This installation node will showcase dynamic daemon port setting.");
		pane.setBorder(BorderFactory.createEtchedBorder());
		pane.setMaximumSize(pane.getPreferredSize());
		infoBox.add(pane);
		return infoBox;
	}
	
	private Box createPortInputField(final JTextField inputField, final DPInstallationNodeContribution contribution) {
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel jLabel = new JLabel("input xmlrpc port: ");
		JLabel tipIcon = new JLabel();
		TIP_ICON.setImage(TIP_ICON.getImage().getScaledInstance(TIP_ICON.getIconWidth()/3, TIP_ICON.getIconHeight()/3, Image.SCALE_DEFAULT));
		
		inputField.setPreferredSize(inputDimension);
		inputField.setMaximumSize(inputField.getPreferredSize());
		inputField.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				KeyboardNumberInput<Integer> keyboardInput = contribution.getInputForTextField(50010, 50020);
				keyboardInput.show(inputField, contribution.getCallbackForTextField());
			}
		});
		tipIcon.setIcon(TIP_ICON);
		tipIcon.setToolTipText("Tip: user can change xmlrpc server port here, then click the ENABLE button.");
		horizontalBox.add(jLabel);
		horizontalBox.add(createHorizontalSpacing());
		horizontalBox.add(inputField);
		horizontalBox.add(createHorizontalSpacing());
		horizontalBox.add(tipIcon);
		return horizontalBox;
	}
	private Box createStartdisableButtons(final DPInstallationNodeContribution contribution) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		enableButton = new JButton("ENABLE");
		enableButton.setPreferredSize(buttonDimension);
		enableButton.setMaximumSize(enableButton.getPreferredSize());
		enableButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contribution.onClickStartDaemon();
			}
		});
		inputBox.add(enableButton);

		inputBox.add(createHorizontalSpacing());

		disableButton = new JButton("DISABLE");
		disableButton.setPreferredSize(buttonDimension);
		disableButton.setMaximumSize(disableButton.getPreferredSize());
		disableButton.setEnabled(true);
		disableButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contribution.onClickStopDaemon();
			}
		});
		inputBox.add(disableButton);

		return inputBox;
	}
	
	private Box createStatusLabel(final JLabel jLabel) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		box.add(jLabel);
		return box;
	}
	
	private Component createHorizontalSpacing() {
		return Box.createRigidArea(new Dimension(style.getHorizontalSpacing(), 0));
	}

	private Component createVerticalSpacing(int space) {
		return Box.createRigidArea(new Dimension(0, space));
	}


	/*
	 * public method for Contribution usage
	 */
	public void setPortTextFieldText(Integer str) {
		portInputField.setText(str.toString());
	}
	public void setStartButton(Boolean enable) { //when start button is disabled, portInputField should also be disabled.
		enableButton.setEnabled(enable);
		portInputField.setEnabled(enable);
	}
	public void setStopButton(Boolean enable) {
		disableButton.setEnabled(enable);
	}
	public void setStatusLabel(String text) {
		statusLabel.setText(text);
	}

}
