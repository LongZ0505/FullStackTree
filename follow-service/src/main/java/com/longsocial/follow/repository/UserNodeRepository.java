package com.longsocial.follow.repository;

import com.longsocial.follow.dto.request.UserNodeRequest;
import com.longsocial.follow.dto.response.RelationProjection;
import com.longsocial.follow.dto.response.RelationResponse;
import com.longsocial.follow.dto.response.UserNodeResponse;
import com.longsocial.follow.entity.FollowRelation;
import com.longsocial.follow.entity.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserNodeRepository extends Neo4jRepository<UserNode, String> {
    @Query("MATCH (a:IndividualsNode {userId:$userId}) return a")
    UserNode findByUserId(String userId);

    @Query("MATCH (a:IndividualsNode {id: $id}) " +
            "SET a.name = $name, " +
            "    a.dob = $dob, " +
            "    a.dod = $dod, " +
            "    a.sex = $sex")
    void update(@Param("id") String id,
                @Param("name") String name,
                @Param("dob") LocalDate dob,
                @Param("dod") LocalDate dod,
                @Param("sex") boolean sex);

   @Query("MATCH (a:IndividualsNode {id: $id}) " +
            "SET a.generation = $generation ")
    void updateGenetation(String id, String generation);

    @Query("""
                MATCH (a:IndividualsNode {id: $fromNodeId})
                MATCH (b:IndividualsNode {id: $toNodeId})
                MERGE (a)-[r:SOUSE]->(b)
                RETURN {id: toString(id(r)), fromNodeId: a.id, toNodeId: b.id, type: type(r)} AS result
            """)
    RelationProjection createSouseRelationship(String fromNodeId, String toNodeId);

    @Query("""
                MATCH (a:IndividualsNode {id: $fromNodeId})
                MATCH (b:IndividualsNode {id: $toNodeId})
                MERGE (a)-[r:BROTHER_SISTER]->(b)
            
                RETURN {id: toString(id(r)), fromNodeId: a.id, toNodeId: b.id, type: type(r)} AS result
            """)
    RelationProjection createBrotherSisterRelationship(String fromNodeId, String toNodeId);

    @Query("""
                MATCH (a:IndividualsNode {id: $fromNodeId})
                MATCH (b:IndividualsNode {id: $toNodeId})
                MERGE (a)-[r:MOTHER_SON]->(b)
                RETURN {id: toString(id(r)), fromNodeId: a.id, toNodeId: b.id, type: type(r)} AS result
            """)
    RelationProjection createMotherSonRelationship(String fromNodeId, String toNodeId);

    @Query("""
                MATCH (a:IndividualsNode {id: $fromNodeId})
                MATCH (b:IndividualsNode {id: $toNodeId})
                MERGE (a)-[r:FATHER_SON]->(b)
                RETURN {id: toString(id(r)), fromNodeId: a.id, toNodeId: b.id, type: type(r)} AS result
            """)
    RelationProjection createFatherSonRelationship(String fromNodeId, String toNodeId);

    @Query("""
            MATCH (n:IndividualsNode)-[r]->(m:IndividualsNode)
            RETURN toString(id(r)) AS id,
                   n.id AS fromNodeId,
                   m.id AS toNodeId,
                   type(r) AS type
            """)
    List<RelationResponse> findAllRelationships();

    @Query("MATCH (n:IndividualsNode) RETURN n")
    List<UserNode> findAllNodes();

    @Query("""
                MATCH ()-[r]->()
                WHERE id(r) = $id
                DELETE r
            """)
    void deleteRelation(long id);
    @Query("""
            MATCH (u:IndividualsNode)
            RETURN u
            ORDER BY u.generation asc;
            """)
    List<UserNode> findAllByGenerationAsc();

    @Query("MATCH (u:IndividualsNode) WHERE toLower(u.name) CONTAINS toLower($query) RETURN u")
    List<UserNode> selectAllByQuery(@Param("query") String query);
}

