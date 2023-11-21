package com.example.hospital.Service;

import com.example.hospital.Controller.DTO.HealthStaff.DoctorInput;
import com.example.hospital.Controller.DTO.HealthStaff.DoctorOutput;
import com.example.hospital.Controller.DTO.HealthStaff.HealthStaffOutputCNumberAndTimetable;
import com.example.hospital.Controller.DTO.HealthStaff.HealthStaffUpdate;
import com.example.hospital.Domain.Doctor;
import com.example.hospital.Exception.*;
import com.example.hospital.Repository.DoctorRepository;
import com.example.hospital.Repository.NurseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    NurseRepository nurseRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<DoctorOutput> getAllDoctors() throws IsEmptyException {
        List<Doctor> doctors = doctorRepository.findAll();
        List<DoctorOutput> doctorsOutput = new ArrayList<>();
        if (doctors.isEmpty()) throw new IsEmptyException("List of doctors is empty");

        for (Doctor doctor : doctors) {
            doctorsOutput.add(DoctorOutput.getDoctor(doctor));
        }
        return doctorsOutput;
    }

    public void addDoctor(DoctorInput doctorInput) throws AlreadyExistsException, InvalidException {
        Doctor newDoctor = DoctorInput.getDoctor(doctorInput);
        if (doctorRepository.existsById(doctorInput.getCollegeNumber())) throw new AlreadyExistsException
                ("Doctor already exists");
        if (doctorRepository.existsByDni(doctorInput.getDni())) throw new AlreadyExistsException
                ("Doctor already exists");
        if (nurseRepository.existsById(doctorInput.getCollegeNumber())) throw new AlreadyExistsException
                ("Nurse already exists");
        if (nurseRepository.existsByDni(doctorInput.getDni())) throw new AlreadyExistsException
                ("Nurse already exists");
        else {
            doctorRepository.save(newDoctor);
        }
    }

    public HealthStaffOutputCNumberAndTimetable setTimeTableOfDoctor(String collegeNumber, HealthStaffUpdate healthStaffUpdate) throws StaffDoesNotExists, DoctorDoesNotExists, InvalidException {
        if ((!doctorRepository.existsById(collegeNumber)) && (!nurseRepository.existsById(collegeNumber)))
            throw new StaffDoesNotExists("Health staff does not exist");
        if (isDoctorOrNurse(collegeNumber) == false) throw new DoctorDoesNotExists("Doctor doesn´t exist");
        Optional<Doctor> doctor = doctorRepository.findById(collegeNumber);
        Doctor doctorSet = doctor.get();
        doctorSet.setStartingTime(healthStaffUpdate.getStartingTime());
        doctorSet.setEndingTime(healthStaffUpdate.getEndingTime());
        doctorRepository.save(doctorSet);
        return HealthStaffOutputCNumberAndTimetable.getHealthStaff(collegeNumber, healthStaffUpdate);
    }

    public DoctorOutput getDoctorById(String collegeNumber) throws DoctorDoesNotExists {
        if (!doctorRepository.existsById(collegeNumber)) throw new DoctorDoesNotExists("Doctor doesn´t exist");
        else {
            Optional<Doctor> doctor = doctorRepository.findById(collegeNumber);
            DoctorOutput doctorOutput = DoctorOutput.getDoctor(doctor.get());
            return doctorOutput;
        }
    }

    public void deleteDoctor(String collegeNumber) throws DoctorDoesNotExists {
        if (!doctorRepository.existsById(collegeNumber)) throw new DoctorDoesNotExists("Doctor doesn´t exist");
        else {
            Optional<Doctor> doctor = doctorRepository.findById(collegeNumber);
            doctorRepository.delete(doctor.get());
        }
    }


    public boolean isDoctorOrNurse(String collegeNumber) {
        if (doctorRepository.existsById(collegeNumber))
            return true;
        else return false;
    }


}
