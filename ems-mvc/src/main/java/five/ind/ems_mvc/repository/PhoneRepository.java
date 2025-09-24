package five.ind.ems_mvc.repository;

import five.ind.ems_mvc.entity.Phone;
import five.ind.ems_mvc.entity.compositeId.PhoneId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, PhoneId> {
}
