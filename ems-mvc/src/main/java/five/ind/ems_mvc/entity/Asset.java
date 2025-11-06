package five.ind.ems_mvc.entity;

import five.ind.ems_mvc.entity.enums.AssetStatus;
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
@Table(name = "assets")
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assetId;

    private String name;
    private String category;
    private LocalDate purchaseDate;
    private Double cost;

    @Enumerated(EnumType.STRING)
    private AssetStatus status = AssetStatus.AVAILABLE;

    private LocalDate warrantyExp;
}
