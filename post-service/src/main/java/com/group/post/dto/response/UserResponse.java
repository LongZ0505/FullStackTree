package com.group.post.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    Integer userId;
    String userName;
    String email;
    String password;
    String avatar;
    String fullName;
    String lastLogin;
    String website;
    String bio;
    String phoneNumber;
}
