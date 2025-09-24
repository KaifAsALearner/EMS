package five.ind.ems_mvc.entity;

import five.ind.ems_mvc.entity.compositeId.PhoneId;
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
@Table(name = "phones")
public class Phone {
    @EmbeddedId
    private PhoneId id;

    @ManyToOne
    @JoinColumn(name = "empId", referencedColumnName = "empId", insertable = false, updatable = false)
    private Employee employee;
}
