<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<%-- show="/customer/booking"--%>

<acme:list >
	<acme:list-column code="customer.passenger.list.label.passportNumber" path="passenger.passportNumber" />
	<acme:list-column code="customer.passenger.list.label.dateOfBirth" path="passenger.dateOfBirth" />
	<acme:list-column code="customer.passenger.list.label.specialNeeds" path="passenger.specialNeeds" />
	<acme:list-column code="customer.passenger.list.label.draftMode" path="passenger.draftMode" />
</acme:list>