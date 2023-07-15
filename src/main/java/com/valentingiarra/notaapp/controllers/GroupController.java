package com.valentingiarra.notaapp.controllers;

import com.valentingiarra.notaapp.service.GroupService;
import com.valentingiarra.notaapp.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("groups")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    // ---- GET

    // ---- POST

    // ---- PUT

    // ---- DELETE
}
