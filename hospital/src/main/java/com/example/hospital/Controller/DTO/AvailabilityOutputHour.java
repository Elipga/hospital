package com.example.hospital.Controller.DTO;

import com.example.hospital.Domain.Availability;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class AvailabilityOutputHour {
    private LocalTime hour;

    public AvailabilityOutputHour(LocalTime hour) {
        this.hour = hour;
    }

    public static AvailabilityOutputHour getAvailability(Availability availability) {
        return new AvailabilityOutputHour(availability.getHour());
    }
}
