
package acme.features.airlineManager.dashboard;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.Statistics;
import acme.entities.airport.Airport;
import acme.entities.leg.Status;
import acme.forms.AirlineManagerDashboard;
import acme.realms.employee.AirlineManager;

@GuiService
public class AirlineManagerDashboardShowService extends AbstractGuiService<AirlineManager, AirlineManagerDashboard> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		Boolean status;
		status = super.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class);

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		AirlineManagerDashboard dashboard;
		int id;
		int higherExperienceCount;
		Date birthDate;
		int yearsUntilRetirement = 0;
		int onTimeLegs;
		int delayedLegs;
		double flightTimelinessRatio;
		Airport mostPopularAirport = null;
		Airport leastPopularAirport = null;
		Long maxLegs = Long.MIN_VALUE;
		Long minLegs = Long.MAX_VALUE;
		Map<Status, Long> numberOfLegsByStatus = new HashMap<>();
		int countFlight;
		double avgCost;
		double maxCost;
		double minCost;
		double stdevCost;

		AirlineManager manager = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();

		id = manager.getId();
		higherExperienceCount = this.repository.countManagersWithMoreExperience(manager.getYearsExp());

		birthDate = manager.getDateBirth();

		Calendar currentCalendar = Calendar.getInstance();
		Calendar birthCalendar = Calendar.getInstance();
		birthCalendar.setTime(birthDate);
		int age = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);
		if (currentCalendar.get(Calendar.MONTH) < birthCalendar.get(Calendar.MONTH) || currentCalendar.get(Calendar.MONTH) == birthCalendar.get(Calendar.MONTH) && currentCalendar.get(Calendar.DAY_OF_MONTH) < birthCalendar.get(Calendar.DAY_OF_MONTH))
			age--;
		if (age < 65)
			yearsUntilRetirement = 65 - age;

		onTimeLegs = this.repository.countOnTimeLegs(id);
		delayedLegs = this.repository.countDelayedLegs(id);
		if (delayedLegs == 0)
			flightTimelinessRatio = Double.POSITIVE_INFINITY;
		else
			flightTimelinessRatio = onTimeLegs / delayedLegs;
		List<Object[]> originLegs = this.repository.countLegsByOrigin(id);
		List<Object[]> destinationLegs = this.repository.countLegsByDestination(id);

		Map<Airport, Long> airportFlightCounts = new HashMap<>();

		for (Object[] entry : originLegs) {
			Airport airport = (Airport) entry[0];
			Long count = (Long) entry[1];
			airportFlightCounts.put(airport, airportFlightCounts.getOrDefault(airport, 0L) + count);
		}

		for (Object[] entry : destinationLegs) {
			Airport airport = (Airport) entry[0];
			Long count = (Long) entry[1];
			airportFlightCounts.put(airport, airportFlightCounts.getOrDefault(airport, 0L) + count);
		}

		for (Entry<Airport, Long> entry : airportFlightCounts.entrySet()) {
			Long Legs = entry.getValue();
			if (Legs > maxLegs) {
				mostPopularAirport = entry.getKey();
				maxLegs = Legs;
			}
			if (Legs < minLegs) {
				leastPopularAirport = entry.getKey();
				minLegs = Legs;
			}
		}

		List<Object[]> LegstatusCounts = this.repository.countLegsByStatus(id);

		for (Object[] entry : LegstatusCounts) {
			Status status = (Status) entry[0];
			Long count = (Long) entry[1];
			numberOfLegsByStatus.put(status, count);
		}

		countFlight = this.repository.countFlights(id) == null ? 0 : this.repository.countFlights(id);
		avgCost = this.repository.getAverageFlightCost(id) == null ? 0 : this.repository.getAverageFlightCost(id);
		maxCost = this.repository.getMaxFlightCost(id) == null ? 0 : this.repository.getMaxFlightCost(id);
		minCost = this.repository.getMinFlightCost(id) == null ? 0 : this.repository.getMinFlightCost(id);
		stdevCost = this.repository.getSTDEVFlightCost(id) == null ? 0 : this.repository.getSTDEVFlightCost(id);

		Statistics statCost = new Statistics();
		statCost.setCount(countFlight);
		statCost.setAverage(avgCost);
		statCost.setMaximum(maxCost);
		statCost.setMinimum(minCost);
		statCost.setDeviation(stdevCost);

		dashboard = new AirlineManagerDashboard();
		dashboard.setExperienceRank(higherExperienceCount + 1);
		dashboard.setYearsUntilRetirement(yearsUntilRetirement);
		dashboard.setFlightTimelinessRatio(flightTimelinessRatio);
		dashboard.setMostPopularAirport(mostPopularAirport);
		dashboard.setLeastPopularAirport(leastPopularAirport);
		dashboard.setNumberOfLegsByStatus(numberOfLegsByStatus);
		dashboard.setCostStatistics(statCost);
		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AirlineManagerDashboard object) {
		Dataset dataset;
		Integer legsOntime;
		Integer legsDelayed;
		Integer legsCancelled;
		Integer legsLanded;

		legsOntime = (int) (object.getNumberOfLegsByStatus().get(Status.ON_TIME) != null ? object.getNumberOfLegsByStatus().get(Status.ON_TIME) : 0);
		legsDelayed = (int) (object.getNumberOfLegsByStatus().get(Status.DELAYED) != null ? object.getNumberOfLegsByStatus().get(Status.DELAYED) : 0);
		legsCancelled = (int) (object.getNumberOfLegsByStatus().get(Status.CANCELLED) != null ? object.getNumberOfLegsByStatus().get(Status.CANCELLED) : 0);
		legsLanded = (int) (object.getNumberOfLegsByStatus().get(Status.LANDED) != null ? object.getNumberOfLegsByStatus().get(Status.LANDED) : 0);

		dataset = super.unbindObject(object, //
			"experienceRank",// 
			"yearsUntilRetirement", "flightTimelinessRatio", "mostPopularAirport", "leastPopularAirport", "costStatistics");
		dataset.put("legsOnTime", legsOntime);
		dataset.put("legsDelayed", legsDelayed);
		dataset.put("legsCancelled", legsCancelled);
		dataset.put("legsLanded", legsLanded);

		super.getResponse().addData(dataset);
	}

}
