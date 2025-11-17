package com.longsocial.follow.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserNodeResponse {
   String id;
   String name;
   boolean sex;
   LocalDate dob;
   LocalDate dod;
   String generation;
}
