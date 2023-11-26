package com.example.hospital.Controller.DTO.HealthStaff;

import com.example.hospital.Controller.DTO.Validation;
import com.example.hospital.Domain.Doctor;
import com.example.hospital.Exception.InvalidException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class DoctorInput {
    @NotNull(message = "DNI is null")
    @NotEmpty(message = "DNI is null")
    private String dni;
    @NotNull(message = "College number is null")
    @NotEmpty(message = "College number is null")
    @Pattern(regexp="^[0-9]+$", message = "College Number contains only numbers")
    private String collegeNumber;
    @NotNull(message = "Name is null")
    @NotEmpty(message = "Name is null")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name contains only letters without accent mark")
    private String name;
    @Min(value = 0, message = "Minimum years experience is 0")
    @Max(value = 100, message = "Maximum years experience is 100")
    private int yearsExperience;
    @NotNull(message = "Starting time is null")
    @Schema(type = "String", pattern = "HH:mm:SS")
    private LocalTime startingTime;
    @NotNull(message = "Ending time is null")
    @Schema(type = "String", pattern = "HH:mm:SS")
    private LocalTime endingTime;

    public DoctorInput(String dni, String collegeNumber, String name, int yearsExperience, LocalTime startingTime, LocalTime endingTime) throws InvalidException {
        Validation validation = new Validation();
        validation.validateDni(dni);
        this.dni = dni;
        validation.validateCNumber(collegeNumber);
        this.collegeNumber = collegeNumber;
        this.name = name;
        validation.validateYearsExperience(yearsExperience);
        this.yearsExperience = yearsExperience;
        validation.validateTime(startingTime);
        this.startingTime = startingTime;
        validation.validateTime(endingTime);
        this.endingTime = endingTime;
        validation.validateTime(startingTime,endingTime);
    }
    public static Doctor getDoctor(DoctorInput doctorInput) throws InvalidException {
        return new Doctor(doctorInput.getDni(),doctorInput.getCollegeNumber(), doctorInput.getName(),
                doctorInput.getStartingTime(), doctorInput.getEndingTime(), doctorInput.getYearsExperience());
    }
}
