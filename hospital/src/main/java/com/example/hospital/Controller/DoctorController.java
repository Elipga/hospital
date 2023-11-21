package com.example.hospital.Controller;

import com.example.hospital.Controller.DTO.HealthStaff.DoctorInput;
import com.example.hospital.Controller.DTO.HealthStaff.DoctorOutput;
import com.example.hospital.Controller.DTO.HealthStaff.HealthStaffOutputCNumberAndTimetable;
import com.example.hospital.Controller.DTO.HealthStaff.HealthStaffUpdate;
import com.example.hospital.Exception.*;
import com.example.hospital.Service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Add doctor", responses = {
            @ApiResponse(responseCode = "201", description = "Doctor created"),
            @ApiResponse(responseCode = "208", description = "Health staff already reported"),
            @ApiResponse(responseCode = "412", description = "Precondition failed")}
    )
    @PostMapping("/doctors")
    public ResponseEntity<String> addDoctor(@Valid @RequestBody DoctorInput doctorInput) {
        try {
            doctorService.addDoctor(doctorInput);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(e.getMessage());
        } catch (InvalidException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        }
    }
    @Operation(summary = "Get all doctors", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Content not found")}
    )
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorOutput>> getAllDoctors() {
        try {
            List<DoctorOutput> doctors = doctorService.getAllDoctors();
            return ResponseEntity.ok(doctors);
        } catch (IsEmptyException e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @Operation(summary = "Get doctor by college number", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")}
    )
    @GetMapping("/doctors/{collegeNumber}")
    public ResponseEntity<DoctorOutput> getDoctorById(@PathVariable String collegeNumber){
        try {
            DoctorOutput doctorOutput = doctorService.getDoctorById(collegeNumber);
            return ResponseEntity.ok(doctorOutput);
        } catch (DoctorDoesNotExists e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Set timetable of doctor", description = "Update Starting and Ending time", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Health Staff not found") }
    )
    @PutMapping("/doctors/{collegeNumber}")
    public ResponseEntity<HealthStaffOutputCNumberAndTimetable> setTimetableOfDoctor(@PathVariable String collegeNumber,
                                                                                     @RequestBody HealthStaffUpdate healthStaffUpdate) {
        try {
            HealthStaffOutputCNumberAndTimetable doctor = doctorService.setTimeTableOfDoctor(collegeNumber, healthStaffUpdate);
            return ResponseEntity.ok(doctor);
        } catch (StaffDoesNotExists e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DoctorDoesNotExists e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidException e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
    }

    @Operation(summary = "Delete doctor", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Doctor not found") }
    )
    @DeleteMapping("/doctors/{collegeNumber}")
    public ResponseEntity<String> deleteDoctor(@PathVariable String collegeNumber){
        try {
            doctorService.deleteDoctor(collegeNumber);
            return ResponseEntity.ok().build();
        } catch (DoctorDoesNotExists e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
