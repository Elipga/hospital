package com.example.hospital.Service;

import com.example.hospital.Controller.DTO.HealthStaff.*;
import com.example.hospital.Domain.Doctor;
import com.example.hospital.Domain.Nurse;
import com.example.hospital.Exception.*;
import com.example.hospital.Repository.DoctorRepository;
import com.example.hospital.Repository.NurseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

@Service
public class NurseService {

    @Autowired
    NurseRepository nurseRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    HealthStaffService healthStaffService;

    public List<NurseOutput> getAllNurses() throws IsEmptyException, InvalidException {
        List<Nurse> nurses = nurseRepository.findAll();
        List<NurseOutput> nursesOutput = new ArrayList<>();
        if(nurses.isEmpty()) throw new IsEmptyException("List of nurses is empty");

        for(Nurse nurse: nurses){
            nursesOutput.add(NurseOutput.getNurse(nurse));
        }
        return nursesOutput;
    }

    public void addNurse(NurseInput nurseInput) throws AlreadyExistsException, InvalidException {
        Nurse newNurse = NurseInput.getNurse(nurseInput);
        if(doctorRepository.existsById(nurseInput.getCollegeNumber())) throw new AlreadyExistsException
                ("Already exist a doctor with that college number");
        if(doctorRepository.existsByDni(nurseInput.getDni())) throw new AlreadyExistsException
                ("Already exist a doctor with that dni");
        if(nurseRepository.existsById(nurseInput.getCollegeNumber())) throw new AlreadyExistsException
                ("Nurse already exists");
        if(nurseRepository.existsByDni(nurseInput.getDni())) throw new AlreadyExistsException
                ("Nurse already exists");
        else {nurseRepository.save(newNurse);
        }
    }

    public NurseOutput getNurseById(String collegeNumber) throws InvalidException, NurseDoesNotExists {
        if (!nurseRepository.existsById(collegeNumber)) throw new NurseDoesNotExists("Nurse doesn´t exist");
        else {
            Optional<Nurse> nurse = nurseRepository.findById(collegeNumber);
            NurseOutput nurseOutput = NurseOutput.getNurse(nurse.get());
            return nurseOutput;
        }
    }

    public HealthStaffOutputCNumberAndTimetable setTimeTableOfNurse(String collegeNumber, HealthStaffUpdate healthStaffUpdate) throws NurseDoesNotExists, InvalidException, AlreadyExistsException {
        if(!nurseRepository.existsById(collegeNumber)) throw new NurseDoesNotExists("Nurse doesn´t exist");
        Optional<Nurse> nurse = nurseRepository.findById(collegeNumber);
        Nurse nurseSet = nurse.get();
        if((nurseSet.getStartingTime().equals(healthStaffUpdate.getStartingTime())) && (nurseSet.getEndingTime()
                .equals(healthStaffUpdate.getEndingTime()))) throw new AlreadyExistsException("There is no changes " +
                "in timetable");
        nurseSet.setStartingTime(healthStaffUpdate.getStartingTime());
        nurseSet.setEndingTime(healthStaffUpdate.getEndingTime());
        nurseRepository.save(nurseSet);
        return HealthStaffOutputCNumberAndTimetable.getHealthStaff(collegeNumber, healthStaffUpdate);
    }

    public TreeMap<LocalDate, List<LocalTime>> getAvailabilityOfNurse(String collegeNumber) throws StaffDoesNotExists, NurseDoesNotExists {
        TreeMap<LocalDate, List<LocalTime>> availabilities;
        if(!nurseRepository.existsById(collegeNumber)) throw new NurseDoesNotExists("Nurse " +
                "doesn´t exist");
        else{
            availabilities = healthStaffService.getAvailabilityOfStaff(collegeNumber);
            return availabilities;
        }
    }
}
