package com.example.hospital.Controller.DTO.HealthStaff;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class NurseOutputCNumberAndTimetable {
    private String collegeNumber;
    private LocalTime startingTime;
    private LocalTime endingTime;
    public NurseOutputCNumberAndTimetable(String collegeNumber, LocalTime startingTime, LocalTime endingTime) {
        this.collegeNumber = collegeNumber;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
    }
    public static NurseOutputCNumberAndTimetable getNurse(String collegeNumber, HealthStaffUpdate healthStaffUpdate) {
        return new NurseOutputCNumberAndTimetable(collegeNumber, healthStaffUpdate.getStartingTime(),
                healthStaffUpdate.getEndingTime());
    }
}
