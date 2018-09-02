package com.marshmallowsocks.msfinance.core.group;

import com.marshmallowsocks.msfinance.core.response.CreateGroupResponse;
import com.marshmallowsocks.msfinance.data.groups.Group;
import com.marshmallowsocks.msfinance.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/all")
    public List<Group> getAllGroups(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        groupService.setUserId(user.getId());
        return groupService.getGroups();
    }

    @PostMapping("/create")
    public CreateGroupResponse createGroup(
            Authentication authentication,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "accounts") List<String> accounts) {
        User user = (User) authentication.getPrincipal();
        groupService.setUserId(user.getId());
        return groupService.createGroup(name, accounts);
    }

    @DeleteMapping("/delete")
    public boolean deleteGroup(
            Authentication authentication,
            @RequestParam(name = "groupId") String groupId) {
        User user = (User) authentication.getPrincipal();
        groupService.setUserId(user.getId());
        return groupService.deleteGroup(groupId);
    }
}
