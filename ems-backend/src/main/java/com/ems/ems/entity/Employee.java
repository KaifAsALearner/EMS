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
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empId;

    // Foreign key to Department
    //    @ManyToOne
    //    @JoinColumn(name = "dept_id")
    //    private Department department;

    // Foreign key to Role (using the composite key)
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "deptId", referencedColumnName = "deptId"),
            @JoinColumn(name = "roleName", referencedColumnName = "roleName")
    })
    private Role role;

    // Self-referencing foreign key for Manager
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    private String fName;
    private String lName;
    private String gender;
    private LocalDate dob;
    private LocalDate hiredDate;
    private Double bonus;
}

