package com.marshmallowsocks.msfinance.core.group;

import com.marshmallowsocks.msfinance.core.ServiceBaseTest;
import com.marshmallowsocks.msfinance.core.response.CreateGroupResponse;
import com.marshmallowsocks.msfinance.data.groups.Group;
import com.marshmallowsocks.msfinance.data.groups.GroupRepository;
import com.marshmallowsocks.msfinance.testutils.ModelObjectFactory;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class GroupServiceTest extends ServiceBaseTest {

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupService groupService;

    @Before
    public void setup() {
        super.init();
        List<Group> groups = new ArrayList<>();

        for(int i = 0; i < 5; i++) {
            groups.add(ModelObjectFactory.randomGroup());
        }

        when(groupRepository.save(any(Group.class)))
                .thenReturn(ModelObjectFactory.randomGroup());

        doNothing().when(groupRepository).deleteById(anyString());

        when(groupRepository.getGroupsForUserId(eq("1234")))
                .thenReturn(groups);
    }

    @Test
    public void saveNewGroup_valid() {

        // arrange
        Group expectedGroup = ModelObjectFactory.randomGroup();

        // act
        CreateGroupResponse groupResponse = groupService.createGroup(
                environment,
                expectedGroup.getName(),
                Collections.emptyList()
        );


        // assert
        verify(groupRepository, times(1))
                .save(any(Group.class));
        Assert.assertFalse(groupResponse.isError());
        Assert.assertTrue(groupResponse.getId().startsWith("ID - "));
        Assert.assertEquals(groupResponse.getMessage(), "Successfully created group.");
    }

    @Test
    public void deleteGroup_valid() {

        // act & assert
        Assert.assertTrue(groupService.deleteGroup("1234"));
    }

    @Test
    public void deleteGroup_invalid() {

        // arrange
        doThrow(new IllegalArgumentException())
                .when(groupRepository).deleteById(anyString());

        // act & assert
        Assert.assertFalse(groupService.deleteGroup("1234"));
    }

    @Test
    public void getGroups_valid() {

        // act
        List<Group> groups = groupService.getGroups(environment);

        // assert
        Assert.assertEquals(groups.size(), 5);
    }
}
