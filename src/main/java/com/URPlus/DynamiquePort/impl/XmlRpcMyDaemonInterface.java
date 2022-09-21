package com.URPlus.DynamiquePort.impl;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class XmlRpcMyDaemonInterface {

	private final XmlRpcClient client;
	private static XmlRpcMyDaemonInterface interface_instance = null;
	private static int currentPort = 0;
	
	private XmlRpcMyDaemonInterface(String host, int port) { //singleton pattern to avoid multi-creation of interface instance
		this.currentPort = port;
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setEnabledForExtensions(true);
		try {
			config.setServerURL(new URL("http://" + host + ":" + port + "/RPC2"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		config.setConnectionTimeout(1000); //1s
		client = new XmlRpcClient();
		client.setConfig(config);
	}

	public boolean isReachable() {
		try {
			client.execute("readPort", new ArrayList<String>());
			return true;
		} catch (XmlRpcException e) {
			return false;
		}
	}

	public static XmlRpcMyDaemonInterface getInerfaceInstance(int port) {
		if(currentPort != port) {
			interface_instance = null; //Destroy current instance
		}
		if(interface_instance == null) {
			interface_instance = new XmlRpcMyDaemonInterface("127.0.0.1", port);
		}
		return interface_instance;
	}


	public String readPort() throws XmlRpcException, UnknownResponseException {
		Object result = client.execute("readPort", new ArrayList<String>());
		return processString(result);
	}

	private String processString(Object response) throws UnknownResponseException {
		if (response instanceof String) {
			return (String) response;
		} else {
			throw new UnknownResponseException();
		}
	}
}
