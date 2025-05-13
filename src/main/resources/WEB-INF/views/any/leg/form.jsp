<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="airline-manager.leg.form.label.flightNumber" path="flightNumber"/>
	<acme:input-moment code="airline-manager.leg.form.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:input-moment code="airline-manager.leg.form.label.scheduledArrival" path="scheduledArrival" />
	<acme:input-select code="airline-manager.leg.form.label.status" path="status" choices="${status}"/>
	<acme:input-select code="airline-manager.leg.form.label.aircraft" path="aircraft" choices="${aircrafts}"/>	
	<acme:input-select code="airline-manager.leg.form.label.departureAirport" path="departureAirport" choices="${departureAirports}"/>	
	<acme:input-select code="airline-manager.leg.form.label.arrivalAirport" path="arrivalAirport" choices="${arrivalAirports}"/>			
</acme:form>


