package com.ems.ems.entity;

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

    private String name;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    private String desc;
    private String priority;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate dueDate;
}
