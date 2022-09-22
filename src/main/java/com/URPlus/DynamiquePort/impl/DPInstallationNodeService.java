package com.URPlus.DynamiquePort.impl;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

public class DPInstallationNodeService implements SwingInstallationNodeService<DPInstallationNodeContribution, DPInstallationNodeView>{

	private final DynamiquePortDaemonService daemonService;
	
	public DPInstallationNodeService(DynamiquePortDaemonService daemonService) {
		// TODO Auto-generated constructor stub
		this.daemonService = daemonService;
	}
	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTitle(Locale locale) {
		// TODO Auto-generated method stub
		return "Dynamique Port";
	}

	@Override
	public DPInstallationNodeView createView(ViewAPIProvider apiProvider) {
		// TODO Auto-generated method stub
		SystemAPI systemAPI = apiProvider.getSystemAPI();
//		Locale locale = systemAPI.getSystemSettings().getLocalization().getLocaleForProgrammingLanguage();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new DPInstallationNodeView(style);
	}

	@Override
	public DPInstallationNodeContribution createInstallationNode(InstallationAPIProvider apiProvider,
			DPInstallationNodeView view, DataModel model, CreationContext context) {
		// TODO Auto-generated method stub
		return new DPInstallationNodeContribution(apiProvider, view, model, daemonService, context);
	}

}
