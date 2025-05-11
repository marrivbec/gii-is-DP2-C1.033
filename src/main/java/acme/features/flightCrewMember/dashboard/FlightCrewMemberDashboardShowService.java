
package acme.features.flightCrewMember.dashboard;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.Statistics;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.Status;
import acme.forms.FlightCrewMemberDashboard;
import acme.realms.employee.FlightCrewMember;

@GuiService
public class FlightCrewMemberDashboardShowService extends AbstractGuiService<FlightCrewMember, FlightCrewMemberDashboard> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		FlightCrewMember flightCrewMember;

		flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		Principal principal = super.getRequest().getPrincipal();
		status = principal.hasRealm(flightCrewMember);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		FlightCrewMemberDashboard dashboard = new FlightCrewMemberDashboard();

		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		int flightCrewMemberId = flightCrewMember.getId();

		List<String> lastFiveDestinations = this.repository.findLastFiveDestinations(flightCrewMemberId, PageRequest.of(0, 5));

		dashboard.setLastFiveDestinations(lastFiveDestinations);

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////

		Integer legsWithIncidentSeverity3 = this.repository.legsWithSeverityByCrewMember(0, 3, flightCrewMemberId);
		Integer legsWithIncidentSeverity7 = this.repository.legsWithSeverityByCrewMember(4, 7, flightCrewMemberId);
		Integer legsWithIncidentSeverity10 = this.repository.legsWithSeverityByCrewMember(8, 10, flightCrewMemberId);

		dashboard.setLegsWithIncidentSeverity03(legsWithIncidentSeverity3);
		dashboard.setLegsWithIncidentSeverity47(legsWithIncidentSeverity7);
		dashboard.setLegsWithIncidentSeverity810(legsWithIncidentSeverity10);

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////

		List<FlightAssignment> assigments = this.repository.findFlightAssignment(flightCrewMemberId);

		List<String> lastLegMembers = new ArrayList<>();

		if (!assigments.isEmpty()) {
			int legId = assigments.get(0).getLeg().getId();
			List<FlightCrewMember> flightCrewMembers = this.repository.findCrewMembersInLastLeg(legId);
			lastLegMembers = flightCrewMembers.stream().map(x -> x.getIdentity().getFullName()).toList();
		}

		dashboard.setLastLegCrewMembers(lastLegMembers);

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////

		List<Object[]> faStatus = this.repository.flightAssignmentsGroupedByStatus(flightCrewMemberId);

		Map<Status, Integer> faByStatus = new HashMap<>();

		for (Object[] result : faStatus) {
			Status type = (Status) result[0];
			Integer count = ((Long) result[1]).intValue();
			faByStatus.put(type, count);
		}

		dashboard.setFlightAssignmentsGroupedByStatus(faByStatus);

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////

		Date moment = MomentHelper.getCurrentMoment();
		LocalDate localDateMoment = moment.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate oneYearAgo = localDateMoment.minusYears(1);
		Date oneYearAgoDate = Date.from(oneYearAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());

		List<Object[]> monthlyCounts = this.repository.countFlightAssignmentsPerMonth(flightCrewMember, oneYearAgoDate, moment);

		List<Double> counts = monthlyCounts.stream().map(obj -> ((Number) obj[2]).doubleValue()).collect(Collectors.toList());

		Statistics statsLastYear = new Statistics();

		if (!counts.isEmpty()) {
			DoubleSummaryStatistics stats = counts.stream().mapToDouble(Double::doubleValue).summaryStatistics();

			statsLastYear.setAverage(stats.getAverage());
			statsLastYear.setMinimum(stats.getMin());
			statsLastYear.setMaximum(stats.getMax());

			double variance = counts.stream().mapToDouble(count -> Math.pow(count - stats.getAverage(), 2)).average().orElse(0.0);
			statsLastYear.setDeviation(Math.sqrt(variance));
		} else {
			statsLastYear.setAverage(0.0);
			statsLastYear.setMinimum(0.0);
			statsLastYear.setMaximum(0.0);
			statsLastYear.setDeviation(0.0);
		}

		dashboard.setFlightAssignmentsStatistics(statsLastYear);

		super.getBuffer().addData(dashboard);

		//		Date dateMinus1Year = MomentHelper.deltaFromCurrentMoment(-1, ChronoUnit.YEARS);
		//
		//		Integer count = this.repository.countFlightAssignmentsLastYear(MomentHelper.deltaFromCurrentMoment(-1, ChronoUnit.YEARS), flightCrewMemberId);
		//
		//		Double average = (double) count / 12;
		//
		//		Calendar calendar = Calendar.getInstance();
		//
		//		calendar.setTime(dateMinus1Year);
		//
		//		int year = calendar.get(Calendar.YEAR);
		//
		//		Integer countPerMonth = 0;
		//
		//		List<Integer> assignmentsPerMonth = new ArrayList<>();
		//
		//		for (int month = 1; month <= 12; month++) {
		//
		//			countPerMonth = this.repository.countFlightAssignmentsPerMonthAndYear(flightCrewMemberId, year, month);
		//
		//			assignmentsPerMonth.add(countPerMonth != null ? countPerMonth : 0);
		//		}
		//
		//		Optional<Integer> min = assignmentsPerMonth.stream().min(Integer::compareTo);
		//		Optional<Integer> max = assignmentsPerMonth.stream().max(Integer::compareTo);
		//
		//		double standardDeviation = Math.sqrt(assignmentsPerMonth.stream().mapToDouble(n -> Math.pow(n - average, 2)).average().orElse(0.0));
		//
		//		flightAssignmentsStatsLastMonth.setCount(count);
		//		flightAssignmentsStatsLastMonth.setAverage(average);
		//		flightAssignmentsStatsLastMonth.setMinimum(min.orElse(0).doubleValue());
		//		flightAssignmentsStatsLastMonth.setMaximum(max.orElse(0).doubleValue());
		//		flightAssignmentsStatsLastMonth.setDeviation(standardDeviation);
		//
		//		dashboard.setFlightAssignmentsStatistics(flightAssignmentsStatsLastMonth);

	}

	@Override
	public void unbind(final FlightCrewMemberDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, "lastFiveDestinations", "legsWithIncidentSeverity03", "legsWithIncidentSeverity47", "legsWithIncidentSeverity810", "lastLegCrewMembers", "flightAssignmentsStatistics");

		dataset.put("CONFIRMED", dashboard.getFlightAssignmentsGroupedByStatus().getOrDefault(Status.CONFIRMED, 0));
		dataset.put("PENDING", dashboard.getFlightAssignmentsGroupedByStatus().getOrDefault(Status.PENDING, 0));
		dataset.put("CANCELLED", dashboard.getFlightAssignmentsGroupedByStatus().getOrDefault(Status.CANCELLED, 0));

		super.getResponse().addData(dataset);
	}

}
