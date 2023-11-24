package com.example.hospital.Controller.DTO.HealthStaff;

import com.example.hospital.Controller.DTO.Validation;
import com.example.hospital.Domain.Nurse;
import com.example.hospital.Exception.InvalidException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class NurseInput {
    @NotNull(message = "DNI is null")
    @NotEmpty(message = "DNI is null")
    private String dni;
    @NotNull(message = "College number is null")
    @NotEmpty(message = "College number is null")
    @Pattern(regexp="^[0-9]+$", message = "College Number contains only numbers")
    private String collegeNumber;
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name contains only letters")
    private String name;
    @NotNull(message = "Starting time is null")
    @Schema(type = "String", pattern = "HH:mm:SS") //format of time input as a String
    private LocalTime startingTime;
    @NotNull(message = "Ending time is null")
    @Schema(type = "String", pattern = "HH:mm:SS")
    private LocalTime endingTime;

    public NurseInput(String dni, String collegeNumber, String name, LocalTime startingTime, LocalTime endingTime) throws InvalidException {
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
    public static Nurse getNurse(NurseInput nurseInput) throws InvalidException {
        return new Nurse(nurseInput.getDni(), nurseInput.getCollegeNumber(), nurseInput.name, nurseInput.startingTime,
                nurseInput.getEndingTime());
    }
}
