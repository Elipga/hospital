package com.example.hospital.Controller;

import com.example.hospital.Controller.DTO.HealthStaff.HealthStaffOutputCNumberAndTimetable;
import com.example.hospital.Controller.DTO.HealthStaff.HealthStaffUpdate;
import com.example.hospital.Controller.DTO.HealthStaff.NurseInput;
import com.example.hospital.Controller.DTO.HealthStaff.NurseOutput;
import com.example.hospital.Exception.*;
import com.example.hospital.Service.NurseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class NurseController {
    @Autowired
    NurseService nurseService;
    @Operation(summary = "Add nurse", responses = {
            @ApiResponse(responseCode = "201", description = "Nurse created"),
            @ApiResponse(responseCode = "208", description = "Nurse already exists"),
            @ApiResponse(responseCode = "412", description = "Precondition failed")}
    )
    @PostMapping("/nurses")
    public ResponseEntity<String> addNurse(@Valid @RequestBody NurseInput nurseInput){
        try {
            nurseService.addNurse(nurseInput);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(e.getMessage());
        } catch (InvalidException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        }
    }
    @Operation(summary = "Get nurses", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Content not found") }
    )
    @GetMapping("/nurses")
    public ResponseEntity<List<NurseOutput>> getAllNurses(){
        try {
            List<NurseOutput> nurses = nurseService.getAllNurses();
            return ResponseEntity.ok(nurses);
        } catch (IsEmptyException e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @Operation(summary = "Set timetable of nurse", description = "Update Starting and Ending time", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Health Staff not found") }
    )
    @PutMapping("/nurses/{collegeNumber}")
    public ResponseEntity<HealthStaffOutputCNumberAndTimetable> setTimetableOfNurse(@PathVariable String collegeNumber,
                                                                                    @RequestBody HealthStaffUpdate healthStaffUpdate) {
        try {
            HealthStaffOutputCNumberAndTimetable nurse = nurseService.setTimeTableOfNurse(collegeNumber, healthStaffUpdate);
            return ResponseEntity.ok(nurse);
        } catch (StaffDoesNotExists e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NurseDoesNotExists e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidException e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
    }
}
