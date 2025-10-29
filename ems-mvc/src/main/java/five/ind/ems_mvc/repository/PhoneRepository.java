package five.ind.ems_mvc.repository;

import five.ind.ems_mvc.entity.Email;
import five.ind.ems_mvc.entity.Phone;
import five.ind.ems_mvc.entity.compositeId.PhoneId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, PhoneId> {
    List<Phone> findByEmployeeEmpId(Long empId);
}
