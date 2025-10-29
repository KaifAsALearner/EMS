package five.ind.ems_mvc.entity;

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
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;
}
