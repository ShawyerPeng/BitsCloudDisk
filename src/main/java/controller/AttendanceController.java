package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import service.AttendanceService;

import java.util.List;

@Controller
@RequestMapping
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @RequestMapping("/countAllAttendance/{userId}/{dateKey}")
    public List<Long> countAllAttendance(@PathVariable Integer userId, @PathVariable String dateKey) {
        return attendanceService.activeUserIds(dateKey);
    }
}
