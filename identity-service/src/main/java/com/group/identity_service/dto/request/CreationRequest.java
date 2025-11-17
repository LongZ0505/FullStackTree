package com.group.identity_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class CreationRequest {
    String userName;
    String password;
    String name;
    String email;
    LocalDate dob;
    boolean sex;
}
