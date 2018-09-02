package com.marshmallowsocks.msfinance.data.institutions;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InstitutionRepository extends MongoRepository<Institution, String> {

    @Query("{'itemToken': ?0}")
    Optional<Institution> getInstitutionByItemToken(String itemToken);

    @Query("{'userId': ?0}")
    List<Institution> getInstitutionsForUserId(String userId);
}
