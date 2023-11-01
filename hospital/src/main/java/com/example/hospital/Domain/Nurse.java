package com.example.hospital.Domain;


import com.example.hospital.Exception.InvalidException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalTime;

@Entity
@Table(name = "nurses")
@Getter
@Setter
@NoArgsConstructor
public class Nurse extends HealthStaff{

    public Nurse(String id, String collegeNumber, LocalTime startingTime, LocalTime endingTime) throws InvalidException {
        super(id, collegeNumber, startingTime, endingTime);
    }

    public Nurse(String id, String collegeNumber, String name, LocalTime startingTime, LocalTime endingTime) throws InvalidException {
        super(id, collegeNumber, name, startingTime, endingTime);
    }
}
