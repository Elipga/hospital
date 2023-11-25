package com.example.hospital.Controller;

import com.example.hospital.Controller.DTO.HealthStaff.DoctorInput;
import com.example.hospital.Controller.DTO.HealthStaff.DoctorOutput;
import com.example.hospital.Controller.DTO.HealthStaff.HealthStaffOutputCNumberAndTimetable;
import com.example.hospital.Controller.DTO.HealthStaff.HealthStaffUpdate;
import com.example.hospital.Exception.*;
import com.example.hospital.Service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.TreeMap;

@RestController
public class DoctorController {
    @Autowired
    DoctorService doctorService;
    @Operation(summary = "Add doctor")
    @PostMapping("/doctors")
    public ResponseEntity<String> addDoctor(@Valid @RequestBody DoctorInput doctorInput) throws AlreadyExistsException, InvalidException {
            doctorService.addDoctor(doctorInput);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Operation(summary = "Get all doctors")
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorOutput>> getAllDoctors() throws IsEmptyException, InvalidException {
            List<DoctorOutput> doctors = doctorService.getAllDoctors();
            return ResponseEntity.ok(doctors);
    }
    @Operation(summary = "Get doctor by college number")
    @GetMapping("/doctors/{collegeNumber}")
    public ResponseEntity<DoctorOutput> getDoctorById(@PathVariable String collegeNumber) throws InvalidException, DoctorDoesNotExists {
            DoctorOutput doctorOutput = doctorService.getDoctorById(collegeNumber);
            return ResponseEntity.ok(doctorOutput);
    }

    @Operation(summary = "Set timetable of doctor", description = "Update Starting and Ending time")
    @PutMapping("/doctors/{collegeNumber}")
    public ResponseEntity<HealthStaffOutputCNumberAndTimetable> setTimetableOfDoctor(@PathVariable String collegeNumber,
                                                                                     @RequestBody HealthStaffUpdate healthStaffUpdate) throws InvalidException, DoctorDoesNotExists, AlreadyExistsException {
            HealthStaffOutputCNumberAndTimetable doctor = doctorService.setTimeTableOfDoctor(collegeNumber, healthStaffUpdate);
            return ResponseEntity.ok(doctor);
    }
    @Operation(summary = "Get availability of doctor for next temporal window")
    @GetMapping("/doctors/{collegeNumber}/availabilities")
    public ResponseEntity <TreeMap<LocalDate, List<LocalTime>>> getAvailabilityOfDoctor(@PathVariable String collegeNumber) throws DoctorDoesNotExists, StaffDoesNotExists {
        TreeMap<LocalDate, List<LocalTime>> availabilityOutputs = null;
        availabilityOutputs = doctorService.getAvailabilityOfDoctor(collegeNumber);
        return ResponseEntity.ok(availabilityOutputs);
    }

    @Operation(summary = "Get busiest doctors for next temporal window")
    @GetMapping("doctors/busy")
    public ResponseEntity<TreeMap<String, Integer>> getBusiestDoctors() throws InvalidException {
        TreeMap<String, Integer> doctors = null;
        doctors = doctorService.getBusiestDoctors();
        return ResponseEntity.ok(doctors);
    }
}
