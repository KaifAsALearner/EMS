package com.ems.ems.entity;

import com.ems.ems.entity.compositeId.RoleId;
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
@Table(name = "roles")
public class Role {
    @EmbeddedId
    private RoleId id;

    @ManyToOne
    @JoinColumn(name = "deptId", referencedColumnName = "deptId", insertable = false, updatable = false)
    private Department department;

    private String seniority;

    private Double baseSalary;
}
