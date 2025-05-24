<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">
	<acme:input-textbox code="customer.booking.form.label.locatorCode" path="locatorCode"/>
	<acme:input-moment code="customer.booking.form.label.purchaseMoment" path="purchaseMoment"/>
	<acme:input-select code="customer.booking.form.label.travelClass" path="travelClass" choices="${choices}"/>
	<acme:input-select code="customer.booking.form.label.flight" path="flight" choices="${choicesFlight}"/>
	<jstl:if test="${acme:anyOf(_command, 'show|update|publish|delete')}">
		<acme:input-money code="customer.booking.form.label.price" path="price" readonly="true"/>
	</jstl:if>
	<acme:input-textbox code="customer.booking.form.label.lastNibble" path="lastNibble"/>
	<jstl:if test="${!readonly }">
		<acme:input-checkbox code="customer.booking.form.label.confirmation" path="confirmation" />
	</jstl:if>
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete')}">
			<acme:button code="customer.booking.form.button.passengers" action="/customer/booking-record/list?bookingId=${bookingId}"/>
			<jstl:if test="${!readonly}">
				<acme:submit code="customer.booking.form.button.update" action="/customer/booking/update"/>
			<acme:button code="customer.booking.form.button.addPassengers" action="/customer/booking-record/create?bookingId=${bookingId}"/>
			<acme:submit code="customer.booking.form.button.draftMode" action="/customer/booking/publish"/>
			<acme:submit code="customer.booking.form.button.delete" action="/customer/booking/delete"/>
			</jstl:if>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
		</jstl:when>		
	</jstl:choose>		
</acme:form>