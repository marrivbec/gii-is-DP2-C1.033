<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true">
	<acme:input-textbox code="administrator.booking.form.label.locatorCode" path="locatorCode"/>
	<acme:input-moment code="administrator.booking.form.label.purchaseMoment" path="purchaseMoment"/>
	<acme:input-select code="administrator.booking.form.label.travelClass" path="travelClass" choices="${choices}"/>
	<acme:input-select code="administrator.booking.form.label.flight" path="flight" choices="${choicesFlight}"/>
	<acme:input-textbox code="administrator.booking.form.label.lastNibble" path="lastNibble"/>
	<acme:input-money code="administrator.booking.form.label.price" path="price" readonly="true"/>
	
	<acme:button code="administrator.bookingPassenger.list.button.passenger" action="/administrator/passenger/list?bookingId=${id}"/>
	
</acme:form>