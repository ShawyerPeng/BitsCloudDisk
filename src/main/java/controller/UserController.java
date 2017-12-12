package controller;

import common.RestResult;
import service.dto.UserDTO;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import service.UserService;
import util.encrypt.Md5Util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public RestResult register(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        //String salt = UUID.randomUUID().toString();
        //// 密码加盐
        //user.setSalt(salt);
        //user.setPassword(Md5Util.md5(user.getPassword() + salt));

        RestResult result = new RestResult();
        Map<String, Object> data = new HashMap<>();

        if (username == null || password == null) {
            result.setStatusCode(400);
            result.setMessage("未输入用户名或密码");
            return result;
        }

        int rowCount;
        int id;
        try {
            userService.insertUser(user);
            id = user.getUserId();
            rowCount = userService.selectRowCount();
        } catch (DuplicateKeyException e) {
            result.setStatusCode(400);
            result.setMessage("用户名已存在");
            return result;
        }
        if (rowCount != 0) {
            data.put("id", id);
            result.setData(data);
            result.setStatusCode(201);
            result.setMessage("注册成功");
            return result;
        }
        return result;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public RestResult login(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        //password = Md5Util.md5(user.getPassword() + user.getSalt());

        RestResult result = new RestResult();
        Map<String, Object> data = new HashMap<>();

        if (username == null || password == null) {
            result.setData(data);
            result.setStatusCode(400);
            result.setMessage("未输入用户名或密码");
            return result;
        }

        boolean exists = userService.checkUsername(username);
        if (!exists) {
            result.setData(data);
            result.setStatusCode(400);
            result.setMessage("不存在此用户");
            return result;
        }

        boolean success = userService.checkPassword(username, password);
        if (!success) {
            result.setData(data);
            result.setStatusCode(400);
            result.setMessage("密码错误");
            return result;
        } else {
            //String token = JwtUtil.generToken("Shawyer", null, null);
            //data.put("token", token);
            result.setData(data);
            result.setStatusCode(200);
            result.setMessage("登陆成功");
            return result;
        }
    }

    // http://localhost:8080/user/check?username=admin
    @RequestMapping("/check")
    @ResponseBody
    public RestResult isExistsUserName(@RequestBody User user) {
        RestResult result = new RestResult();
        Map<String, Object> data = new HashMap<>();

        String username = user.getUsername();
        boolean exists = userService.checkUsername(username);
        if (exists) {
            data.put("isExists", true);
            result.setData(data);
            result.setStatusCode(200);
            result.setMessage("查询成功，存在此用户");
            return result;
        } else {
            data.put("isExists", false);
            result.setData(data);
            result.setStatusCode(200);
            result.setMessage("查询成功，不存在此用户");
            return result;
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public RestResult delete(@RequestBody User user) {
        RestResult result = new RestResult();
        Map<String, Object> data = new HashMap<>();

        String username = user.getUsername();
        String password = user.getPassword();
        int success = userService.deleteUser(username, password);
        if (success == 1) {
            result.setData(data);
            result.setStatusCode(204);
            result.setMessage("删除成功");
            return result;
        } else {
            result.setData(data);
            result.setStatusCode(400);
            result.setMessage("删除失败");
            return result;
        }

    }

    @RequestMapping(value = "/changePass", method = RequestMethod.POST)
    @ResponseBody
    public RestResult changePass(@RequestBody User user) {
        RestResult result = new RestResult();
        Map<String, Object> data = new HashMap<>();

        String username = user.getUsername();
        String newPass = user.getPassword();
        int success = userService.changePass(username, newPass);

        if (success == 1) {
            result.setData(data);
            result.setStatusCode(201);
            result.setMessage("密码修改成功");
            return result;
        } else {
            result.setData(data);
            result.setStatusCode(400);
            result.setMessage("密码修改失败");
            return result;
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public RestResult edit(@RequestBody User user) {
        RestResult result = new RestResult();
        Map<String, Object> data = new HashMap<>();

        int success = userService.updateUser(user);
        if (success == 1) {
            result.setData(data);
            result.setStatusCode(201);
            result.setMessage("用户信息修改成功");
            return result;
        } else {
            result.setData(data);
            result.setStatusCode(400);
            result.setMessage("用户信息修改失败");
            return result;
        }
    }

    // http://localhost:8080/user/infoByUsername?username=admin
    @RequestMapping("/infoByUsername")
    @ResponseBody
    public RestResult info(@RequestBody User user) {
        RestResult result = new RestResult();
        Map<String, Object> data = new HashMap<>();

        String username = user.getUsername();
        UserDTO getUser = userService.getUserByUsername(username);

        if (getUser != null) {
            data.put("user", getUser);
            result.setData(data);
            result.setStatusCode(201);
            result.setMessage("用户信息获取成功");
            return result;
        } else {
            result.setData(data);
            result.setStatusCode(400);
            result.setMessage("用户信息获取失败");
            return result;
        }
    }

    // http://localhost:8080/user/infoByUserId?userId=admin
    @RequestMapping("/infoByUserId")
    @ResponseBody
    public RestResult infoById(@RequestBody User user) {
        RestResult result = new RestResult();
        Map<String, Object> data = new HashMap<>();

        Integer userId = user.getUserId();
        UserDTO getUser = userService.getUserByUserId(userId);

        if (getUser != null) {
            data.put("user", getUser);
            result.setData(data);
            result.setStatusCode(201);
            result.setMessage("用户信息获取成功");
            return result;
        } else {
            result.setData(data);
            result.setStatusCode(400);
            result.setMessage("用户信息获取失败");
            return result;
        }
    }
}