package five.ind.ems_mvc.entity;

import five.ind.ems_mvc.entity.compositeId.WeaknessId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "weaknesses")
public class Weakness {
    @EmbeddedId
    private WeaknessId id;

    @ManyToOne
    @JoinColumn(name = "reviewId", referencedColumnName = "reviewId", insertable = false, updatable = false)
    private Performance performance;
}
