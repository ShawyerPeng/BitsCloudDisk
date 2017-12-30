package service.impl;

import mapper.NoteMapper;
import model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.NoteService;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    private NoteMapper noteMapper;

    @Override
    public int insertNote(Note note) {
        return noteMapper.insert(note);
    }

    @Override
    public int deleteNote(Integer id) {
        return noteMapper.delete(id);
    }

    @Override
    public int updateNote(Note note) {
        return noteMapper.updateByPrimaryKeySelective(note);
    }

    @Override
    public Note getNote(Integer id) {
        return noteMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Note> listNote(Integer userId) {
        return noteMapper.selectByUserId(userId);
    }
}