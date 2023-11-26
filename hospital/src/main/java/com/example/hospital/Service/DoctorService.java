package com.example.hospital.Service;

import com.example.hospital.Controller.DTO.HealthStaff.DoctorInput;
import com.example.hospital.Controller.DTO.HealthStaff.DoctorOutput;
import com.example.hospital.Controller.DTO.HealthStaff.HealthStaffOutputCNumberAndTimetable;
import com.example.hospital.Controller.DTO.HealthStaff.HealthStaffUpdate;
import com.example.hospital.Domain.Appointment;
import com.example.hospital.Domain.Doctor;
import com.example.hospital.Exception.*;
import com.example.hospital.Repository.AppointmentRepository;
import com.example.hospital.Repository.DoctorRepository;
import com.example.hospital.Repository.NurseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class DoctorService {
    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    NurseRepository nurseRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    HealthStaffService healthStaffService;

    public List<DoctorOutput> getAllDoctors() throws IsEmptyException, InvalidException {
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
                ("Already exist a nurse with that college number");
        if (nurseRepository.existsByDni(doctorInput.getDni())) throw new AlreadyExistsException
                ("Already exist a nurse with that dni");
        else {
            doctorRepository.save(newDoctor);
        }
    }
    public HealthStaffOutputCNumberAndTimetable setTimeTableOfDoctor(String collegeNumber, HealthStaffUpdate healthStaffUpdate) throws DoctorDoesNotExists, InvalidException, AlreadyExistsException {
        if(!doctorRepository.existsById(collegeNumber)) throw new DoctorDoesNotExists("Doctor doesn´t exist");
        Optional<Doctor> doctor = doctorRepository.findById(collegeNumber);
        Doctor doctorSet = doctor.get();
        if((doctorSet.getStartingTime().equals(healthStaffUpdate.getStartingTime())) && (doctorSet.getEndingTime()
                .equals(healthStaffUpdate.getEndingTime()))) throw new AlreadyExistsException("There is no changes " +
                "in timetable");
        doctorSet.setStartingTime(healthStaffUpdate.getStartingTime());
        doctorSet.setEndingTime(healthStaffUpdate.getEndingTime());
        doctorRepository.save(doctorSet);
        return HealthStaffOutputCNumberAndTimetable.getHealthStaff(collegeNumber, healthStaffUpdate);
    }
    public DoctorOutput getDoctorById(String collegeNumber) throws DoctorDoesNotExists, InvalidException {
        if (!doctorRepository.existsById(collegeNumber)) throw new DoctorDoesNotExists("Doctor doesn´t exist");
        else {
            Optional<Doctor> doctor = doctorRepository.findById(collegeNumber);
            DoctorOutput doctorOutput = DoctorOutput.getDoctor(doctor.get());
            return doctorOutput;
        }
    }
    public TreeMap<LocalDate, List<LocalTime>> getAvailabilityOfDoctor(String collegeNumber) throws DoctorDoesNotExists, StaffDoesNotExists {
        TreeMap<LocalDate, List<LocalTime>> availabilities;
        if(!doctorRepository.existsById(collegeNumber)) throw new DoctorDoesNotExists("Doctor " +
                "doesn´t exist");
        else{
            availabilities = healthStaffService.getAvailabilityOfStaff(collegeNumber);
            return availabilities;
        }
    }
    public TreeMap<Integer, String> getBusiestDoctors() throws IsEmptyException {
        List<Doctor> doctors = doctorRepository.findAll();
        if(doctors.isEmpty()) throw new IsEmptyException("List of doctors is empty");
        TreeMap<Integer, String> doctorsOutput = new TreeMap<>(Comparator.reverseOrder());
        LocalDate[] dates = healthStaffService.temporalWindowArray();
        LocalDate firstDay = dates[0];

        for(Doctor doctor: doctors){ //all doctors
            int appointmentCounter = 0; //set counter to 0
            List<Appointment> appointments = appointmentRepository.findByCollegeNumber(doctor.getCollegeNumber());
            //appointments of doctor (by college number)
            for(Appointment appointment: appointments){
                if(appointment.getDateOfAppointment().isAfter(firstDay.minusDays(1))){ //if appointment is on the week of temporal window
                    appointmentCounter = appointmentCounter + 1;} //add +1 to counter
            }
            doctorsOutput.put(appointmentCounter, doctor.getCollegeNumber());
        }
        return doctorsOutput;
    }
}
