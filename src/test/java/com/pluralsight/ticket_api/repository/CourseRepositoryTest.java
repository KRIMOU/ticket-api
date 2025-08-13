package com.pluralsight.ticket_api.repository;

import com.pluralsight.ticket_api.model.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
public class CourseRepositoryTest {
    @Autowired
    CourseRepository courseRepository;


    @Test
    @Sql({"/filterTestData.sql"})
    void givenStatus_whenGettingTickets_thenTicketsWithMatchingStatusAreReturned(){
        Optional<Course> course = courseRepository.findById(1L);
        assertFalse(course.isPresent());
        //if i do the same call then the first cash layer comes to play and it will retreive same data if i put @transactional
    }

    @Test
    void sort(){
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        courseRepository.findAll(sort);
    }

    @Test
    void pagination(){
        PageRequest pagination = PageRequest.of(0,4);
        Page<Course> firstPage = courseRepository.findAll(pagination);
        Pageable secondPage = firstPage.nextPageable();
    }

}
