package five.ind.ems_mvc.service.impl;

import five.ind.ems_mvc.entity.AssetAssigned;
import five.ind.ems_mvc.repository.AssetAssignedRepository;
import five.ind.ems_mvc.service.AssetAssignedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetAssignedServiceImpl implements AssetAssignedService {
    @Autowired
    private AssetAssignedRepository repo;

    @Override
    public AssetAssigned assignAsset(AssetAssigned assetAssigned) {
        return repo.save(assetAssigned);
    }

    @Override
    public List<AssetAssigned> getAssetsByEmployee(Long empId) {
        return repo.findByEmployee_EmpId(empId);
    }

    @Override
    public List<AssetAssigned> getAssignmentsByAsset(Long assetId) {
        return repo.findByAsset_AssetId(assetId);
    }
}