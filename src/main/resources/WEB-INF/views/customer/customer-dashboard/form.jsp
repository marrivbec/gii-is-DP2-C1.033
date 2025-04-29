<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>

<div class="container text-center">
    
    <h3 class="mt-4 fw-bold">
        <acme:print code="customer.customer-dashboard.booking-statistics" />
    </h3>
    <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.count-booking" /></th>
            <td><acme:print value="${booking5Years.count}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.average-booking" /></th>
            <td><acme:print value="${booking5Years.average}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.minimum-booking" /></th>
            <td><acme:print value="${booking5Years.minimum}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.maximum-booking" /></th>
            <td><acme:print value="${booking5Years.maximum}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.devv-booking" /></th>
            <td><acme:print value="${booking5Years.deviation}" /></td>
        </tr>
    </table>

    
    <h3 class="mt-4 fw-bold">
        <acme:print code="customer.customer-dashboard.passenger-statistics" />
    </h3>
    <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.count-passenger" /></th>
            <td><acme:print value="${passengersBooking.count}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.average-passenger" /></th>
            <td><acme:print value="${passengersBooking.average}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.minimum-passenger" /></th>
            <td><acme:print value="${passengersBooking.minimum}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.maximum-passenger" /></th>
            <td><acme:print value="${passengersBooking.maximum}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.devv-passenger" /></th>
            <td><acme:print value="${passengersBooking.deviation}" /></td>
        </tr>
    </table>

    
    <h3 class="mt-4 fw-bold">
        <acme:print code="customer.customer-dashboard.last-five-destinations" />
    </h3>
    <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.last-five-destinations" /></th>
            <td><acme:print value="${theLastFiveDestinations}" /></td>
        </tr>
    </table>

    
    <h3 class="mt-4 fw-bold">
        <acme:print code="customer.customer-dashboard.money-spent-last-year" />
    </h3>
    <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.money-spent" /></th>
            <td><acme:print value="${moneySpentInBookingDuringLastYear}" /></td>
        </tr>
    </table>

    <h3 class="mt-4 fw-bold">
        <acme:print code="customer.customer-dashboard.numBYTravelClass" />
    </h3>
    <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.totalNumTravelClassEconomy" /></th>
            <td><acme:print value="${totalNumTravelClassEconomy}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customer-dashboard.label.totalNumTravelClassBusiness" /></th>
            <td><acme:print value="${totalNumTravelClassBusiness}" /></td>
        </tr>
    </table>

    <div class="mt-4">
        <acme:return />
    </div>
</div>