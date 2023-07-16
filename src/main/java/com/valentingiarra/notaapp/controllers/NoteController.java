package com.valentingiarra.notaapp.controllers;

import com.valentingiarra.notaapp.model.ApiResponse;
import com.valentingiarra.notaapp.persistence.entities.Group;
import com.valentingiarra.notaapp.persistence.entities.Note;
import com.valentingiarra.notaapp.service.NoteService;
import com.valentingiarra.notaapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("notes")
public class NoteController {
    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // ---- GET
    @GetMapping("user_id/{userId}")
    public ResponseEntity<ApiResponse> allNotesByUserId(@PathVariable("userId") Long userId) {
        Object notes = noteService.allNotesByUserId(userId);
        if (notes.getClass() == String.class) {
            return new ResponseEntity<>(new ApiResponse("error", notes), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse("success", notes), HttpStatus.OK);
    }

    @GetMapping("user_id/{userId}/group_id/{groupId}")
    public ResponseEntity<ApiResponse> allNotesByUserIdAndGroupId(@PathVariable("userId") Long userId, @PathVariable("groupId") Long groupId) {
        Object notes = noteService.allNotesByUserIdAndGroupId(userId, groupId);
        if (notes.getClass() == String.class) {
            return new ResponseEntity<>(new ApiResponse("error", notes), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse("success", notes), HttpStatus.OK);
    }

    // ---- POST
    @PostMapping("")
    public ResponseEntity<ApiResponse> createNote(@RequestBody Note data) {
        Object note = noteService.createNote(data);

        if (note.getClass() != Note.class) {
            return new ResponseEntity<>(new ApiResponse("error", note), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse("success", note), HttpStatus.CREATED);
    }

    // ---- PUT
    @PutMapping("/{noteId}")
    public ResponseEntity<ApiResponse> updateNote(@PathVariable Long noteId, @RequestBody Note data) {
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
        Object res = noteService.deleteNoteById(noteId);

        if (res != "") {
            return new ResponseEntity<>(new ApiResponse("error", res), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse("success", res), HttpStatus.OK);
    }
}
