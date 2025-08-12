package com.pluralsight.ticket_api.repository;

import com.pluralsight.ticket_api.DAO.jdbc.PersonRepository;
import com.pluralsight.ticket_api.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = DatabaseContextTest.class)
public class PersonRepositoryTest {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    public JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        String sql = "INSERT INTO person (id,name, location, birth_date) VALUES (?,?, ?, ?)";
        jdbcTemplate.update(sql,1, "John Doe", "Paris", new Date());
    }

    @Test
    void givenPersonId_whenFetchPerson_thenReturnPerson(){
        assertNotNull(personRepository);
        assertEquals("John Doe",personRepository.findById(1).getName());
    }

    @Test
    void givenNothing_whenFetchPersons_thenReturnPersons(){
        assertNotNull(personRepository);
        assertEquals(1,personRepository.findAll().size());
    }

    @Test
    void givenIdPerson_whenDeletePerson_thenReturnNothing(){
        personRepository.deleteById(1);
        assertEquals(0,personRepository.findAll().size());
    }

    @Test
    void givenIdPerson_whenInsertPerson_thenReturnPersons(){
        Person person = new Person();
        person.setId(2);
        person.setLocation("tizi");
        person.setName("krimou");
        personRepository.insertPerson(person);
        assertEquals(2,personRepository.findAll().size());
    }

}
