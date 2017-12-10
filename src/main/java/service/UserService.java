package service;

import dto.UserDTO;
import model.User;

public interface UserService {
    int insertUser(User user);

    boolean checkUsername(String username);

    boolean checkPassword(String username, String password);

    int changePass(String username, String password);

    UserDTO getUserByUserId(Integer userId);

    UserDTO getUserByUsername(String username);

    int deleteUser(String username, String password);

    int updateUser(User user);

    int selectRowCount();
}