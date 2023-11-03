package com.example.hospital.Service;

import com.example.hospital.Controller.DTO.DoctorInput;
import com.example.hospital.Controller.DTO.DoctorOutput;
import com.example.hospital.Domain.Doctor;
import com.example.hospital.Domain.HealthStaff;
import com.example.hospital.Exception.AlreadyExistsException;
import com.example.hospital.Exception.InvalidException;
import com.example.hospital.Exception.IsEmptyException;
import com.example.hospital.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorService {
    @Autowired
    DoctorRepository doctorRepository;

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
        else {doctorRepository.save(newDoctor);
        }
    }



    //getTimetableOfDoctor
    //addtimetableToDoctor
    //addCitaDoctor
    //getCitasDoctor
    //
}
