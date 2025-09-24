package five.ind.ems_mvc.repository;

import five.ind.ems_mvc.entity.Attendance;
import five.ind.ems_mvc.entity.compositeId.AttendanceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceId> {

}
