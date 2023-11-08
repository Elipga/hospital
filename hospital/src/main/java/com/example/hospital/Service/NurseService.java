package com.example.hospital.Service;

import com.example.hospital.Controller.DTO.*;
import com.example.hospital.Domain.Doctor;
import com.example.hospital.Domain.Nurse;
import com.example.hospital.Exception.*;
import com.example.hospital.Repository.DoctorRepository;
import com.example.hospital.Repository.NurseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NurseService {

    @Autowired
    NurseRepository nurseRepository;

    @Autowired
    DoctorRepository doctorRepository;

    public List<NurseOutput> getAllNurses() throws IsEmptyException {
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
        if(nurseRepository.existsById(nurseInput.getCollegeNumber())) throw new AlreadyExistsException
                ("Nurse already exists");
        else {nurseRepository.save(newNurse);
        }
    }

    public NurseOutputCNumberAndTimetable setTimeTableOfNurse(String collegeNumber, HealthStaffUpdate healthStaffUpdate) throws StaffDoesNotExists, NurseDoesNotExists {
        if ((!doctorRepository.existsById(collegeNumber)) && (!nurseRepository.existsById(collegeNumber)))
            throw new StaffDoesNotExists("Health staff does not exist");
        if(isDoctorOrNurse(collegeNumber) == true) throw new NurseDoesNotExists("Nurse doesn´t exist");
        Optional<Nurse> nurse = nurseRepository.findById(collegeNumber);
        Nurse nurseSet = nurse.get();
        nurseSet.setStartingTime(healthStaffUpdate.getStartingTime());
        nurseSet.setEndingTime(healthStaffUpdate.getEndingTime());
        nurseRepository.save(nurseSet);
        return NurseOutputCNumberAndTimetable.getNurse(collegeNumber, healthStaffUpdate);
    }

    public boolean isDoctorOrNurse(String collegeNumber){
        if(doctorRepository.existsById(collegeNumber))
            return true;
        else return false;
    }

}
