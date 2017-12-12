package service;

import java.util.List;

public interface AttendanceService {
    void activeUser(long userId, String dateKey);

    boolean isActiveUser(long userId, String dateKey);

    long totalCountUsers(String dateKey);

    List<Long> activeUserIds(String dateKey);

    List<Long> continueActiveUserCount(String... dateKeys);
}