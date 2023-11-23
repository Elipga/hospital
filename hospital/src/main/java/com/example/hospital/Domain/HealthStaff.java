package com.example.hospital.Domain;

import com.example.hospital.Controller.DTO.Validation;
import com.example.hospital.Exception.InvalidException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalTime;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) //Each class in the inheritance hierarchy has its own table in the database
@Getter
@NoArgsConstructor
public class HealthStaff {
    @NotNull(message = "DNI is null")
    @NotEmpty(message = "DNI is null")
    private String dni;
    @Id
    @NotNull(message = "College number is null")
    @NotEmpty(message = "College number is null")
    @Pattern(regexp="^[0-9]+$")
    private String collegeNumber;
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name contains only letters")
    private String name;
    @NotNull(message = "Starting time is null")
    private LocalTime startingTime;
    @NotNull(message = "Ending time is null")
    private LocalTime endingTime;
    public HealthStaff(String dni, String collegeNumber, LocalTime startingTime, LocalTime endingTime) throws InvalidException {
        Validation validation = new Validation();
        validation.validateDni(dni);
        this.dni = dni;
        validation.validateCNumber(collegeNumber);
        this.collegeNumber = collegeNumber;
        validation.validateTime(startingTime);
        this.startingTime = startingTime;
        validation.validateTime(endingTime);
        this.endingTime = endingTime;
        validation.validateTime(startingTime,endingTime);
    }
    public HealthStaff(String dni, String collegeNumber, String name, LocalTime startingTime, LocalTime endingTime) throws InvalidException {
        Validation validation = new Validation();
        validation.validateDni(dni);
        this.dni = dni;
        validation.validateCNumber(collegeNumber);
        this.collegeNumber = collegeNumber;
        this.name = name;
        validation.validateTime(startingTime);
        this.startingTime = startingTime;
        validation.validateTime(endingTime);
        this.endingTime = endingTime;
        validation.validateTime(startingTime,endingTime);
    }

    public void setStartingTime(LocalTime startingTime) {
        this.startingTime = startingTime;
    }

    public void setEndingTime(LocalTime endingTime) {
        this.endingTime = endingTime;
    }
}
