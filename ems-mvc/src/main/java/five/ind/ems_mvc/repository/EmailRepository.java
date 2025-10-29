package five.ind.ems_mvc.repository;

import five.ind.ems_mvc.entity.Email;
import five.ind.ems_mvc.entity.compositeId.EmailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, EmailId> {
    List<Email> findByEmployeeEmpId(Long empId);
}
