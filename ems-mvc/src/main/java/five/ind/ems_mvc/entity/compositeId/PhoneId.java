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
public class PhoneId implements Serializable {
    private Long empId;
    private String phoneNo;
}
