<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.airline.list.label.name" path="name" width="15%"/>
	<acme:list-column code="administrator.airline.list.label.iata" path="iata" width="10%"/>
	<acme:list-column code="administrator.airline.list.label.web" path="web" width="15%"/>
	<acme:list-column code="administrator.airline.list.label.type" path="type" width="10%"/>
	<acme:list-column code="administrator.airline.list.label.dateFundation" path="dateFundation" width="15%"/>
	<acme:list-column code="administrator.airline.list.label.mail" path="mail" width="15%"/>
	<acme:list-column code="administrator.airline.list.label.phone" path="phone" width="10%"/>
	<acme:list-payload path="payload"/>
</acme:list>


<acme:button code="administrator.airline.list.button.create" action="/administrator/airline/create"/>