<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="airline-manager.flight.form.label.tag" path="tag"/>
	<acme:input-textbox code="airline-manager.flight.form.label.description" path="description"/>
	<acme:input-money code="airline-manager.flight.form.label.cost" path="cost"/>
	<acme:input-checkbox code="airline-manager.flight.form.label.selfTransfer" path="selfTransfer"/>
		
	
	<acme:input-textbox code="airline-manager.flight.form.label.originCity" path="originCity" readonly="true"/>
	<acme:input-textbox code="airline-manager.flight.form.label.destinationCity" path="destinationCity" readonly="true"/>
	<acme:input-moment code="airline-manager.flight.form.label.scheduledDeparture" path="scheduledDeparture" readonly="true"/>	
	<acme:input-moment code="airline-manager.flight.form.label.scheduledArrival" path="scheduledArrival" readonly="true"/>	
	<acme:input-integer code="airline-manager.flight.form.label.layovers" path="layovers" readonly="true" />	
	
	
	
	<jstl:choose>	 
		
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="airline-manager.flight.form.button.legs" action="/airline-manager/leg/list?masterId=${id}"/>			
		</jstl:when>			
					
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="airline-manager.flight.form.button.update" action="/airline-manager/flight/update"/>
			<acme:submit code="airline-manager.flight.form.button.delete" action="/airline-manager/flight/delete"/>
			<acme:submit code="airline-manager.flight.form.button.publish" action="/airline-manager/flight/publish"/>
			<acme:button code="airline-manager.flight.form.button.legs" action="/airline-manager/leg/list?masterId=${id}"/>			
			
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="airline-manager.flight.form.button.create" action="/airline-manager/flight/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>

