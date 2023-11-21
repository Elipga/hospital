package com.example.hospital.Service;

import com.example.hospital.Controller.DTO.HealthStaff.DoctorOutput;
import com.example.hospital.Controller.DoctorController;
import com.example.hospital.Domain.Doctor;
import com.example.hospital.Exception.DoctorDoesNotExists;
import com.example.hospital.Exception.InvalidException;
import com.example.hospital.Exception.IsEmptyException;
import com.example.hospital.Repository.DoctorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.print.Doc;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;
    @InjectMocks
    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        doctorService = new DoctorService(doctorRepository);
        Mockito.mock(DoctorService.class);
    }
    @Test
    void GivenAnArrayOfDoctorsRetrievedByRepository_WhenCallToGetAllDoctors_ThenReturnSameArrayAsRepository() throws InvalidException, IsEmptyException {
        String startString = "15:30:00";
        String endString = "17:00:00";

        LocalTime startingTime = LocalTime.parse(startString);
        LocalTime endingTime = LocalTime.parse(endString);

        List<Doctor> doctors = new ArrayList<Doctor>();
        Doctor doctor1 = new Doctor("12345678A", "123456789", "Elisa",startingTime, endingTime, (byte)1);
        Doctor doctor2 = new Doctor("12345678B", "223456789", "Elisa", startingTime, endingTime, (byte)1);
        Doctor doctor3 = new Doctor("12345678C", "323456789", "Elisa", startingTime, endingTime, (byte)1);
        doctors.add(doctor1);
        doctors.add(doctor2);
        doctors.add(doctor3);

        when(doctorRepository.findAll()).thenReturn(doctors);
        List<DoctorOutput> doctorOutputs = doctorService.getAllDoctors();
        Assertions.assertEquals(3, doctorOutputs.size());
    }


    @Test
    void getDoctorById() throws InvalidException, DoctorDoesNotExists {
        String startString = "15:30:00";
        String endString = "17:00:00";

        LocalTime startingTime = LocalTime.parse(startString);
        LocalTime endingTime = LocalTime.parse(endString);
        Doctor doctor = new Doctor("12345678A", "123456789", "Elisa",startingTime, endingTime, (byte)1);
        DoctorOutput doctorOutput = new DoctorOutput("12345678A", "123456789", "Elisa",startingTime, endingTime);

        when(doctorRepository.existsById(doctor.getCollegeNumber())).thenReturn(true);
        when(doctorRepository.findById(doctor.getCollegeNumber())).thenReturn(Optional.of(doctor));
        DoctorOutput result = doctorService.getDoctorById(doctor.getCollegeNumber());
        Assertions.assertEquals(doctorOutput.getName(), result.getName()); // doctorOutput.toString, result.toString
        //propiedad a propiedad
    }
}