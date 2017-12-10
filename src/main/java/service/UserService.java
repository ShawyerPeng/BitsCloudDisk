package service;

import model.User;

public interface UserService {
    int insertUser(User user);

    boolean checkUsername(String username);

    boolean checkPassword(String username, String password);

    int changePass(String username, String password);

    User getUserByUserId(Integer userId);

    User getUserByUsername(String username);

    int deleteUser(String username, String password);

    int updateUser(User user);

    int selectRowCount();
}