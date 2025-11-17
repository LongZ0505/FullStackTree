package com.longsocial.follow.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserNodeRequest {
    String userId;
    String name;
    boolean sex;
    LocalDate dob;
    LocalDate dod;
    String generation;
}
