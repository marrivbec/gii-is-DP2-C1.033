<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="airline-manager.flight.list.label.tag" path="tag" width="15%"/>
    <acme:list-column code="airline-manager.flight.list.label.selfTransfer" path="selfTransfer" width="15%"/>
    <acme:list-column code="airline-manager.flight.list.label.cost" path="cost" width="15%"/>
    <acme:list-column code="airline-manager.flight.list.label.originCity" path="originCity" width="15%"/>
    <acme:list-column code="airline-manager.flight.list.label.destinationCity" path="destinationCity" width="15%"/>
    <acme:list-column code="airline-manager.flight.list.label.scheduledDeparture" path="scheduledDeparture" width="15%"/>
	<acme:list-column code="airline-manager.flight.list.label.scheduledArrival" path="scheduledArrival" width="15%"/>
	<acme:list-column code="airline-manager.flight.list.label.layovers" path="layovers" width="15%"/>
	<acme:list-column code="airline-manager.flight.list.label.draftMode" path="draftMode" width="15%"/>
	
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="airline-manager.flight.list.button.create" action="/airline-manager/flight/create"/>
</jstl:if>	
