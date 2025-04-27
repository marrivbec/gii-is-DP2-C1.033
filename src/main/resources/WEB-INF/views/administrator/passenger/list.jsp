<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<acme:list >
	<acme:list-column code="administrator.passenger.list.label.passportNumber" path="passportNumber" />
	<acme:list-column code="administrator.passenger.list.label.dateOfBirth" path="dateOfBirth" />
	<acme:list-column code="administrator.passenger.list.label.specialNeeds" path="specialNeeds" />
	<acme:list-column code="administrator.passenger.list.label.draftMode" path="draftMode" />
</acme:list>