package com.longsocial.follow.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Node("IndividualsNode")
public class UserNode {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;
    @Property("userId")
    String userId;
    String name;
    boolean sex;
    LocalDate dob;
    LocalDate dod;
    String generation;
    @Relationship(type = "SOUSE", direction = Relationship.Direction.OUTGOING)
    Set<UserNode> spouseRelationship;
    @Relationship(type = "FATHER_SON", direction = Relationship.Direction.OUTGOING)
    Set<UserNode> childrenAsFather;
    @Relationship(type = "MOTHER_SON", direction = Relationship.Direction.OUTGOING)
    Set<UserNode> childrenAsMother;
}
