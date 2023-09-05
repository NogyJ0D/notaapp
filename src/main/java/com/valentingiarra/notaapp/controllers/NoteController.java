package com.valentingiarra.notaapp.controllers;

import com.valentingiarra.notaapp.model.ApiResponse;
import com.valentingiarra.notaapp.persistence.entities.Group;
import com.valentingiarra.notaapp.persistence.entities.Note;
import com.valentingiarra.notaapp.service.LogService;
import com.valentingiarra.notaapp.service.NoteService;
import com.valentingiarra.notaapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = {"*"})
@RequestMapping("notes")
public class NoteController {
    private final NoteService noteService;
    private final LogService logService;

    @Autowired
    public NoteController(NoteService noteService, LogService logService) {
        this.noteService = noteService;
        this.logService = logService;
    }

    // ---- GET
    @GetMapping("user_id/{userId}")
    public ResponseEntity<ApiResponse> allNotesByUserId(@PathVariable("userId") Long userId) {
        logService.WriteToLog("/notes/user_id/" + userId.toString(), "GET", null);

        Object notes = noteService.allNotesByUserId(userId);
        if (notes.getClass() == String.class) {
            return new ResponseEntity<>(new ApiResponse("error", notes), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse("success", notes), HttpStatus.OK);
    }

    @GetMapping("user_id/{userId}/group_id/{groupId}")
    public ResponseEntity<ApiResponse> allNotesByUserIdAndGroupId(@PathVariable("userId") Long userId, @PathVariable("groupId") Long groupId) {
        logService.WriteToLog("/notes/user_id/" + userId.toString() + "/group_id/" + groupId.toString(), "GET", null);

        Object notes = noteService.allNotesByUserIdAndGroupId(userId, groupId);
        if (notes.getClass() == String.class) {
            return new ResponseEntity<>(new ApiResponse("error", notes), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse("success", notes), HttpStatus.OK);
    }

    // ---- POST
    @PostMapping("")
    public ResponseEntity<ApiResponse> createNote(@RequestBody Note data) {
        logService.WriteToLog("/notes", "POST", data.toString());

        Object note = noteService.createNote(data);

        if (note.getClass() != Note.class) {
            return new ResponseEntity<>(new ApiResponse("error", note), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse("success", note), HttpStatus.CREATED);
    }

    // ---- PUT
    @PutMapping("/{noteId}")
    public ResponseEntity<ApiResponse> updateNote(@PathVariable Long noteId, @RequestBody Note data) {
        System.out.println("update note");
        logService.WriteToLog("/notes/" + noteId.toString(), "PUT", data.toString());

        data.setId(noteId);
        Object note = noteService.updateNote(data);
        if (note.getClass() != Note.class) {
            return new ResponseEntity<>(new ApiResponse("error", note), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ApiResponse("success", note), HttpStatus.OK);
    }

    // ---- DELETE
    @DeleteMapping("{noteId}")
    public ResponseEntity<ApiResponse> deleteNoteById(@PathVariable Long noteId) {
        logService.WriteToLog("/notes/" + noteId.toString(), "DELETE", null);

        Object res = noteService.deleteNoteById(noteId);

        if (res != "") {
            return new ResponseEntity<>(new ApiResponse("error", res), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse("success", res), HttpStatus.OK);
    }
}
