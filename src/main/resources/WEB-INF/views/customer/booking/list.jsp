<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.booking.list.label.locatorCode" path="locatorCode" width="15%"/>
	<acme:list-column code="customer.booking.list.label.purchaseMoment" path="purchaseMoment" width="10%"/>
	<acme:list-column code="customer.booking.list.label.travelClass" path="travelClass" width="15%"/>
	<acme:list-column code="customer.booking.list.label.price" path="price" width="10%"/>
	<acme:list-column code="customer.booking.list.label.lastNibble" path="lastNibble" width="15%"/>
	<acme:list-payload path="payload"/>
</acme:list>


<acme:button code="customer.booking.list.button.create" action="/customer/booking/create"/>