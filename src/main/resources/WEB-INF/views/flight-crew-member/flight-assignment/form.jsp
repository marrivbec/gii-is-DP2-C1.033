	<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.flightCrewMember" path="flightCrewMember" readonly="true" placeholder = "acme.placeholders.form.flightAssignment.flightCrewMember"/>
	<acme:input-moment code="flight-crew-member.flight-assignment.form.label.moment" path="moment" readonly="true" placeholder = "acme.placeholders.form.flightAssignment.moment"/>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.duty" path="duty" choices="${dutyChoices}" readonly="draftMode"/>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.currentStatus" path="currentStatus" choices="${statusChoices}" readonly="draftMode"/>
	<acme:input-textarea code="flight-crew-member.flight-assignment.form.label.remarks" path="remarks" readonly="draftMode" placeholder = "remarks"/>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.leg" path="leg" choices="${legChoices}" readonly="draftMode" />
	
	<acme:input-moment code="airlineManager.leg.form.label.scheduledDeparture" path="scheduledDeparture" readonly="true"/>
	<acme:input-moment code="airlineManager.leg.form.label.scheduledArrival" path="scheduledArrival" readonly="true"/>
	<acme:input-textbox code="airlineManager.leg.form.label.status" path="status" readonly="true"/>
	<acme:input-textbox code="airlineManager.leg.form.label.departureAirport" path="departureAirport" readonly="true"/>
	<acme:input-textbox code="airlineManager.leg.form.label.arrivalAirport" path="arrivalAirport" readonly="true"/>
	<acme:input-textbox code="airlineManager.leg.form.label.aircraft" path="aircraft" readonly="true"/>
	<acme:input-textbox code="airlineManager.leg.form.label.flight" path="flight" readonly="true"/>
			
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight-crew-member.flight-assignment.form.button.create" action="/flight-crew-member/flight-assignment/create"/>
		</jstl:when>
		
		
		
		
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
		
		
			<acme:input-moment code="airlineManager.leg.form.label.scheduledDeparture" path="scheduledDeparture" readonly="true"/>
			<acme:input-moment code="airlineManager.leg.form.label.scheduledArrival" path="scheduledArrival" readonly="true"/>
			<acme:input-textbox code="airlineManager.leg.form.label.status" path="status" readonly="true"/>
			<acme:input-textbox code="airlineManager.leg.form.label.departureAirport" path="departureAirport" readonly="true"/>
			<acme:input-textbox code="airlineManager.leg.form.label.arrivalAirport" path="arrivalAirport" readonly="true"/>
			<acme:input-textbox code="airlineManager.leg.form.label.aircraft" path="aircraft" readonly="true"/>
			<acme:input-textbox code="airlineManager.leg.form.label.flight" path="flight" readonly="true"/>
		
			<acme:submit code="flight-crew-member.flight-assignment.form.button.update" action="/flight-crew-member/flight-assignment/update"/>
			<acme:submit code="flight-crew-member.flight-assignment.form.button.publish" action="/flight-crew-member/flight-assignment/publish"/>
			<acme:submit code="flight-crew-member.flight-assignment.form.button.delete" action="/flight-crew-member/flight-assignment/delete"/>
			
			
		
			
		<jstl:if test="${pastLeg }">
			<acme:button code="flight-crew-member.flight-assignment.form.button.activityLogs" action="/flight-crew-member/activity-log/list?masterId=${id}"/>			
			</jstl:if>
		</jstl:when>
		
		<jstl:when test="${acme:anyOf(_command, 'show') && draftMode == false && pastLeg }">
			
			
			<acme:input-moment code="airlineManager.leg.form.label.scheduledDeparture" path="scheduledDeparture" readonly="true"/>
			<acme:input-moment code="airlineManager.leg.form.label.scheduledArrival" path="scheduledArrival" readonly="true"/>
			<acme:input-textbox code="airlineManager.leg.form.label.status" path="status" readonly="true"/>
			<acme:input-textbox code="airlineManager.leg.form.label.departureAirport" path="departureAirport" readonly="true"/>
			<acme:input-textbox code="airlineManager.leg.form.label.arrivalAirport" path="arrivalAirport" readonly="true"/>
			<acme:input-textbox code="airlineManager.leg.form.label.aircraft" path="aircraft" readonly="true"/>
			<acme:input-textbox code="airlineManager.leg.form.label.flight" path="flight" readonly="true"/>		
			
			<acme:button code="flight-crew-member.flight-assignment.form.button.activityLogs" action="/flight-crew-member/activity-log/list?masterId=${id}"/>
			
				
		</jstl:when>

		
	</jstl:choose>
	
	
</acme:form>