package com.example.hospital.Repository;

import com.example.hospital.Domain.Availability;
import com.example.hospital.Domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {
}
