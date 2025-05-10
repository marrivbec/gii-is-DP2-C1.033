<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>

<div class="container text-center">

 	<h3 class="mt-4 fw-bold">
        <acme:print code="airline-manager.airline-manager-dashboard.experienceRank" />
    </h3>
    <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="airline-manager.airline-manager-dashboard.label.experienceRank" /></th>
            <td><acme:print value="${experienceRank}" /></td>
        </tr>
    </table>
    
    <h3 class="mt-4 fw-bold">
        <acme:print code="airline-manager.airline-manager-dashboard.yearsUntilRetirement" />
    </h3>
    <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="airline-manager.airline-manager-dashboard.label.yearsUntilRetirement" /></th>
            <td><acme:print value="${yearsUntilRetirement}" /></td>
        </tr>
    </table>
    
    <h3 class="mt-4 fw-bold">
        <acme:print code="airline-manager.airline-manager-dashboard.flightTimelinessRatio" />
    </h3>
    <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="airline-manager.airline-manager-dashboard.label.flightTimelinessRatio" /></th>
            <td><acme:print value="${flightTimelinessRatio}" /></td>
        </tr>
    </table>
    
     <h3 class="mt-4 fw-bold">
        <acme:print code="airline-manager.airline-manager-dashboard.mostPopularAirport" />
    </h3>
    <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="airline-manager.airline-manager-dashboard.label.mostPopularAirport" /></th>
            <td><acme:print value="${mostPopularAirport.name}" /></td>
        </tr>
        <tr>
            <th><acme:print code="airline-manager.airline-manager-dashboard.label.leastPopularAirport" /></th>
            <td><acme:print value="${leastPopularAirport.name}" /></td>
        </tr>
    </table>
    
    <h3 class="mt-4 fw-bold">
        <acme:print code="airline-manager.airline-manager-dashboard.costStatistics" />
    </h3>
    <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="airline-manager.airline-manager-dashboard.label.count-costStatistics" /></th>
            <td><acme:print value="${costStatistics.count}" /></td>
        </tr>
        <tr>
            <th><acme:print code="airline-manager.airline-manager-dashboard.label.average-costStatistics" /></th>
            <td><acme:print value="${costStatistics.average}" /></td>
        </tr>
        <tr>
            <th><acme:print code="airline-manager.airline-manager-dashboard.label.minimum-costStatistics" /></th>
            <td><acme:print value="${costStatistics.minimum}" /></td>
        </tr>
        <tr>
            <th><acme:print code="airline-manager.airline-manager-dashboard.label.maximum-costStatistics" /></th>
            <td><acme:print value="${costStatistics.maximum}" /></td>
        </tr>
        <tr>
            <th><acme:print code="airline-manager.airline-manager-dashboard.label.devv-costStatistics" /></th>
            <td><acme:print value="${costStatistics.deviation}" /></td>
        </tr>
    </table>

    <h3 class="mt-4 fw-bold">
        <acme:print code="airline-manager.airline-manager-dashboard.legsStatus" />
    </h3>
    <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="airline-manager.airline-manager-dashboard.label.legsOnTime" /></th>
            <td><acme:print value="${legsOnTime}" /></td>
        </tr>
        <tr>
            <th><acme:print code="airline-manager.airline-manager-dashboard.legsDelayed" /></th>
            <td><acme:print value="${legsDelayed}" /></td>
        </tr>
         <tr>
            <th><acme:print code="airline-manager.airline-manager-dashboard.legsCancelled" /></th>
            <td><acme:print value="${legsCancelled}" /></td>
        </tr>
         <tr>
            <th><acme:print code="airline-manager.airline-manager-dashboard.legsLanded" /></th>
            <td><acme:print value="${legsLanded}" /></td>
        </tr>
    </table>

    <div class="mt-4">
        <acme:return />
    </div>
</div>