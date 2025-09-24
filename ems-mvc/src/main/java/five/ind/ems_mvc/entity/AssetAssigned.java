package five.ind.ems_mvc.entity;

import five.ind.ems_mvc.entity.compositeId.AssetAssignedId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "asset_assigned")
public class AssetAssigned {
    @EmbeddedId
    private AssetAssignedId id;

    @ManyToOne
    @JoinColumn(name = "assetId", referencedColumnName = "assetId", insertable = false, updatable = false)
    private Asset asset;

    @ManyToOne
    @JoinColumn(name = "empId", referencedColumnName = "empId", insertable = false, updatable = false)
    private Employee employee;

    private LocalDate issuedOn;
    private LocalDate returnedOn;
    private String remark;
}
