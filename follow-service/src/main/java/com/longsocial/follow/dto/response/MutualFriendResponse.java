package com.longsocial.follow.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class MutualFriendResponse {
    Integer userId;
    String userName;
    String avatar;
}
