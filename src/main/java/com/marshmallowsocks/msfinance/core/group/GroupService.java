package com.marshmallowsocks.msfinance.core.group;

import com.marshmallowsocks.msfinance.core.ServiceBase;
import com.marshmallowsocks.msfinance.core.response.CreateGroupResponse;
import com.marshmallowsocks.msfinance.data.groups.Group;
import com.marshmallowsocks.msfinance.data.groups.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService implements ServiceBase {

    @Autowired
    private GroupRepository repository;

    private String userId;

    public CreateGroupResponse createGroup(String name, List<String> accounts) {
        Group group = repository.save(new Group(
                name,
                accounts,
                getUserId())
        );

        return new CreateGroupResponse(false, group.id, "Successfully created group.");
    }

    public boolean deleteGroup(String groupId) {
        try {
            repository.deleteById(groupId);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    public List<Group> getGroups() {
        return repository.findAll();
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
