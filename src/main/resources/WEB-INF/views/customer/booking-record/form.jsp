<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form >
	<acme:input-select code="customer.booking-record.form.label.passenger" path="passenger" choices="${choices}"/>
	<acme:submit code="customer.booking.form.button.update" action="/customer/booking-record/create?bookingId=${bookingId}"/>	
</acme:form>