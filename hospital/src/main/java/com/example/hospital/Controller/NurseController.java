package com.example.hospital.Controller;

import com.example.hospital.Controller.DTO.*;
import com.example.hospital.Exception.*;
import com.example.hospital.Service.NurseService;
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

    @GetMapping("/nurses")
    public ResponseEntity<List<NurseOutput>> getAllNurses(){
        try {
            List<NurseOutput> nurses = nurseService.getAllNurses();
            return ResponseEntity.ok(nurses);
        } catch (IsEmptyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/nurses")
    public ResponseEntity<String> addNurse(@Valid @RequestBody NurseInput nurseInput){
        try {
            nurseService.addNurse(nurseInput);
            return ResponseEntity.ok().build();
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
        } catch (InvalidException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
    }

    @PutMapping("/nurses/{collegeNumber}")
    public ResponseEntity<HealthStaffOutputCNumberAndTimetable> setTimetableOfNurse(@PathVariable String collegeNumber,
                                                                                @RequestBody HealthStaffUpdate healthStaffUpdate) {
        try {
            HealthStaffOutputCNumberAndTimetable nurse = nurseService.setTimeTableOfNurse(collegeNumber, healthStaffUpdate);
            return ResponseEntity.ok(nurse);
        } catch (StaffDoesNotExists e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NurseDoesNotExists e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
