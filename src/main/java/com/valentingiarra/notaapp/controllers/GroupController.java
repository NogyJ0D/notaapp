package com.valentingiarra.notaapp.controllers;

import com.valentingiarra.notaapp.model.ApiResponse;
import com.valentingiarra.notaapp.persistence.entities.Group;
import com.valentingiarra.notaapp.persistence.entities.User;
import com.valentingiarra.notaapp.service.GroupService;
import com.valentingiarra.notaapp.service.LogService;
import com.valentingiarra.notaapp.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = {"*"})
@RequestMapping("groups")
public class GroupController {
    private final GroupService groupService;
    private final LogService logService;

    @Autowired
    public GroupController(GroupService groupService, LogService logService) {
        this.groupService = groupService;
        this.logService = logService;
    }

    // ---- GET
    @GetMapping("user_id/{userId}")
    public ResponseEntity<ApiResponse> getAllGroupsByUserId(@PathVariable("userId") Long userId) {
        logService.WriteToLog("/groups/user_id/" + userId.toString(), "GET", null);

        List<Group> groups = groupService.getGroupsByUserId(userId);

        if (groups.size() == 0) {
            return new ResponseEntity<>(new ApiResponse("empty", groups), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse("success", groups), HttpStatus.OK);
    }

    // ---- POST
    @PostMapping("")
    public ResponseEntity<ApiResponse> createGroup(@RequestBody Group data) {
        logService.WriteToLog("/groups", "POST", data.toString());

        Object group = groupService.createGroup(data);

        if (group.getClass() != Group.class) {
            return new ResponseEntity<>(new ApiResponse("error", group), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse("success", group), HttpStatus.CREATED);
    }

    // ---- PUT
    @PutMapping("/{groupId}")
    public ResponseEntity<ApiResponse> updateGroup(@PathVariable Long groupId, @RequestBody Group data) {
        logService.WriteToLog("/groups/" + groupId.toString(), "PUT", data.toString());

        data.setId(groupId);
        Object group = groupService.updateGroup(data);
        if (group.getClass() != Group.class) {
            return new ResponseEntity<>(new ApiResponse("error", group), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ApiResponse("success", group), HttpStatus.OK);
    }

    // ---- DELETE
    @DeleteMapping("{groupId}")
    public ResponseEntity<ApiResponse> deleteGroupById(@PathVariable Long groupId) {
        logService.WriteToLog("/groups/" + groupId.toString(), "DELETE", null);

        Object res = groupService.deleteGroupById(groupId);

        if (res != "") {
            return new ResponseEntity<>(new ApiResponse("error", res), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse("success", res), HttpStatus.OK);
    }
}
