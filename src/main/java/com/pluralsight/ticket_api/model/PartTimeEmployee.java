package com.pluralsight.ticket_api.model;

import java.math.BigDecimal;
import jakarta.persistence.Entity;

@Entity
public class PartTimeEmployee extends Employee {

    protected PartTimeEmployee() {
    }

    public PartTimeEmployee(String name, BigDecimal hourlyWage) {
        super(name);
        this.hourlyWage = hourlyWage;
    }

    private BigDecimal hourlyWage;

}
