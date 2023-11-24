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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.TreeMap;

@RestController
public class NurseController {
    @Autowired
    NurseService nurseService;
    @Operation(summary = "Add nurse")
    @PostMapping("/nurses")
    public ResponseEntity<String> addNurse(@Valid @RequestBody NurseInput nurseInput) throws AlreadyExistsException, InvalidException {
            nurseService.addNurse(nurseInput);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Operation(summary = "Get nurses")
    @GetMapping("/nurses")
    public ResponseEntity<List<NurseOutput>> getAllNurses() throws IsEmptyException, InvalidException {
            List<NurseOutput> nurses = nurseService.getAllNurses();
            return ResponseEntity.ok(nurses);
    }
    @Operation(summary = "Set timetable of nurse", description = "Update Starting and Ending time")
    @PutMapping("/nurses/{collegeNumber}")
    public ResponseEntity<HealthStaffOutputCNumberAndTimetable> setTimetableOfNurse(@PathVariable String collegeNumber,
                                                                                    @RequestBody HealthStaffUpdate healthStaffUpdate) throws InvalidException, NurseDoesNotExists, AlreadyExistsException {
            HealthStaffOutputCNumberAndTimetable nurse = nurseService.setTimeTableOfNurse(collegeNumber, healthStaffUpdate);
            return ResponseEntity.ok(nurse);
    }

    @Operation(summary = "Get availability of nurse for next temporal window")
    @GetMapping("/nurses/{collegeNumber}/availabilities")
    public ResponseEntity <TreeMap<LocalDate, List<LocalTime>>> getAvailabilityOfNurse(@PathVariable String collegeNumber) throws NurseDoesNotExists, StaffDoesNotExists {
        TreeMap<LocalDate, List<LocalTime>> availabilityOutputs = null;
        availabilityOutputs = nurseService.getAvailabilityOfNurse(collegeNumber);
        return ResponseEntity.ok(availabilityOutputs);
    }
}
