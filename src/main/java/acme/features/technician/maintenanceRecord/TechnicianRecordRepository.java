
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Involves;
import acme.entities.task.Task;

@Repository
public interface TechnicianRecordRepository extends AbstractRepository {

	//todos los records  

	@Query("SELECT f FROM MaintenanceRecord f")
	Collection<MaintenanceRecord> getAllMaintenanceRecords();

	@Query("select br from Involves br where br.maintenanceRecord.id=:id")
	Collection<Involves> findAllInvolvedInById(int id);

	//todos los records de un tecnico f
	@Query("SELECT f FROM MaintenanceRecord f WHERE f.technician.id = :technicianId")
	Collection<MaintenanceRecord> getMyMaintenanceRecords(int technicianId);

	//encontrar un record en concreto
	@Query("SELECT f FROM MaintenanceRecord f WHERE f.id=:id")
	MaintenanceRecord findRecordById(int id);

	//necesito las tareas de un record
	//ir a la tabla de involvedIn y mirar los que tengan id de record
	@Query("SELECT b.task from Involves b  WHERE b.maintenanceRecord.id = :recordId")
	Collection<Task> findTasksByRecordId(@Param("recordId") int recordId);

	//para encontrar el aircraft asociado al record
	@Query("SELECT b.aircraft FROM MaintenanceRecord b WHERE b.id=:recordId")
	Aircraft findAircraftByRecordId(@Param("recordId") int recordId);

	@Query("SELECT f FROM Aircraft f WHERE f.id = ?1")
	Aircraft findAircraftById(int id);

	@Query("SELECT f FROM Aircraft f")
	Collection<Aircraft> getAllAircraft();

}
