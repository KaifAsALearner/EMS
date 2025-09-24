package five.ind.ems_mvc.entity.compositeId;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AssetAssignedId implements Serializable {
    private Long assetId;
    private Long empId;
}
