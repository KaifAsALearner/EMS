package com.ems.ems.entity.compositeId;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RoleId implements Serializable {
    private Long deptId;
    private String roleName;
}
