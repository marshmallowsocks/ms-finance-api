package com.marshmallowsocks.msfinance.core.group;

import com.marshmallowsocks.msfinance.core.response.CreateGroupResponse;
import com.marshmallowsocks.msfinance.data.groups.Group;
import com.marshmallowsocks.msfinance.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/groups", consumes = "application/json")
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
            @RequestParam(name = "name") String name,
            @RequestParam(name = "accounts") List<String> accounts) {
        groupService.setUserId(user.getId());
        return groupService.createGroup(name, accounts);
    }

    @DeleteMapping("/delete")
    public boolean deleteGroup(
            @AuthenticationPrincipal User user,
            @RequestParam(name = "groupId") String groupId) {
        groupService.setUserId(user.getId());
        return groupService.deleteGroup(groupId);
    }
}
