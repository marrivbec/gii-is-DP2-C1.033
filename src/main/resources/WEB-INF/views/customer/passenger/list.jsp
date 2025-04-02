<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
<acme:list-column code="customer.passenger.list.label.fullName" path="fullName" />
	<acme:list-column code="customer.passenger.list.label.passportNumber" path="passportNumber" />
	<acme:list-column code="customer.passenger.list.label.dateOfBirth" path="dateOfBirth" />
	<acme:list-column code="customer.passenger.list.label.specialNeeds" path="specialNeeds" />
	<acme:list-column code="customer.passenger.list.label.draftMode" path="draftMode" />
</acme:list>
<jstl:if test="${_command == 'list' }"> <acme:button code="customer.passenger.list.button.create" action="/customer/passenger/create"/></jstl:if>