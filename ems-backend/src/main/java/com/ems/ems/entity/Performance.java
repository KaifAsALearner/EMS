package com.ems.ems.entity;

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
@Table(name = "performance_reviews")
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private String review;
    private String rating;

    @ManyToOne
    @JoinColumn(name = "of_emp_id")
    private Employee ofEmp; // Employee being reviewed

    @ManyToOne
    @JoinColumn(name = "by_emp_id")
    private Employee byEmp; // Employee who performed the review
}
