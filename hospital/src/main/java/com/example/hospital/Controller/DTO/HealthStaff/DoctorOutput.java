package com.example.hospital.Controller.DTO.HealthStaff;

import com.example.hospital.Domain.Doctor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class DoctorOutput {
    private String dni;
    private String collegeNumber;
    private String name;
    private LocalTime startingTime;
    private LocalTime endingTime;
    public DoctorOutput(String dni, String collegeNumber, String name, LocalTime startingTime, LocalTime endingTime) {
        this.dni = dni;
        this.collegeNumber = collegeNumber;
        this.name = name;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
    }
    public static DoctorOutput getDoctor(Doctor doctor){
        return new DoctorOutput(doctor.getDni(), doctor.getCollegeNumber(), doctor.getName(), doctor.getStartingTime(),
                doctor.getEndingTime());
    }
}
