<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="technician.maintenance-record.form.label.status" path="status" width="5%"/>
	<acme:list-column code="technician.maintenance-record.form.label.estimatedCost" path="estimatedCost" width="5%"/>
	<acme:list-column code="technician.maintenance-record.form.label.notes" path="notes" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="technician.maintenance-record.list.button.create" action="/technician/maintenance-record/create"/>
</jstl:if>