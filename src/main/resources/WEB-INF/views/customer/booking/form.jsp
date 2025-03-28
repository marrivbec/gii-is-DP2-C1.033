<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">
	<acme:input-textbox code="customer.booking.form.label.locatorCode" path="locatorCode"/>
	<acme:input-moment code="customer.booking.form.label.purchaseMoment" path="purchaseMoment"/>
	<acme:input-select code="customer.booking.form.label.travelClass" path="travelClass" choices="${choices}"/>
	<acme:input-money code="customer.booking.form.label.price" path="price" readonly="true"/>
	<acme:input-textbox code="customer.booking.form.label.lastNibble" path="lastNibble"/>
	
	<acme:input-checkbox code="customer.booking.form.label.confirmation" path="confirmation" />
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update')}">
			<acme:submit code="customer.booking.form.button.update" action="/customer/booking/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
		</jstl:when>		
	</jstl:choose>		
</acme:form>
