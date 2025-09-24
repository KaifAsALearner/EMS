package five.ind.ems_mvc.repository;

import five.ind.ems_mvc.entity.AssetAssigned;
import five.ind.ems_mvc.entity.compositeId.AssetAssignedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetAssignedRepository extends JpaRepository<AssetAssigned, AssetAssignedId> {
}
