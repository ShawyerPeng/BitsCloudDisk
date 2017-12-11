//import mapper.UserFileMapper;
//import mapper.UserFolderMapper;
//import mapper.UserMapper;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//import service.dto.UserDTO;
//import service.dto.UserFileDTO;
//import service.dto.UserFolderDTO;
//import util.object.DTOConvertUtil;
//
//import static org.junit.Assert.assertEquals;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//@ContextConfiguration(classes = {WebConfig.class, RootConfig.class})
//public class ManagerTest {
//    @Autowired
//    private UserMapper userDAO;
//    @Autowired
//    private UserFileMapper localFileDAO;
//    @Autowired
//    private UserFolderMapper localFolderDAO;
//    @Autowired
//    private DTOConvertUtil convertor;
//
//    @Transactional
//    @Test
//    public void testDataConvertor() {
//        UserDTO userDTO = convertor.convertToDTO(userDAO.selectByPrimaryKey(1));
//        assertEquals("admin", userDTO.getUsername());
//
//        UserFolderDTO localFolderDTO = convertor.convertToDTO(localFolderDAO.selectByPrimaryKey(1));
//        System.out.println(localFolderDTO);
//
//        UserFileDTO localFileDTO = convertor.convertToDTO(localFileDAO.selectByPrimaryKey(2), null);
//        System.out.println(localFileDTO);
//    }
//}