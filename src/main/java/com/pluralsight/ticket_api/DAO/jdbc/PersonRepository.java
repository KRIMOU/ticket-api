package com.pluralsight.ticket_api.DAO.jdbc;

import com.pluralsight.ticket_api.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class PersonRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    static class PersonRowMapper implements RowMapper<Person> {
        @Override
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person person = new Person();
            person.setId(rs.getInt("id"));
            person.setName(rs.getString("name"));
            person.setLocation(rs.getString("location"));
            person.setBirthDate(rs.getTimestamp("birth_date"));
            return person;
        }

    }

    public Person findById(int id) {
        return jdbcTemplate.queryForObject("select * from person where id=?", new BeanPropertyRowMapper<>(Person.class), id);
    }

}
