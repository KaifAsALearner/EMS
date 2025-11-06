package five.ind.ems_mvc.service.impl;

import five.ind.ems_mvc.entity.Asset;
import five.ind.ems_mvc.repository.AssetRepository;
import five.ind.ems_mvc.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetServiceImpl implements AssetService {
    @Autowired
    private AssetRepository assetRepository;

    @Override
    public Asset createAsset(Asset asset) {
        return assetRepository.save(asset);
    }

    @Override
    public Asset updateAsset(Long assetId, Asset updatedAsset) {
        Optional<Asset> optional = assetRepository.findById(assetId);
        if (optional.isPresent()) {
            Asset asset = optional.get();
            asset.setName(updatedAsset.getName());
            asset.setCategory(updatedAsset.getCategory());
            asset.setPurchaseDate(updatedAsset.getPurchaseDate());
            asset.setCost(updatedAsset.getCost());
            asset.setStatus(updatedAsset.getStatus());
            asset.setWarrantyExp(updatedAsset.getWarrantyExp());
            return assetRepository.save(asset);
        }
        return null;
    }

    @Override
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    @Override
    public Asset getAssetById(Long assetId) {
        return assetRepository.findById(assetId).orElse(null);
    }
}
