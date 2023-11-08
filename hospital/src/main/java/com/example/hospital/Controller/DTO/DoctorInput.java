package com.example.hospital.Controller.DTO;

import com.example.hospital.Domain.Doctor;
import com.example.hospital.Domain.HealthStaff;
import com.example.hospital.Exception.InvalidException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class DoctorInput {
    @NotNull(message = "ID is null")
    @NotEmpty(message = "ID is null")
    private String id;
    @NotNull(message = "College number is null")
    @NotEmpty(message = "College number is null")
    private String collegeNumber;

    @NotNull(message = "Name is null")
    @NotEmpty(message = "Name is null")
    private String name;

    @Min(value = 0, message = "Minimum years experience is 0")
    @Max(value = 100, message = "Maximum years experience is 100")
    private byte yearsExperience;

    @NotNull(message = "Starting time is null")
    //@Min(value = 8, message = "Minimum starting time is 8h")
    //@Max(value = 14, message = "Maximum starting time is 14h")
    private LocalTime startingTime;

    @NotNull(message = "Ending time is null")
    //@Min(value = 14, message = "Minimum ending time is 14h")
    //@Max(value = 20, message = "Maximum ending time is 20h")
    private LocalTime endingTime;

    public DoctorInput(String id, String collegeNumber, String name, byte yearsExperience, LocalTime startingTime, LocalTime endingTime) throws InvalidException {
        if((id.length() != 9)) throw new InvalidException("Id has 9 chars");
        if(!id.matches("[0-9]{8}[A-Za-z]"))throw new InvalidException("ID must contain 8 numbers" +
                "and 1 letter");
        this.id = id;
        this.collegeNumber = collegeNumber;
        this.name = name;
        this.yearsExperience = yearsExperience;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
    }

    public static Doctor getDoctor(DoctorInput doctorInput) throws InvalidException {
        return new Doctor(doctorInput.getId(),doctorInput.getCollegeNumber(), doctorInput.getName(),
                doctorInput.getStartingTime(), doctorInput.getEndingTime(), doctorInput.getYearsExperience());
    }
}
