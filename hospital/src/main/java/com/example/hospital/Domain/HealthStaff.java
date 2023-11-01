package com.example.hospital.Domain;

import com.example.hospital.Exception.InvalidException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@Table(name = "health_staff")
@NoArgsConstructor
public class HealthStaff {
    @Id
    @NotNull(message = "ID is null")
    @NotEmpty(message = "ID is null")
    private String id;
    @NotNull(message = "College number is null")
    @NotEmpty(message = "College number is null")
    private String collegeNumber;

    private String name;

    @NotNull(message = "Starting time is null")
    //@Min(value = 8, message = "Minimum starting time is 8h")
    //@Max(value = 14, message = "Maximum starting time is 14h")
    private LocalTime startingTime;

    @NotNull(message = "Ending time is null")
    //@Min(value = 14, message = "Minimum ending time is 14h")
    //@Max(value = 20, message = "Maximum ending time is 20h")
    private LocalTime endingTime;

    public HealthStaff(String id, String collegeNumber, LocalTime startingTime, LocalTime endingTime) throws InvalidException {
        validateId(id);
        this.id = id;
        this.collegeNumber = collegeNumber;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
    }

    public HealthStaff(String id, String collegeNumber, String name, LocalTime startingTime, LocalTime endingTime) throws InvalidException {
        validateId(id);
        this.id = id;
        this.collegeNumber = collegeNumber;
        this.name = name;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
    }

    private static void validateId(String id) throws InvalidException {
        if((id.length() != 9)) throw new InvalidException("Id has 9 chars");
        if(!id.matches("[0-9]{8}[A-Za-z]"))throw new InvalidException("ID must contain 8 numbers" +
                "and 1 letter");
    }
}
