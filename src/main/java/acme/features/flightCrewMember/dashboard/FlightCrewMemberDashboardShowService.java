
package acme.features.flightCrewMember.dashboard;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import acme.client.components.models.Dataset;
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
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		FlightCrewMemberDashboard dashboard = new FlightCrewMemberDashboard();

		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		int flightCrewMemberId = flightCrewMember.getId();

		List<String> lastFiveDestinations = this.repository.findLastFiveDestinations(flightCrewMemberId, PageRequest.of(0, 5));

		Integer legsWithIncidentSeverity3 = this.repository.legsWithSeverity(0, 3);

		Integer legsWithIncidentSeverity7 = this.repository.legsWithSeverity(4, 7);

		Integer legsWithIncidentSeverity10 = this.repository.legsWithSeverity(8, 10);

		List<FlightAssignment> assigments = this.repository.findFlightAssignment(flightCrewMemberId);

		List<String> lastLegMembers = new ArrayList<>();

		if (!assigments.isEmpty()) {

			int legId = assigments.get(0).getLeg().getId();
			List<FlightCrewMember> flightCrewMembers = this.repository.findCrewMembersInLastLeg(legId);

			lastLegMembers = flightCrewMembers.stream().map(x -> x.getIdentity().getFullName()).toList();
		}

		List<Object[]> faStats = this.repository.flightAssignmentsGroupedByStatus(flightCrewMemberId);

		Map<Status, Integer> flightAssignmentsGroupedByStatus = new HashMap<>();

		for (Object[] result : faStats) {

			Status statusType = (Status) result[0];

			Integer count = ((Long) result[1]).intValue();

			flightAssignmentsGroupedByStatus.put(statusType, count);
		}

		Statistics flightAssignmentsStatsLastMonth = new Statistics();

		Date dateMinus1Year = MomentHelper.deltaFromCurrentMoment(-1, ChronoUnit.YEARS);

		Integer count = this.repository.countFlightAssignmentsLastYear(MomentHelper.deltaFromCurrentMoment(-1, ChronoUnit.YEARS), flightCrewMemberId);

		Double average = (double) count / 12;

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(dateMinus1Year);

		int year = calendar.get(Calendar.YEAR);

		Integer countPerMonth = 0;

		List<Integer> assignmentsPerMonth = new ArrayList<>();

		for (int month = 1; month <= 12; month++) {

			countPerMonth = this.repository.countFlightAssignmentsPerMonthAndYear(flightCrewMemberId, year, month);

			assignmentsPerMonth.add(countPerMonth != null ? countPerMonth : 0);
		}

		Optional<Integer> min = assignmentsPerMonth.stream().min(Integer::compareTo);

		Optional<Integer> max = assignmentsPerMonth.stream().max(Integer::compareTo);

		double standardDeviation = Math.sqrt(assignmentsPerMonth.stream().mapToDouble(n -> Math.pow(n - average, 2)).average().orElse(0.0));

		flightAssignmentsStatsLastMonth.setCount(count);
		flightAssignmentsStatsLastMonth.setAverage(average);
		flightAssignmentsStatsLastMonth.setMinimum(min.orElse(0).doubleValue());
		flightAssignmentsStatsLastMonth.setMaximum(max.orElse(0).doubleValue());
		flightAssignmentsStatsLastMonth.setDeviation(standardDeviation);

		dashboard.setLastFiveDestinations(lastFiveDestinations);
		dashboard.setLegsWithIncidentSeverity03(legsWithIncidentSeverity3);
		dashboard.setLegsWithIncidentSeverity47(legsWithIncidentSeverity7);
		dashboard.setLegsWithIncidentSeverity810(legsWithIncidentSeverity10);
		dashboard.setLastLegCrewMembers(lastLegMembers);
		dashboard.setFlightAssignmentsGroupedByStatus(flightAssignmentsGroupedByStatus);
		dashboard.setFlightAssignmentsStatistics(flightAssignmentsStatsLastMonth);

		super.getBuffer().addData(dashboard);
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
