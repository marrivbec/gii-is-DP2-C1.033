<%--
- menu.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:menu-bar>
	<acme:menu-left>
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="76585025: Ferrer �lvarez, �ngel Manuel" action="https://zoomquilt.org/"/>
			<acme:menu-suboption code="29584798: Villalba Fern�ndez, Jes�s" action="https://orangetv.orange.es/brw"/>
			<acme:menu-suboption code="29550304: Rivas Becerra, Mario" action="http://eelslap.com/"/>
			<acme:menu-suboption code="49093416: Ramirez Morales, Juan" action="https://www.apple.com/"/>
			<acme:menu-suboption code="77859734: Baladr�n Caballero, �lvaro" action="https://www.realbetisbalompie.es/"/>
			
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.any">
			<acme:menu-suboption code="master.menu.any.list-flight-assignments-published" action="/any/flight-assignment/list" />
			<acme:menu-suboption code="master.menu.any.list-flight-published" action="/any/flight/list" />
			
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator" access="hasRealm('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.list-user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-airports" action="/administrator/airport/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-airlines" action="/administrator/airline/list" />
			<acme:menu-suboption code="master.menu.administrator.list-aircrafts" action="/administrator/aircraft/list" />
			<acme:menu-suboption code="master.menu.administrator.list-claims" action="/administrator/claim/list" />
			<acme:menu-suboption code="master.menu.administrator.list-booking" action="/administrator/booking/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-initial" action="/administrator/system/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-sample" action="/administrator/system/populate-sample"/>			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-system-down" action="/administrator/system/shut-down"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.provider" access="hasRealm('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.consumer" access="hasRealm('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>
		<acme:menu-option code="master.menu.customer" access="hasRealm('Customer')">
            <acme:menu-suboption code="master.menu.customer.booking" action="/customer/booking/list" />
            <acme:menu-suboption code="master.menu.customer.show-dashboard" action="/customer/customer-dashboard/show"/>
            <acme:menu-suboption code="master.menu.customer.passenger" action="/customer/passenger/list" />
            
        </acme:menu-option>
		
		<acme:menu-option code="master.menu.assistanceAgent" access="hasRealm('AssistanceAgent')">
			<acme:menu-suboption code="master.menu.assistanceAgent.list-claims" action="/assistance-agent/claim/list"/>			
			<acme:menu-suboption code="master.menu.assistanceAgent.list-claims-pending" action="/assistance-agent/claim/pending"/>
			<acme:menu-suboption code="master.menu.assistanceAgent.show-dashboard" action="/assistance-agent/assistance-agent-dashboard/show" />
				
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.flightCrewMember" access="hasRealm('FlightCrewMember')">	
			<acme:menu-suboption code="master.menu.flightCrewMember.list-flight-assignments-planned" action="/flight-crew-member/flight-assignment/list-planned" />
			<acme:menu-suboption code="master.menu.flightCrewMember.show-dashboard" action="/flight-crew-member/flight-crew-member-dashboard/show"/>
			<acme:menu-suboption code="master.menu.flightCrewMember.list-flight-assignments-completed" action="/flight-crew-member/flight-assignment/list-completed" />
				
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.technicians" access="hasRealm('Technician')">
			<acme:menu-suboption code="master.menu.technicians.list-my-maintenance-records" action="/technician/maintenance-record/list"/>
			<acme:menu-suboption code="master.menu.technicians.list-my-tasks" action="/technician/task/list"/>

			
		</acme:menu-option>
		
	</acme:menu-left>
	

	
	
	<acme:menu-option code="master.menu.airline-manager" access="hasRealm('AirlineManager')">
			<acme:menu-suboption code="master.menu.airline-manager.list-my-flights" action="/airline-manager/flight/list" />	
            <acme:menu-suboption code="master.menu.airline-manager.show-dashboard" action="/airline-manager/airline-manager-dashboard/show"/>
			
					
	</acme:menu-option>
		
	<acme:menu-right>		
		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.become-assistanceAgent" action="/authenticated/assistance-agent/create" access="!hasRealm('AssistanceAgent')"/>
			<acme:menu-suboption code="master.menu.user-account.assistanceAgent-profile" action="/authenticated/assistance-agent/update" access="hasRealm('AssistanceAgent')"/>
			<acme:menu-suboption code="master.menu.user-account.become-airlineManager" action="/authenticated/airline-manager/create" access="!hasRealm('AirlineManager')"/>
			<acme:menu-suboption code="master.menu.user-account.airlineManager-profile" action="/authenticated/airline-manager/update" access="hasRealm('AirlineManager')"/>
			<acme:menu-suboption code="master.menu.user-account.become-customer" action="/authenticated/customer/create" access="!hasRealm('Customer')"/>
			<acme:menu-suboption code="master.menu.user-account.customer-profile" action="/authenticated/customer/update" access="hasRealm('Customer')"/>
			<acme:menu-suboption code="master.menu.user-account.general-profile" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider-profile" action="/authenticated/provider/update" access="hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.consumer-profile" action="/authenticated/consumer/update" access="hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-flight-crew-member" action="/authenticated/flight-crew-member/create" access="!hasRealm('FlightCrewMember')"/>
			<acme:menu-suboption code="master.menu.user-account.flight-crew-member-profile" action="/authenticated/flight-crew-member/update" access="hasRealm('FlightCrewMember')"/>
		</acme:menu-option>
	</acme:menu-right>
</acme:menu-bar>

