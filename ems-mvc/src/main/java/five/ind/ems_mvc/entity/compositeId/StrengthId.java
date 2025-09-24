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
public class StrengthId implements Serializable {
    private Long reviewId;
    private String strength;
}
