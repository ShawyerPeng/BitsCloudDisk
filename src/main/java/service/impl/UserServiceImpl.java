package service.impl;

import service.dto.UserDTO;
import mapper.UserMapper;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.UserService;
import util.JwtUtil;
import util.object.DTOConvertUtil;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DTOConvertUtil convertor;

    @Override
    public int insertUser(User user) {
        return userMapper.insert(user);
    }

    @Override
    public boolean checkUsername(String username) {
        User user = userMapper.selectByUsername(username);
        if (user != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkPassword(String username, String password) {
        User user = userMapper.selectByPassword(username, password);
        if (user != null) {
            return true;
        }
        return false;
    }

    @Override
    public String login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            System.out.println("用户名不存在");
        } else if (user.getPassword().equals(password)) {
            return JwtUtil.generateToken(username);
        } else {
            System.out.println("密码错误");
        }
        return "登陆失败";
    }

    @Override
    public int changePass(String username, String password) {
        int userId = getUserByUsername(username).getUserId();
        User user = new User();
        user.setUserId(userId);
        user.setPassword(password);
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return null;
        } else {
            return convertor.convertToDTO(user);
        }
    }

    @Override
    public UserDTO getUserByUserId(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return null;
        } else {
            return convertor.convertToDTO(user);
        }
    }

    @Override
    public int deleteUser(String username, String password) {
        User user = userMapper.selectByUsername(username);
        return userMapper.deleteByPrimaryKey(user.getUserId());
    }

    @Override
    public int updateUser(User user) {
        int userId = getUserByUsername(user.getUsername()).getUserId();
        user.setUserId(userId);
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int selectRowCount() {
        return userMapper.selectRowCount();
    }
}