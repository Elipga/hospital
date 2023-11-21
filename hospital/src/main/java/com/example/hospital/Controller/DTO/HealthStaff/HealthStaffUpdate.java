package com.example.hospital.Controller.DTO.HealthStaff;

import com.example.hospital.Controller.DTO.Validation;
import com.example.hospital.Exception.InvalidException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class HealthStaffUpdate {
    @NotNull(message = "Starting time is null")
    @Schema(type = "String", pattern = "HH:mm:SS")
    private LocalTime startingTime;
    @NotNull(message = "Ending time is null")
    @Schema(type = "String", pattern = "HH:mm:SS")
    private LocalTime endingTime;
    public HealthStaffUpdate(LocalTime startingTime, LocalTime endingTime) throws InvalidException {
        Validation validation = new Validation();
        validation.validateTime(startingTime);
        this.startingTime = startingTime;
        validation.validateTime(endingTime);
        this.endingTime = endingTime;
        validation.validateTime(startingTime,endingTime);
    }
}
