package com.example.hospital.Domain;

import com.example.hospital.Exception.InvalidException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalTime;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
public class Doctor extends HealthStaff {
    @Min(value = 0, message = "Minimum years experience is 0")
    @Max(value = 100, message = "Maximum years experience is 100")
    private int yearsExperience;
    public Doctor(String dni, String collegeNumber, LocalTime startingTime, LocalTime endingTime, int yearsExperience) throws InvalidException {
        super(dni, collegeNumber, startingTime, endingTime);
        this.yearsExperience = yearsExperience;
    }
    public Doctor(String dni, String collegeNumber, String name, LocalTime startingTime, LocalTime endingTime, int yearsExperience) throws InvalidException {
        super(dni, collegeNumber, name, startingTime, endingTime);
        this.yearsExperience = yearsExperience;
    }
}
