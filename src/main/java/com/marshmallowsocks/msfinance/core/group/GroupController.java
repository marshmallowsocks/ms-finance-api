package com.marshmallowsocks.msfinance.core.group;

import com.marshmallowsocks.msfinance.core.response.CreateGroupResponse;
import com.marshmallowsocks.msfinance.data.groups.Group;
import com.marshmallowsocks.msfinance.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/groups", consumes = MediaType.APPLICATION_JSON_VALUE)
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/all")
    public List<Group> getAllGroups(@AuthenticationPrincipal final User user) {
        groupService.setUserId(user.getId());
        return groupService.getGroups();
    }

    @PostMapping("/create")
    public CreateGroupResponse createGroup(
            @AuthenticationPrincipal final User user,
            @RequestBody CreateGroupRequest request) {
        groupService.setUserId(user.getId());
        return groupService.createGroup(request.getName(), request.getAccounts());
    }

    @DeleteMapping("/delete")
    public boolean deleteGroup(
            @AuthenticationPrincipal User user,
            @RequestBody DeleteGroupRequest request) {
        groupService.setUserId(user.getId());
        return groupService.deleteGroup(request.getGroupId());
    }
}
