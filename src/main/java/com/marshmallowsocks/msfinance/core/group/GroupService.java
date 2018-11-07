package com.marshmallowsocks.msfinance.core.group;

import com.marshmallowsocks.msfinance.core.response.CreateGroupResponse;
import com.marshmallowsocks.msfinance.data.groups.Group;
import com.marshmallowsocks.msfinance.data.groups.GroupRepository;
import io.leangen.graphql.annotations.GraphQLEnvironment;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.execution.ResolutionEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository repository;

    private String userId;

    @GraphQLMutation(name = "createGroup")
    public CreateGroupResponse createGroup(@GraphQLEnvironment ResolutionEnvironment environment, String name, List<String> accounts) {
        userId = environment.rootContext.toString();
        Group group = repository.save(new Group(
                name,
                accounts,
                userId)
        );
        return new CreateGroupResponse(false, group.getId(), "Successfully created group.");
    }

    @GraphQLMutation(name = "deleteGroup")
    public boolean deleteGroup(String groupId) {
        try {
            repository.deleteById(groupId);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    @GraphQLQuery(name = "groups")
    public List<Group> getGroups(@GraphQLEnvironment ResolutionEnvironment environment) {
        userId = environment.rootContext.toString();
        return repository.getGroupsForUserId(userId);
    }
}
