package com.group.post.repository;

import com.group.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    List<Post>findALlByUserId(Integer id);
    Optional<Post> findByPostIdentifier(String postIdentifier);
    @Query("{'userId': {$in:?0}}")
    Page<Post> findPostPageable(List<Integer> userId, Pageable pageable);
    @Query("{'_id': {$in:?0}}")
    Page<Post> findAllById(List<String> postIds,Pageable pageable);
    @Query("{'_id':{$in:?0}}")
    List<Post> findByListId(List<String> postIds);

}
