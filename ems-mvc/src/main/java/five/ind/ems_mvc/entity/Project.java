package five.ind.ems_mvc.entity;

import five.ind.ems_mvc.entity.enums.ProjectStatus;
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
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

//    @ManyToOne
//    @JoinColumn(name = "dept_id") // department reference
//    private Department department;

    @Column(name = "description", length = 100)
    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private String priority;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate dueDate;
}
