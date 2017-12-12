import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import service.AttendanceService;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
public class UserActiveTest {
    @Autowired
    private AttendanceService attendanceService;

    @Test
    public void testActiveUser() {
        String dateKey = "20150410";
        for (int i = 1; i < 10000; i += 2) {
            attendanceService.activeUser(i, dateKey);
        }
        dateKey = "20150409";
        for (int i = 1; i < 10000; i += 4) {
            attendanceService.activeUser(i, dateKey);
        }
        dateKey = "20150408";
        for (int i = 1; i < 10000; i += 6) {
            attendanceService.activeUser(i, dateKey);
        }
    }

    @Test
    public void testTotalCountUsers() {
        String dateKey = "20150410";
        System.out.println(attendanceService.totalCountUsers(dateKey));
    }

    @Test
    public void testContinueCountUsers() {
        String[] dateKey = new String[]{"20150408", "20150409", "20150410"};
        List<Long> results =  attendanceService.continueActiveUserCount(dateKey);
        System.out.println("---" + results.size());
        for (int i = 0; i < results.size(); i++) {
            System.out.println(results.get(i) + " ");
        }
    }
}
