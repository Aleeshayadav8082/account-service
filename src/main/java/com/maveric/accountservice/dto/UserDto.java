package com.maveric.accountservice.dto;

import com.maveric.accountservice.enums.Gender;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Builder
@Data
public class UserDto {
    private String id;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String address;

    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;
}