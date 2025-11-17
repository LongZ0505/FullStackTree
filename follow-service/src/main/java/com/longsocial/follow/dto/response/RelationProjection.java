package com.longsocial.follow.dto.response;

import com.longsocial.follow.entity.UserNode;
import org.springframework.beans.factory.annotation.Value;

public interface RelationProjection {
  //  @Value("#{target.r.id}")   // relationship id
    String getId();

   // @Value("#{target.from.id}") // node id
    String getFromNodeId();

   // @Value("#{target.to.id}")   // node id, nullable
    String getToNodeId();

   // @Value("#{target.r.type}")  // relationship type
    String getType();
}
