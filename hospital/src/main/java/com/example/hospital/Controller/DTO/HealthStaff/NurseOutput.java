package com.example.hospital.Controller.DTO.HealthStaff;

import com.example.hospital.Domain.Nurse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class NurseOutput {
    private String dni;
    private String collegeNumber;
    private String name;
    private LocalTime startingTime;
    private LocalTime endingTime;
    public NurseOutput(String dni, String collegeNumber, String name, LocalTime startingTime, LocalTime endingTime) {
        this.dni = dni;
        this.collegeNumber = collegeNumber;
        this.name = name;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
    }
    public static NurseOutput getNurse(Nurse nurse) {
        return new NurseOutput(nurse.getDni(),nurse.getCollegeNumber(), nurse.getName(), nurse.getStartingTime(),
                nurse.getEndingTime());
    }
}
