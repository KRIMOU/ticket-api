package com.pluralsight.ticket_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Cacheable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Cacheable
@NamedQueries(value = {
        @NamedQuery(name = "query_get_all_courses",
                query = "Select  c  From Course c"),
        @NamedQuery(name = "query_get_all_courses_join_fetch",
                query = "Select  c  From Course c JOIN FETCH c.students s"),
        @NamedQuery(name = "query_get_100_Step_courses",
                query = "Select  c  From Course c where name like '%100 Steps'") })
public class Course {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedDate;

    @CreationTimestamp
    private LocalDateTime createdDate;

    private boolean isDeleted;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<Review> previewList = new ArrayList<>();

    @ManyToMany(mappedBy="courses")
    private List<Student> students = new ArrayList<>();


}
