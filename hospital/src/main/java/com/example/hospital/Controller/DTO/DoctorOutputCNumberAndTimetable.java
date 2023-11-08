package com.example.hospital.Controller.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class DoctorOutputCNumberAndTimetable {

    @NotNull(message = "College number is null")
    @NotEmpty(message = "College number is null")
    private String collegeNumber;

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

    public DoctorOutputCNumberAndTimetable(String collegeNumber, LocalTime startingTime, LocalTime endingTime) {
        this.collegeNumber = collegeNumber;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
    }

    public static DoctorOutputCNumberAndTimetable getDoctor(String collegeNumber, HealthStaffUpdate healthStaffUpdate) {
        return new DoctorOutputCNumberAndTimetable(collegeNumber, healthStaffUpdate.getStartingTime(),
                healthStaffUpdate.getEndingTime());
    }
}
