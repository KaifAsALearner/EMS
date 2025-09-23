package com.ems.ems.entity;

import com.ems.ems.entity.compositeId.PhoneId;
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
