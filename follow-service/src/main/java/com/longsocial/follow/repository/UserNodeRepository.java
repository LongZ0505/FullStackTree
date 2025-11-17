package com.longsocial.follow.repository;

import com.longsocial.follow.dto.request.UserNodeRequest;
import com.longsocial.follow.dto.response.RelationProjection;
import com.longsocial.follow.dto.response.RelationResponse;
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
    @Query("MATCH (a:UserNode {userId:$userId}) return a")
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

    @Query("MATCH (a:UserNode {userId:$userId})-[:FOLLOWS]->(b:UserNode)" +
            "RETURN b")
    List<UserNode> getFollowees(@Param("userId") Integer userId);

    @Query("MATCH (a:UserNode {userId:$userId})-[r:FOLLOWS]->(b:UserNode)" +
            "RETURN r")
    Set<FollowRelation> getFolloweesRelation(@Param("userId") Integer userId);

    @Query("MATCH (a:UserNode)-[:FOLLOWS]->(b:UserNode {userId:$userId})" +
            "RETURN a")
    List<UserNode> getFollowers(@Param("userId") Integer userId);

    @Query("MATCH (a:UserNode{userId:$userId1})-[:FOLLOWS]->(c:UserNode)" +
            "<-[:FOLLOWS]-(b:UserNode {userId:$userId2}) RETURN c")
    List<UserNode> getMutualFriend(@Param("userId1") Integer userId1, @Param("userId2") Integer userId2);

    @Query("MATCH (a:UserNode {userId: $followerId}), (b:UserNode {userId: $followeeId}) " +
            "MERGE (a)-[r:FOLLOWS]->(b) " +
            "SET r.followTimestamp = $timestamp")
    Optional<UserNode> createRelation(String followerId, String followeeId, Instant timestamp);

    @Query("MATCH(a:UserNode {userId:$followerId})-[r:FOLLOWS]->(b:UserNode {userId:$followeeId})" +
            "DELETE r")
    void deleteRelationship(String followerId, String followeeId);

    @Query("MATCH (a:UserNode {userId:$followerId})-[r:FOLLOWS]->(b:UserNode {userId:$followeeId})" +
            "RETURN COUNT(*)>0")
    Boolean checkFollowing(@Param("followerId") String followerId, @Param("followeeId") String followeeId);

    @Query("MATCH (a:UserNode)-[r:FOLLOWS]->(b:UserNode {userId: $userId}) return a, r.followTimestamp as followTimestamp" +
            " order by" +
            " r.followTimestamp desc limit $limit")
    List<RelationResponse> getRecentFollowed(Integer userId, int limit);

//    @Query("MATCH (a:IndividualsNode {id: $fromNodeId}) " +
//            "MATCH (b:IndividualsNode {id: $toNodeId}) " +
//            "MERGE (a)-[r:SOUSE]->(b) " +
//            "RETURN a.id AS fromNodeId,  " +
//            "       b.id AS toNodeId,  " +
//            "       toString(id(r)) AS id,  " +
//            "       type(r) AS type")
//    RelationResponse createSouseRelationship(String fromNodeId, String toNodeId);
//    @Query("MATCH (a:IndividualsNode {id: $fromNodeId}) " +
//            "MATCH (b:IndividualsNode {id: $toNodeId}) " +
//            "MERGE (a)-[r:BROTHER_SISTER]->(b) " +
//            "RETURN a.id AS fromNodeId,  " +
//            "       b.id AS toNodeId,  " +
//            "       toString(id(r)) AS id,  " +
//            "       type(r) AS type")
//    RelationResponse createBrotherSisterRelationship(String fromNodeId, String toNodeId);
//    @Query("MATCH (a:IndividualsNode {id: $fromNodeId}) " +
//            "MATCH (b:IndividualsNode {id: $toNodeId}) " +
//            "MERGE (a)-[r:MOTHER_SON]->(b) " +
//            "RETURN a.id AS fromNodeId,  " +
//            "       b.id AS toNodeId,  " +
//            "       toString(id(r)) AS id,  " +
//            "       type(r) AS type")
//    RelationResponse createMotherSonRelationship(String fromNodeId, String toNodeId);
//    @Query("MATCH (a:IndividualsNode {id: $fromNodeId}) " +
//            "MATCH (b:IndividualsNode {id: $toNodeId}) " +
//            "MERGE (a)-[r:FATHER_SON]->(b) " +
//            "RETURN a.id AS fromNodeId,  " +
//            "       b.id AS toNodeId,  " +
//            "       toString(id(r)) AS id,  " +
//            "       type(r) AS type")
//    RelationResponse createFatherSonRelationship(String fromNodeId, String toNodeId);

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
    void  deleteRelation(long id);
}

