package util.validation;

import mapper.UserMapper;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ParamChecker {
    @Autowired
    private UserMapper userMapper;

    /**
     * @return {@code true} if user has enough storage, {@code false} otherwise
     */
    @Transactional(readOnly = true)
    public boolean isUserStorageEnough(int userId, long fileSize) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user.getUsedSize() + fileSize > user.getMemorySize()) {
            return false;
        }
        return true;
    }
}