<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<jstl:choose>
	<jstl:when test="${acme:anyOf(_command, 'show|delete')}">
		<acme:form readonly="true">
			<acme:input-textarea code="customer.passenger.form.label.fullName" path="passenger.fullName"/>	
			<acme:input-email code="customer.passenger.form.label.email" path="passenger.email" />
			<acme:input-textbox code="customer.passenger.form.label.passportNumber" path="passenger.passportNumber"/>
			<acme:input-textarea code="customer.passenger.form.label.specialNeeds" path="passenger.specialNeeds" />
			<acme:input-moment code="customer.passenger.form.label.dateOfBirth" path="passenger.dateOfBirth" />
			<jstl:if test="${draftMode}">
				<acme:submit code="customer.booking-record.form.button.delete" action="/customer/booking-record/delete"/>
			</jstl:if>
			</acme:form>
		</jstl:when>
		
		<jstl:when test="${_command== 'create'}">
			<acme:form >
				<acme:input-select code="customer.booking-record.form.label.passenger" path="passenger" choices="${choices}"/>
				<acme:submit code="customer.booking-record.form.button.create" action="/customer/booking-record/create?bookingId=${bookingId} "/>	
		</acme:form>
		</jstl:when>
</jstl:choose>