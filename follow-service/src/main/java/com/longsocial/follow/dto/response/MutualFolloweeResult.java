package com.longsocial.follow.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MutualFolloweeResult {
    Integer userId;
    String userName;
    String avatar;
    Integer mutualNumber;
    List<MutualFriendResponse> mutual;
}
