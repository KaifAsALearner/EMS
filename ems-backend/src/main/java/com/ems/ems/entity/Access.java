package com.ems.ems.entity;

import com.ems.ems.entity.compositeId.AccessId;
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
@Table(name = "access_control")
public class Access {
    @EmbeddedId
    private AccessId id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "deptId", referencedColumnName = "deptId", insertable = false, updatable = false),
            @JoinColumn(name = "roleName", referencedColumnName = "roleName", insertable = false, updatable = false)
    })
    private Role role;

    private String access;
}
