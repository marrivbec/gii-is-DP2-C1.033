<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>

<div class="container text-center">

    
  
    <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="flight-crew-member.flight-crew-member-dashboard.label.last-five-destinations" /></th>
            <td><acme:print value="${lastFiveDestinations}" /></td>
        </tr>
    </table>
    
    
    <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="flight-crew-member.flight-crew-member-dashboard.label.fcm-assigned" /></th>
            <td><acme:print value="${lastLegCrewMembers}" /></td>
        </tr>
    </table>
    
     <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="flight-crew-member.flight-crew-member-dashboard.label.grouped-by-status-confirmed" /></th>
            <td><acme:print value="${CONFIRMED}" /></td>
        </tr>
        <tr>
            <th><acme:print code="flight-crew-member.flight-crew-member-dashboard.label.grouped-by-status-pending" /></th>
            <td><acme:print value="${PENDING}" /></td>
        </tr>	
        <tr>
            <th><acme:print code="flight-crew-member.flight-crew-member-dashboard.label.grouped-by-status-cancelled" /></th>
            <td><acme:print value="${CANCELLED}" /></td>
        </tr>
        
        
    </table>
    
    <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="flight-crew-member.flight-crew-member-dashboard.label.legs-with-status-03" /></th>
            <td><acme:print value="${legsWithIncidentSeverity03}" /></td>
        </tr>
        <tr>
            <th><acme:print code="flight-crew-member.flight-crew-member-dashboard.label.legs-with-status-47" /></th>
            <td><acme:print value="${legsWithIncidentSeverity47}" /></td>
        </tr>
        <tr>
            <th><acme:print code="flight-crew-member.flight-crew-member-dashboard.label.legs-with-status-810" /></th>
            <td><acme:print value="${legsWithIncidentSeverity810}" /></td>
        </tr>
    </table>
    
    <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="flight-crew-member.flight-crew-member-dashboard.label.statistics" /></th>
            <td><acme:print value="${flightAssignmentsStatistics.average}" /></td>
            <td><acme:print value="${flightAssignmentsStatistics.minimum}" /></td>
            <td><acme:print value="${flightAssignmentsStatistics.maximum}" /></td>
            <td><acme:print value="${flightAssignmentsStatistics.deviation}" /></td>
        </tr>
    </table>
    
    

    
</div>