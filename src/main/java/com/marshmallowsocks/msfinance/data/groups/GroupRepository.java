package com.marshmallowsocks.msfinance.data.groups;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface GroupRepository extends MongoRepository<Group, String> {

    @Query("{'userId': ?0}")
    List<Group> getGroupsForUserId(String userId);
}
