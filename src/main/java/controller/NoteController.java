package controller;

import common.ResponseResult;
import controller.reqbody.NoteInsertReqBody;
import model.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import service.NoteService;
import service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class NoteController {
    public static final Logger logger = LoggerFactory.getLogger(NoteController.class);

    @Autowired
    private NoteService noteService;
    @Autowired
    private UserService userService;

    /**
     * 保存Note
     */
    @RequestMapping(value = "/users/{username}/notes/insert", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult insertNote(@PathVariable String username,
                              @RequestBody NoteInsertReqBody body) {
        Integer userId = userService.getUserByUsername(username).getUserId();
        Note note = new Note(userId, body.getTitle(), body.getContent());
        noteService.insertNote(note);
        Map<String, Object> data = new HashMap<>();
        data.put("id", note.getId());
        return ResponseResult.builder().code(201).message("笔记创建成功").data(data).build();
    }

    @RequestMapping(value = "/note/{id}")
    public String goNote(@PathVariable Integer id) {
        return "/detail";
    }

    /**
     * 获取Note
     */
    @RequestMapping(value = "/users/{username}/notes/{id}")
    @ResponseBody
    public ResponseResult getNote(@PathVariable("id") Integer id) {
        Note note = noteService.getNote(id);
        return ResponseResult.builder().code(201).message("获取成功").data(note).build();
    }

    /**
     * 编辑Note
     */
    @RequestMapping(value = "/users/{username}/notes/{id}/edit", method = RequestMethod.POST)
    @ResponseBody
    public Integer edit(@PathVariable String username,
                        @PathVariable("id") Integer id,
                        @RequestParam("title") String title, @RequestParam("content") String content) {
        Integer userId = userService.getUserByUsername(username).getUserId();
        Note note = new Note(userId, title, content);
        note.setUserId(id);
        return noteService.updateNote(note);
    }

    /**
     * 获取用户的所有Note
     */
    @RequestMapping(value = "/users/{username}/notes")
    @ResponseBody
    public ResponseResult getNoteList(@PathVariable String username) {
        Integer userId = userService.getUserByUsername(username).getUserId();
        List<Note> notes = noteService.listNote(userId);
        return ResponseResult.builder().code(201).message("获取成功").data(notes).build();
    }
}