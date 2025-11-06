package five.ind.ems_mvc.service;

import five.ind.ems_mvc.entity.Asset;

import java.util.List;

public interface AssetService {
    Asset createAsset(Asset asset);
    Asset updateAsset(Long assetId, Asset asset);
    List<Asset> getAllAssets();
    Asset getAssetById(Long assetId);
}
