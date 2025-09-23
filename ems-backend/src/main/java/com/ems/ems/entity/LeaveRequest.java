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
@Table(name = "leave_requests")
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reqId;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;

    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LocalDate dateOfApproval;
}
