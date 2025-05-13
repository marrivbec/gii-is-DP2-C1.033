<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">
	<acme:input-textbox code="administrator.airline.form.label.name" path="name"/>
	<acme:input-textbox code="administrator.airline.form.label.iata" path="iata"/>
	<acme:input-url code="administrator.airline.form.label.web" path="web"/>
	<acme:input-select code="administrator.airline.form.label.type" path="type" choices="${type}"/>
	<acme:input-moment code="administrator.airline.form.label.dateFundation" path="dateFundation"/>
	<acme:input-email code="administrator.airline.form.label.mail" path="mail"/>
	<acme:input-textbox code="administrator.airline.form.label.phone" path="phone"/>
	
	<acme:input-checkbox code="administrator.airline.form.label.confirmation" path="confirmation" />
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update')}">
			<acme:submit code="administrator.airline.form.button.update" action="/administrator/airline/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="administrator.airline.form.button.create" action="/administrator/airline/create"/>
		</jstl:when>		
	</jstl:choose>		
</acme:form>
