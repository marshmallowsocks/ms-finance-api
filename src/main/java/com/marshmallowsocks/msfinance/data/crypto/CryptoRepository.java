package com.marshmallowsocks.msfinance.data.crypto;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CryptoRepository extends MongoRepository<Crypto, String> {
    void deleteByCryptoId(String cryptoId);
}
