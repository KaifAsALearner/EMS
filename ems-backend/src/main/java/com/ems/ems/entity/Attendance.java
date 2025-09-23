package com.ems.ems.entity;

import com.ems.ems.entity.compositeId.AttendanceId;
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
@Table(name = "attendance")
public class Attendance {
    @EmbeddedId
    private AttendanceId id;

    @ManyToOne
    @JoinColumn(name = "empId", referencedColumnName = "empId", insertable = false, updatable = false)
    private Employee employee;

    private String checkIn; // Assuming time can be a string
    private String checkOut; // Assuming time can be a string
}
