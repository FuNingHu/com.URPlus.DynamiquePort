package com.URPlus.DynamiquePort.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.ur.urcap.api.contribution.DaemonService;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;

/**
 * Hello world activator for the OSGi bundle URCAPS contribution
 *
 */
public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		DynamiquePortDaemonService daemonService = new DynamiquePortDaemonService();
		DPInstallationNodeService installationNodeService = new DPInstallationNodeService(daemonService);
		
		bundleContext.registerService(SwingInstallationNodeService.class, installationNodeService, null);
		bundleContext.registerService(DaemonService.class, daemonService, null);
		System.out.println("DynamiquePort says Hello World!");
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println("DynamiquePort says Goodbye World!");
	}
}

