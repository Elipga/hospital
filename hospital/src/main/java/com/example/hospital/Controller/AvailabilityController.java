package com.example.hospital.Controller;

import com.example.hospital.Controller.DTO.AvailabilityOutput;
import com.example.hospital.Controller.DTO.AvailabilityOutputHour;
import com.example.hospital.Controller.DTO.DoctorOutputnumberOfAppointments;
import com.example.hospital.Exception.DoctorDoesNotExists;
import com.example.hospital.Exception.NurseDoesNotExists;
import com.example.hospital.Exception.StaffDoesNotExists;
import com.example.hospital.Service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.TreeMap;

@RestController
public class AvailabilityController {
    @Autowired
    AvailabilityService availabilityService;

    /*@RequestMapping(value = "/availabilities", params = {"collegeNumber"})
    //@GetMapping("/doctors/{collegueNumber}/availabilities")
    public ResponseEntity <List<AvailabilityOutput>> getAvailabilityOfStaff(@RequestParam String collegeNumber){
        List<AvailabilityOutput> availabilityOutputs = null;
        try {
            availabilityOutputs = availabilityService.getAvailabilityOfStaff(collegeNumber);
            return ResponseEntity.ok(availabilityOutputs);
        } catch (StaffDoesNotExists e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }*/

    @GetMapping("/doctors/{collegeNumber}/availabilities")
    public ResponseEntity <TreeMap<DayOfWeek, List<AvailabilityOutputHour>>> getAvailabilityOfDoctor(@PathVariable String collegeNumber){
        TreeMap<DayOfWeek, List<AvailabilityOutputHour>> availabilityOutputs = null;
        try {
            if(availabilityService.isDoctorOrNurse(collegeNumber) == false) throw new DoctorDoesNotExists("Doctor" +
                    "doesn´t exist");
            availabilityOutputs = availabilityService.getAvailabilityOfStaff(collegeNumber);
            return ResponseEntity.ok(availabilityOutputs);
        } catch (StaffDoesNotExists e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DoctorDoesNotExists e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/nurses/{collegeNumber}/availabilities")
    public ResponseEntity <TreeMap<DayOfWeek, List<AvailabilityOutputHour>>> getAvailabilityOfNurse(@PathVariable String collegeNumber){
        TreeMap<DayOfWeek, List<AvailabilityOutputHour>> availabilityOutputs = null;
        try {
            if(availabilityService.isDoctorOrNurse(collegeNumber) == true) throw new NurseDoesNotExists("Nurse" +
                    "doesn´t exist");
            availabilityOutputs = availabilityService.getAvailabilityOfStaff(collegeNumber);
            return ResponseEntity.ok(availabilityOutputs);
        } catch (StaffDoesNotExists e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NurseDoesNotExists e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
