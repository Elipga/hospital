package com.example.hospital.Controller;

import com.example.hospital.Controller.DTO.DoctorInput;
import com.example.hospital.Controller.DTO.DoctorOutput;
import com.example.hospital.Controller.DTO.DoctorOutputCNumberAndTimetable;
import com.example.hospital.Controller.DTO.HealthStaffUpdate;
import com.example.hospital.Exception.*;
import com.example.hospital.Service.DoctorService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorOutput>> getAllDoctors() {
        try {
            List<DoctorOutput> doctors = doctorService.getAllDoctors();
            return ResponseEntity.ok(doctors);
        } catch (IsEmptyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/doctors")
    public ResponseEntity<String> addDoctor(@Valid @RequestBody DoctorInput doctorInput) {
        try {
            doctorService.addDoctor(doctorInput);
            return ResponseEntity.ok().build();
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
        } catch (InvalidException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
    }

    @PutMapping("/doctors/{collegeNumber}")
    public ResponseEntity<DoctorOutputCNumberAndTimetable> setTimetableOfDoctor(@PathVariable String collegeNumber,
                                                                                @RequestBody HealthStaffUpdate healthStaffUpdate) {
        try {
            DoctorOutputCNumberAndTimetable doctor = doctorService.setTimeTableOfDoctor(collegeNumber, healthStaffUpdate);
            return ResponseEntity.ok(doctor);
        } catch (StaffDoesNotExists e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DoctorDoesNotExists e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
