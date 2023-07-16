package com.valentingiarra.notaapp.service;

import com.valentingiarra.notaapp.persistence.entities.Group;
import com.valentingiarra.notaapp.persistence.entities.Note;
import com.valentingiarra.notaapp.persistence.entities.User;
import com.valentingiarra.notaapp.persistence.repositories.GroupRepository;
import com.valentingiarra.notaapp.persistence.repositories.NoteRepository;
import com.valentingiarra.notaapp.persistence.repositories.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final NoteRepository noteRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, UserRepository userRepository, NoteRepository noteRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
    }

    public List<Group> getGroupsByUserId(Long userId) {
        return groupRepository.findAllByUserIdOrderByPosition(userId);
    }

    public Object createGroup(Group data) {
        User user = userRepository.findById(data.getUser().getId()).orElse(null);
        if (user == null) {
            return "User not found";
        }

        if (data.getName().equalsIgnoreCase("Without group")) {
            return "Invalid name";
        }

        Integer maxPos = groupRepository.findMaxPositionByUserId(user.getId());
        if (maxPos == null) {
            maxPos = 0;
        }

        data.setUser(user);
        data.setPosition(maxPos + 1);
        return groupRepository.save(data);
    }

    public Object updateGroup(Group data) {
        Group group = groupRepository.findById(data.getId()).orElse(null);
        if (group == null) {
            return "Group not found";
        }

        if (group.getPosition() == 1) {
            return "Cannot modify reserved group";
        }

        if (data.getPosition() == 1) {
            return "Cannot set position 1 for a group";
        }

        if (!group.getPosition().equals(data.getPosition())) {
            Group prevPositionGroup = groupRepository.findByUserIdAndPosition(data.getUser().getId(), data.getPosition());

            if (prevPositionGroup != null) {
                prevPositionGroup.setPosition(group.getPosition());

                groupRepository.save(prevPositionGroup);
            }
            group.setPosition(data.getPosition());
        }

        group.setName(data.getName());
        group.setBackcolor(data.getBackcolor());
        group.setForecolor(data.getForecolor());
        return groupRepository.save(group);
    }

    public Object deleteGroupById(Long groupId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        if (group == null) {
            return "Group not found";
        }

        if (group.getPosition() == 1 && group.getName().equalsIgnoreCase("Without group")) {
            return "Cannot delete reserved group";
        }

        // Move notes from this group to reserved group
        User user = group.getUser();
        Group withoutGroup = groupRepository.findByUserIdAndPosition(user.getId(), 1);

        List<Note> notes = group.getNotes();
        if (notes != null && !notes.isEmpty()) {
            for (Note note : notes) {
                note.setGroup(withoutGroup);
                withoutGroup.appendNote(note);
            }
        }

        groupRepository.delete(group);

        // Add position to new notes in reserved group
        List<Note> withoutGroupNotes = withoutGroup.getNotes();
        if (withoutGroupNotes != null && !withoutGroupNotes.isEmpty()) {
            int pos = 1;
            for (Note note : withoutGroupNotes) {
                note.setPosition(pos);
                noteRepository.save(note);
                pos++;
            }
        }

        // Reorder groups position
        List<Group> groups = groupRepository.findAllByUserIdOrderByPosition(group.getUser().getId());
        if (groups.size() > 0) {
            for (int i = 0; i < groups.size(); i ++) {
                Group alterGroup = groups.get(i);
                alterGroup.setPosition(i + 1);
                groupRepository.save(alterGroup);
            }
        }
        return "";
    }
}
