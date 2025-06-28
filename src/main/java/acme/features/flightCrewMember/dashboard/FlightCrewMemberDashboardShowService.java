
package acme.features.flightCrewMember.dashboard;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		Integer legsWithSeverity3 = this.repository.legsWithSeverity(0, 3, flightCrewMemberId);
		Integer legsWithSeverity7 = this.repository.legsWithSeverity(4, 7, flightCrewMemberId);
		Integer legsWithSeverity10 = this.repository.legsWithSeverity(8, 10, flightCrewMemberId);

		dashboard.setLegsWithIncidentSeverity03(legsWithSeverity3);
		dashboard.setLegsWithIncidentSeverity47(legsWithSeverity7);
		dashboard.setLegsWithIncidentSeverity810(legsWithSeverity10);

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////

		List<FlightAssignment> lastLegAssignment = this.repository.findFlightAssignment(flightCrewMemberId, PageRequest.of(0, 1));

		FlightAssignment fa = !lastLegAssignment.isEmpty() ? lastLegAssignment.get(0) : null;

		List<String> legMemberAssignment = fa != null ? this.repository.memberLastLeg(fa.getLeg().getId()) : List.of(flightCrewMember.getIdentity().getFullName());

		dashboard.setLastLegCrewMembers(legMemberAssignment);

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////

		Map<Status, Integer> faByStatus = new HashMap<>();

		for (Status status : Status.values())
			faByStatus.put(status, this.repository.nFaByStatus(flightCrewMemberId, status));

		dashboard.setFlightAssignmentsGroupedByStatus(faByStatus);

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////

		Statistics statsLastYear = new Statistics();

		Calendar calendar = Calendar.getInstance();
		Date dateMinus1Year = MomentHelper.deltaFromCurrentMoment(-1, ChronoUnit.YEARS);
		calendar.setTime(dateMinus1Year);
		int year = calendar.get(Calendar.YEAR);

		Integer totalCount = this.repository.faLastYear(year, flightCrewMemberId);
		List<Integer> assignmentsPerMonth = new ArrayList<>();

		for (int month = 1; month <= 12; month++) {
			Integer countPerMonth = this.repository.faInMonth(flightCrewMemberId, year, month);
			assignmentsPerMonth.add(countPerMonth != null ? countPerMonth : 0);
		}

		double average = (double) totalCount / 12;
		Integer min = assignmentsPerMonth.stream().min(Integer::compareTo).orElse(0);
		Integer max = assignmentsPerMonth.stream().max(Integer::compareTo).orElse(0);
		double standardDeviation = Math.sqrt(assignmentsPerMonth.stream().mapToDouble(n -> Math.pow(n - average, 2)).average().orElse(0.0));

		statsLastYear.setCount(totalCount);
		statsLastYear.setAverage(average);
		statsLastYear.setMinimum(min.doubleValue());
		statsLastYear.setMaximum(max.doubleValue());
		statsLastYear.setDeviation(standardDeviation);

		dashboard.setFlightAssignmentsStatistics(statsLastYear);

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
