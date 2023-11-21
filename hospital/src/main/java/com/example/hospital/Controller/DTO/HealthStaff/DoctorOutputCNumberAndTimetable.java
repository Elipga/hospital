package com.example.hospital.Controller.DTO.HealthStaff;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class DoctorOutputCNumberAndTimetable {
    private String collegeNumber;
    private LocalTime startingTime;
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
