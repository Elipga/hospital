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
    private byte yearsExperience;

    private long numberOfAppointments;

    public Doctor(String id, String collegeNumber, LocalTime startingTime, LocalTime endingTime, byte yearsExperience) throws InvalidException {
        super(id, collegeNumber, startingTime, endingTime);
        this.yearsExperience = yearsExperience;
        this.numberOfAppointments = 0;
    }

    public Doctor(String id, String collegeNumber, String name, LocalTime startingTime, LocalTime endingTime, byte yearsExperience) throws InvalidException {
        super(id, collegeNumber, name, startingTime, endingTime);
        this.yearsExperience = yearsExperience;
        this.numberOfAppointments = 0;
    }
}
