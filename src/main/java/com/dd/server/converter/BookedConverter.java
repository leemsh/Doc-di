package com.dd.server.converter;

import com.dd.server.domain.Booked;
import com.dd.server.dto.BookedDto;

public class BookedConverter {
    public static Booked toEntity(BookedDto bookedDto){
        Booked booked = new Booked();
        booked.setEmail(bookedDto.getEmail());
        booked.setHospitalName(bookedDto.getHospitalName());
        booked.setDoctorName(bookedDto.getDoctorName());
        booked.setSubject(bookedDto.getSubject());
        booked.setBookTime(bookedDto.getBookTime());
        return booked;
    }
}
