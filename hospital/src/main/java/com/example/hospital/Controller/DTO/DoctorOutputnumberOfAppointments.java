package com.example.hospital.Controller.DTO;

import com.example.hospital.Domain.Doctor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class DoctorOutputnumberOfAppointments {

    @NotNull(message = "College number is null")
    @NotEmpty(message = "College number is null")
    private String collegeNumber;
    private long numberOfAppointments;

    public DoctorOutputnumberOfAppointments(String collegeNumber, long numberOfAppointments) {
        this.collegeNumber = collegeNumber;
        this.numberOfAppointments = numberOfAppointments;
    }

    public static DoctorOutputnumberOfAppointments getDoctor(Doctor doctor, long numberOfAppointments) {
        return new DoctorOutputnumberOfAppointments(doctor.getCollegeNumber(), numberOfAppointments);
    }
}
