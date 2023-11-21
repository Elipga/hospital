package com.example.hospital.Controller.DTO.HealthStaff;

import com.example.hospital.Controller.DTO.Validation;
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
public class HealthStaffOutputCNumberAndTimetable {
    @NotNull(message = "College number is null")
    @NotEmpty(message = "College number is null")
    @Pattern(regexp="^[0-9]+$", message = "College Number contains only numbers")
    private String collegeNumber;
    @NotNull(message = "Starting time is null")
    @Schema(type = "String", pattern = "HH:mm:SS")
    private LocalTime startingTime;
    @NotNull(message = "Ending time is null")
    @Schema(type = "String", pattern = "HH:mm:SS")
    private LocalTime endingTime;
    public HealthStaffOutputCNumberAndTimetable(String collegeNumber, LocalTime startingTime, LocalTime endingTime) throws InvalidException {
        Validation validation = new Validation();
        validation.validateCNumber(collegeNumber);
        this.collegeNumber = collegeNumber;
        validation.validateTime(startingTime);
        this.startingTime = startingTime;
        validation.validateTime(endingTime);
        this.endingTime = endingTime;
        validation.validateTime(startingTime,endingTime);
    }
    public static HealthStaffOutputCNumberAndTimetable getHealthStaff(String collegeNumber, HealthStaffUpdate healthStaffUpdate) throws InvalidException {
        return new HealthStaffOutputCNumberAndTimetable(collegeNumber, healthStaffUpdate.getStartingTime(),
                healthStaffUpdate.getEndingTime());
    }
}
