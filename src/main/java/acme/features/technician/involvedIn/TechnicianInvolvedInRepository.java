
package acme.features.technician.involvedIn;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Involves;
import acme.entities.task.Task;

@Repository
public interface TechnicianInvolvedInRepository extends AbstractRepository {

	//encontrar todas las task de 
	//cambiado
	@Query("select b from Involves b where b.maintenanceRecord.technician.id=:technicianId")
	Collection<Involves> findAllInvolvedInByTechnicianId(int technicianId);

	//ESTE NO SE USA¿¿?¿?¿?¿
	@Query("select b from Booking b where b.id=:id")
	Booking findBookingById(int id);

	//cambiado
	@Query("select b.maintenanceRecord from Involves b where b.id=?1")
	MaintenanceRecord findOneRecordByInvolvedIn(int id);

	//cambiado
	@Query("select b.task from Involves b where b.id=?1")
	Task findOneTaskByInvolvedIn(int id);

	//cambiado
	@Query("select b from Involves b where b.id=:id")
	Involves findInvolvedIn(int id);

	//encontrar las tasks de un tecnico
	@Query("select b from Task b where b.technician.id=:id")
	Collection<Task> findTaskByTechnicianId(int id);

	//encontrar el record con el tecnico
	@Query("select b from MaintenanceRecord b where b.technician.id=:id")
	Collection<MaintenanceRecord> findRecordByTechnicianId(int id);

	//CAMBIADO
	@Query("SELECT COUNT(b) > 0 FROM Involves b WHERE b.maintenanceRecord = :maintenanceRecord AND b.task = :task")
	boolean existsByRecordAndTask(@Param("maintenanceRecord") MaintenanceRecord maintenanceRecord, @Param("task") Task task);

	//CAMBIADO
	@Query("select b from MaintenanceRecord b where b.technician.id=:id and b.draftMode=:draftMode")
	Collection<MaintenanceRecord> findNotPublishRecord(@Param("id") int id, @Param("draftMode") boolean draftMode);

}
