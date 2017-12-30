package service;

import model.Note;

import java.util.List;

public interface NoteService {
    int insertNote(Note note);

    int deleteNote(Integer id);

    int updateNote(Note note);

    Note getNote(Integer id);

    List<Note> listNote(Integer userId);
}