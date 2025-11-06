package five.ind.ems_mvc.service;

import five.ind.ems_mvc.entity.AssetAssigned;

import java.util.List;

public interface AssetAssignedService {
    AssetAssigned assignAsset(AssetAssigned assetAssigned);
    List<AssetAssigned> getAssetsByEmployee(Long empId);
    List<AssetAssigned> getAssignmentsByAsset(Long assetId);
}
