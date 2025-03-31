/*
 * AssistanceAgentTrackingLogController.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.assistanceAgent.claim;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.claim.Claim;
import acme.realms.employee.AssistanceAgent;

@GuiController
public class AssistanceAgentClaimController extends AbstractGuiController<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimListService			listService;

	@Autowired
	private AssistanceAgentClaimListPendingService	listPendingService;

	@Autowired
	private AssistanceAgentClaimShowService			showService;

	@Autowired
	private AssistanceAgentClaimCreateService		createService;

	@Autowired
	private AssistanceAgentClaimUpdateService		updateService;

	@Autowired
	private AssistanceAgentClaimDeleteService		deleteService;

	@Autowired
	private AssistanceAgentClaimPublishService		publishService;


	// Constructors -----------------------------------------------------------
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("pending", "list", this.listPendingService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
