package com.marshmallowsocks.msfinance.testutils;

import com.marshmallowsocks.msfinance.data.accesstoken.AccessToken;
import com.marshmallowsocks.msfinance.data.groups.Group;
import com.marshmallowsocks.msfinance.data.institutions.Institution;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Collections;

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

    public static Group randomGroup() {
        Group group = new Group(
                RandomStringUtils.randomAscii(10),
                Collections.emptyList(),
                RandomStringUtils.randomAscii(6)
        );

        group.setId("ID - " + RandomStringUtils.randomAscii(6));
        return group;
    }
}
