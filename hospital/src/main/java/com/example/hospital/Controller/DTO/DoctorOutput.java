package com.example.hospital.Controller.DTO;

import com.example.hospital.Domain.Doctor;
import com.example.hospital.Domain.Timetable;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Getter
public class DoctorOutput {
    @NotNull(message = "ID is null")
    @NotEmpty(message = "ID is null")
    private String id;

    private String name;

    @NotNull(message = "Starting time is null")
    @NotEmpty(message = "Starting time is null")
    //@Min(value = 8, message = "Minimum starting time is 8h")
    //@Max(value = 14, message = "Maximum starting time is 14h")
    private LocalTime startingTime;

    @NotNull(message = "Ending time is null")
    @NotEmpty(message = "Ending time is null")
    //@Min(value = 14, message = "Minimum ending time is 14h")
    //@Max(value = 20, message = "Maximum ending time is 20h")
    private LocalTime endingTime;

    public DoctorOutput(String id, String name, LocalTime startingTime, LocalTime endingTime) {
        this.id = id;
        this.name = name;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
    }

    public static DoctorOutput getDoctor(Doctor doctor) {

        return new DoctorOutput(doctor.getId(), doctor.getName(), doctor.getStartingTime(),
                doctor.getEndingTime());
    }
}
