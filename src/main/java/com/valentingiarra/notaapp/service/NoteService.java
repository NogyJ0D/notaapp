package com.valentingiarra.notaapp.service;

import com.valentingiarra.notaapp.persistence.entities.Group;
import com.valentingiarra.notaapp.persistence.entities.Note;
import com.valentingiarra.notaapp.persistence.entities.User;
import com.valentingiarra.notaapp.persistence.repositories.GroupRepository;
import com.valentingiarra.notaapp.persistence.repositories.NoteRepository;
import com.valentingiarra.notaapp.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository, UserRepository userRepository, GroupRepository groupRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public Object allNotesByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return "User not found";
        }

        List<Note> notes = noteRepository.findAllByUserIdOrderByGroupPositionAscPositionAsc(userId);
        return notes;
    }

    public Object allNotesByUserIdAndGroupId(Long userId, Long groupId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return "User not found";
        }
        Group group = groupRepository.findById(groupId).orElse(null);
        if (group == null) {
            return "Group not found";
        }

        List<Note> notes = noteRepository.findAllByUserIdAndGroupIdOrderByPosition(userId, groupId);
        return notes;
    }

    public Object createNote(Note data) {
        User user = userRepository.findById(data.getUser().getId()).orElse(null);
        if (user == null) {
            return "User not found";
        }

        Group group = groupRepository.findById(data.getGroup().getId()).orElse(null);
        if (group == null) {
            return "Group not found";
        }

        Integer maxPos = noteRepository.findMaxPositionByUserIdAndGroupId(user.getId(), group.getId());
        if (maxPos == null) {
            maxPos = 0;
        }

        data.setUser(user);
        data.setPosition(maxPos + 1);
        data.setGroup(group);
        data.setBackcolor(group.getBackcolor());
        data.setForecolor(group.getForecolor());
        return noteRepository.save(data);
    }

    public Object updateNote(Note data) {
        Note note = noteRepository.findById(data.getId()).orElse(null);
        if (note == null) {
            return "Note not found";
        }

        Group originalGroup = note.getGroup();
        Group newGroup = data.getGroup();

        if (!note.getPosition().equals(data.getPosition())) {
            Note prevPositionNote = noteRepository.findByUserIdAndGroupIdAndPosition(
                    data.getUser().getId(), newGroup.getId(), data.getPosition()
            );

            if (prevPositionNote != null) {
                prevPositionNote.setPosition(note.getPosition());
                noteRepository.save(prevPositionNote);
            }

            note.setPosition(data.getPosition());
        }

        // Update positions in old group
        if (!originalGroup.equals(newGroup)) {
            List<Note> originalGroupNotes = noteRepository.findAllByUserIdAndGroupIdOrderByPosition(note.getUser().getId(), originalGroup.getId());
            originalGroupNotes.remove(note);

            int pos = 1;
            for (Note originalNote : originalGroupNotes) {
                originalNote.setPosition(pos);
                noteRepository.save(originalNote);
                pos++;
            }

            // Update position in new group
            List<Note> newGroupNotes = noteRepository.findAllByUserIdAndGroupIdOrderByPosition(note.getUser().getId(), newGroup.getId());

            int newPosition = newGroupNotes.size() + 1;
            note.setPosition(newPosition);
        }

        note.setText(data.getText());
        note.setBackcolor(data.getBackcolor());
        note.setForecolor(data.getForecolor());
        note.setGroup(newGroup);
        note.setTitle(data.getTitle());
        return noteRepository.save(note);
    }

    public Object deleteNoteById(Long noteId) {
        Note note = noteRepository.findById(noteId).orElse(null);
        if (note == null) {
            return "Note not found";
        }

        noteRepository.delete(note);

        // Reorder notes position
        List<Note> notes = noteRepository.findAllByUserIdAndGroupIdOrderByPosition(note.getUser().getId(), note.getGroup().getId());
        int pos = 1;
        for (Note originalNote : notes) {
            originalNote.setPosition(pos);
            noteRepository.save(originalNote);
            pos++;
        }

        return "";
    }
}
