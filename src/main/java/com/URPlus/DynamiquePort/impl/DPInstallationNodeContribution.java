package com.URPlus.DynamiquePort.impl;

import java.awt.EventQueue;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

//import org.apache.xmlrpc.XmlRpcException;

import com.ur.urcap.api.contribution.DaemonContribution;
import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.userinteraction.UserInteraction;
import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidationFactory;
import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidator;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;

public class DPInstallationNodeContribution implements InstallationNodeContribution{

//	private static final String XMLRPC_VARIABLE = "dynamique_port_swing";
	private static final String PORT_KEY = "port";
	private static final Integer PORT_DEFAULT = 50010;
	private static final String ENABLED_KEY = "enabled";
	
	private DataModel model;
	
	private final DPInstallationNodeView view;
	private final DynamiquePortDaemonService daemonService;
	private XmlRpcMyDaemonInterface xmlRpcDaemonInterface;
	
	private Timer uiTimer;
	private UserInteraction userInteraction;
	
	public DPInstallationNodeContribution(InstallationAPIProvider apiProvider, 
												DPInstallationNodeView view, 
												DataModel model, 
												DynamiquePortDaemonService daemonService, 
												CreationContext context) {
		// TODO Auto-generated constructor stub
		this.view = view;
		this.daemonService = daemonService;
		this.model = model;
		this.userInteraction = apiProvider.getUserInterfaceAPI().getUserInteraction();
		
	}
	
	private void updateUI() { 
		DaemonContribution.State state = getDaemonState();
		if(state == DaemonContribution.State.RUNNING) {
			view.setStartButton(false);
			view.setStopButton(true);
		}else {
			view.setStartButton(true);
			view.setStopButton(false);
		}
		
		String text = "";
		switch(state) {
		case RUNNING:
			text = "DynamiqueDaemon runs";
			break;
		case STOPPED:
			text = "DynamiqueDaemon stopped";
			break;
		case ERROR:
			text = "DynamiqueDaemon failed";
			break;
		}
		view.setStatusLabel(text);
	}
	
	private DaemonContribution.State getDaemonState(){
		return daemonService.getDaemon().getState();
	}
	
	@Override
	public void openView() {
		// TODO Auto-generated method stub
		view.setPortTextFieldText(model.get(PORT_KEY, PORT_DEFAULT));
		
		//UI update from nonGUI threads must use EventQueue.invokeLater
		uiTimer = new Timer(true);
		uiTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventQueue.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						updateUI();
					}
				});
			}
		}, 0, 200); //update interval 200ms
	}

	@Override
	public void closeView() {
		// TODO Auto-generated method stub
		if(uiTimer != null) {
			uiTimer.cancel();
		}
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		// TODO Auto-generated method stub
		
	}

	
//	public XmlRpcMyDaemonInterface getXmlRpcMyDaemonInterface() {
//		return xmlRpcDaemonInterface;
//	}
	private Boolean isDaemonEnabled() {
		return model.get(ENABLED_KEY, true);
	}
	private void applyDesiredDaemonStatus() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(isDaemonEnabled()) {
					try {
						awaitDaemonRunning(5000);
						xmlRpcDaemonInterface.readPort();
					}catch (Exception e) {
						// TODO: handle exception
						System.err.println("Could not run the daemon process");
						e.printStackTrace();
					}
				}else {
					daemonService.getDaemon().stop();
				}
			}
		}).start();
	}
	private void awaitDaemonRunning(long timeOutMilliSeconds) throws InterruptedException {
		daemonService.getDaemon().start();
		long endTime = System.nanoTime() + timeOutMilliSeconds * 1000L * 1000L;
		while(System.nanoTime() < endTime && (daemonService.getDaemon().getState() != DaemonContribution.State.RUNNING || !xmlRpcDaemonInterface.isReachable())) {
			Thread.sleep(100);
		}
	}
	
	public void onClickStartDaemon() {
		xmlRpcDaemonInterface = XmlRpcMyDaemonInterface.getInerfaceInstance(model.get(PORT_KEY, PORT_DEFAULT)); //get current interface instance
		model.set(ENABLED_KEY, true);
		applyDesiredDaemonStatus();
	}
	public void onClickStopDaemon() {
		model.set(ENABLED_KEY, false);
		applyDesiredDaemonStatus();
	}
	
	public KeyboardNumberInput<Integer> getInputForTextField(Integer minValue, Integer maxValue) {
		KeyboardInputFactory keyboardFactory = userInteraction.getKeyboardInputFactory();
		InputValidationFactory validationFactory = userInteraction.getInputValidationFactory();
		
		InputValidator<Integer> validator = validationFactory.createIntegerRangeValidator(minValue, maxValue);
		
		final KeyboardNumberInput<Integer> keyboardInput = keyboardFactory.createPositiveIntegerKeypadInput();
		keyboardInput.setErrorValidator(validator);
		keyboardInput.setInitialValue(model.get(PORT_KEY, PORT_DEFAULT));
		
		return keyboardInput;
	}

	/*
	 * public method for View usage
	 */
	public KeyboardInputCallback<Integer> getCallbackForTextField() {
		return new KeyboardInputCallback<Integer>() {
			@Override
			public void onOk(Integer value) {
				model.set(PORT_KEY, value);
				view.setPortTextFieldText(value);
				try {
					BufferedWriter out = new BufferedWriter(new FileWriter("/home/ur/ursim/ursim-5.11.0.108249/programs/port.dat"));
					// this is only for demonstration, while being deployed on real robot, it would be another location.
					out.write(value.toString());
					out.close();
					System.out.println("File creation successful!");
				}catch(IOException e) {
					
				}
			}
		};
	}
	

}
