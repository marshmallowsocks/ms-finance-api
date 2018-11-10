package com.marshmallowsocks.msfinance.testutils;

import com.marshmallowsocks.msfinance.data.accesstoken.AccessToken;
import com.marshmallowsocks.msfinance.data.institutions.Institution;
import org.apache.commons.lang3.RandomStringUtils;

public class ModelObjectFactory {

    public static AccessToken randomAccessToken() {
        return new AccessToken(
                RandomStringUtils.randomAscii(10),
                RandomStringUtils.randomAscii(10),
                RandomStringUtils.randomAlphanumeric(6)
        );
    }

    public static Institution randomInstitution() {
        return new Institution(
                RandomStringUtils.randomAscii(10),
                RandomStringUtils.randomAscii(10),
                RandomStringUtils.randomAscii(10),
                RandomStringUtils.randomAscii(10),
                RandomStringUtils.randomAscii(6)
        );
    }
}
