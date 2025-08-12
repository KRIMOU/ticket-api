package com.pluralsight.ticket_api.repository;

import com.pluralsight.ticket_api.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class StudentRepositoryTest {
    @Autowired
    public StudentRepository studentRepository;


    @Test
    @Sql({"/filterTestData.sql"})
    void givenStatus_whenGettingTickets_thenTicketsWithMatchingStatusAreReturned(){
        List<Student> studentList = studentRepository.findAll();
        assertEquals(1 , studentList.size());
    }

    @Test
    @Sql({"/filterTestData.sql"})
    void givenId_whenGettingStudents_thenStudentAreReturned(){
        //when the relation is lazy : Hibernate:
        //    select
        //        s1_0.id,
        //        s1_0.name,
        //        s1_0.passport_id
        //    from
        //        student s1_0
        //    where
        //        s1_0.id=?
        //Hibernate:
        //    select
        //        p1_0.id,
        //        p1_0.name
        //    from
        //        passport p1_0
        //    where
        //        p1_0.id=?

        Optional<Student> student = studentRepository.findById(1L);
        //when eager the result of request is Hibernate:
        //    select
        //        s1_0.id,
        //        s1_0.name,
        //        p1_0.id,
        //        p1_0.name
        //    from
        //        student s1_0
        //    left join
        //        passport p1_0
        //            on p1_0.id=s1_0.passport_id
        //    where
        //        s1_0.id=?
        assertEquals("name" , student.get().getPassport().getName());
    }
}
