package com.example.hospital.Service;

import com.example.hospital.Controller.DTO.*;
import com.example.hospital.Domain.Doctor;
import com.example.hospital.Exception.*;
import com.example.hospital.Repository.DoctorRepository;
import com.example.hospital.Repository.NurseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    NurseRepository nurseRepository;

    public List<DoctorOutput> getAllDoctors() throws IsEmptyException {
        List<Doctor> doctors = doctorRepository.findAll();
        List<DoctorOutput> doctorsOutput = new ArrayList<>();
        if(doctors.isEmpty()) throw new IsEmptyException("List of doctors is empty");

        for(Doctor doctor: doctors){
            doctorsOutput.add(DoctorOutput.getDoctor(doctor));
        }
        return doctorsOutput;
    }

    public void addDoctor(DoctorInput doctorInput) throws AlreadyExistsException, InvalidException {
        Doctor newDoctor = DoctorInput.getDoctor(doctorInput);
        if(doctorRepository.existsById(doctorInput.getCollegeNumber())) throw new AlreadyExistsException
                ("Doctor already exists");
        if(doctorRepository.existsById(doctorInput.getId())) throw new AlreadyExistsException
                ("Doctor already exists");
        if(nurseRepository.existsById(doctorInput.getCollegeNumber())) throw new AlreadyExistsException
                ("Nurse already exists");
        if(nurseRepository.existsById(doctorInput.getId())) throw new AlreadyExistsException
                ("Nurse already exists");
        else {doctorRepository.save(newDoctor);
        }
    }

    public HealthStaffOutputCNumberAndTimetable setTimeTableOfDoctor(String collegeNumber, HealthStaffUpdate healthStaffUpdate) throws StaffDoesNotExists, DoctorDoesNotExists {
        if ((!doctorRepository.existsById(collegeNumber)) && (!nurseRepository.existsById(collegeNumber)))
            throw new StaffDoesNotExists("Health staff does not exist");
        if(isDoctorOrNurse(collegeNumber) == false) throw new DoctorDoesNotExists("Doctor doesn´t exist");
        Optional<Doctor> doctor = doctorRepository.findById(collegeNumber);
        Doctor doctorSet = doctor.get();
        doctorSet.setStartingTime(healthStaffUpdate.getStartingTime());
        doctorSet.setEndingTime(healthStaffUpdate.getEndingTime());
        doctorRepository.save(doctorSet);
        return HealthStaffOutputCNumberAndTimetable.getHealthStaff(collegeNumber, healthStaffUpdate);
    }

    public boolean isDoctorOrNurse(String collegeNumber){
        if(doctorRepository.existsById(collegeNumber))
            return true;
        else return false;
    }
}
