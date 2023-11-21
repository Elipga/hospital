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
public class DoctorOutput {

    @NotNull(message = "DNI is null")
    @NotEmpty(message = "DNI is null")
    private String dni;
    @NotNull(message = "College number is null")
    @NotEmpty(message = "College number is null")
    @Pattern(regexp="^[0-9]+$", message = "College Number contains only numbers")
    private String collegeNumber;
    @NotNull(message = "Name is null")
    @NotEmpty(message = "Name is null")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name contains only letters")
    private String name;
    @NotNull(message = "Starting time is null")
    @Schema(type = "String", pattern = "HH:mm:SS")
    private LocalTime startingTime;
    @NotNull(message = "Ending time is null")
    @Schema(type = "String", pattern = "HH:mm:SS")
    private LocalTime endingTime;

    public DoctorOutput(String dni, String collegeNumber, String name, LocalTime startingTime, LocalTime endingTime) throws InvalidException {
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

    public static DoctorOutput getDoctor(Doctor doctor) throws InvalidException {
        return new DoctorOutput(doctor.getDni(), doctor.getCollegeNumber(), doctor.getName(), doctor.getStartingTime(),
                doctor.getEndingTime());
    }
}
