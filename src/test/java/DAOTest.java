//import config.RootConfig;
//import config.WebConfig;
//import entity.FileDO;
//import entity.LocalFileDO;
//import entity.LocalFolderDO;
//import mapper.OriginFileMapper;
//import mapper.UserMapper;
//import model.OriginFile;
//import model.User;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.Date;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//@ContextConfiguration(classes = { WebConfig.class, RootConfig.class } )
//public class DAOTest {
//
//    @Autowired
//    private UserMapper userDAO;
//
//    @Transactional
//    @Test
//    public void testUserDAO() {
//        User getTest = userDAO.selectByPrimaryKey(1);
//        assertEquals("admin", getTest.getUsername());
//
//        User saveTest = new User();
//        saveTest.setUsername("save" + System.currentTimeMillis());
//        saveTest.setPassword("password");
//        saveTest.setNickname("nickname");
//        saveTest.setIconimg("save" + System.currentTimeMillis());
//        saveTest.setUsedSize(10000L);
//        userDAO.insert(saveTest);
//        System.out.println(saveTest.getUserId());// 数据库生成的自增主键
//
//        User updateTest = userDAO.selectByPrimaryKey(2);
//        updateTest.setUsername("update" + System.currentTimeMillis());
//        updateTest.setIconimg("update" + System.currentTimeMillis());
//        userDAO.updateByPrimaryKeySelective(updateTest);
//
//        User removeTest = userDAO.selectByPrimaryKey(9);
//        userDAO.deleteByPrimaryKey(removeTest.getUserId());
//
//        System.out.println(userDAO.selectByUsername("admin"));
//    }
//
//    @Autowired
//    private OriginFileMapper fileDAO;
//
//    @Transactional
//    @Test
//    public void testFileDAO() {
//        OriginFile getTest = fileDAO.selectByPrimaryKey(29);
//        assertTrue(306407 == getTest.getFileSize());
//
//        OriginFile saveTest = new OriginFile();
//        saveTest.setCreateTime(new Date());
//        saveTest.setModifyTime(new Date());
//        saveTest.setFileMd5("save" + System.currentTimeMillis());
//        saveTest.setFileSize(15615616L);
//        saveTest.setFileType("save test");
//        saveTest.setFileUrl("save" + System.currentTimeMillis());
//        fileDAO.insert(saveTest);
//        System.out.println(saveTest.getOriginFileId());// 数据库生成的自增主键
//
//        OriginFile updateTest = fileDAO.selectByPrimaryKey(33);
//        updateTest.setFileMd5("update" + System.currentTimeMillis());
//        updateTest.setFileUrl("update" + System.currentTimeMillis());
//        fileDAO.updateByPrimaryKeySelective(updateTest);
//
//        FileDO removeTest = fileDAO.selectByPrimaryKey(saveTest.getOriginFileId());
//        fileDAO.deleteByPrimaryKey(removeTest.getId());
//
//        System.out.println(fileDAO.getByFileMd5("8486f0c3cd2d48fd0b24eb1045e338f6"));
//    }
//
//    @Autowired
//    private LocalFolderDAO localFolderDAO;
//
//    @Transactional
//    @Test
//    public void testLocalFolderDAO() {
//        LocalFolderDO getTest = localFolderDAO.get(1L);
//        assertEquals("home", getTest.getLocalName());
//
//        LocalFolderDO saveTest = new LocalFolderDO();
//        saveTest.setLdtCreate(LocalDateTime.now());
//        saveTest.setLdtModified(LocalDateTime.now());
//        saveTest.setLocalName("save test");
//        saveTest.setUserID(System.currentTimeMillis());
//        saveTest.setParent(System.currentTimeMillis());
//        localFolderDAO.save(saveTest);
//        System.out.println(saveTest.getId());// 数据库生成的自增主键
//
//        LocalFolderDO updateTest = localFolderDAO.get(11L);
//        updateTest.setUserID(System.currentTimeMillis());
//        updateTest.setParent(System.currentTimeMillis());
//        updateTest.setLocalName("update test");
//        localFolderDAO.update(updateTest);
//
//        LocalFolderDO removeTest = localFolderDAO.get(saveTest.getId());
//        localFolderDAO.remove(removeTest);
//
//        System.out.println(localFolderDAO.listByParent(1L));
//        System.out.println(localFolderDAO.listByName(1L, "图"));
//    }
//
//    @Autowired
//    private LocalFileDAO localFileDAO;
//
//    @Transactional
//    @Test
//    public void testLocalFileDAO() {
//        LocalFileDO getTest = localFileDAO.get(66L);
//        assertTrue(32L == getTest.getFileID());
//
//        LocalFileDO saveTest = new LocalFileDO();
//        saveTest.setLdtCreate(LocalDateTime.now());
//        saveTest.setLdtModified(LocalDateTime.now());
//        saveTest.setUserID(System.currentTimeMillis());
//        saveTest.setFileID(System.currentTimeMillis());
//        saveTest.setLocalName("save test");
//        saveTest.setLocalType("save test");
//        saveTest.setParent(System.currentTimeMillis());
//        localFileDAO.save(saveTest);
//        System.out.println(saveTest.getId());// 数据库生成的自增主键
//
//        LocalFileDO updateTest = localFileDAO.get(75L);
//        updateTest.setUserID(System.currentTimeMillis());
//        updateTest.setParent(System.currentTimeMillis());
//        updateTest.setLocalName("update test");
//        updateTest.setLocalType("update test");
//        updateTest.setParent(System.currentTimeMillis());
//        localFileDAO.update(updateTest);
//
//        LocalFileDO removeTest = localFileDAO.get(saveTest.getId());
//        localFileDAO.remove(removeTest);
//
//        System.out.println(localFileDAO.getByPath(1L, 6L, "文本", "txt"));
//        System.out.println(localFileDAO.listByParent(8L));
//        System.out.println(localFileDAO.listRecentFile(1L));
//        System.out.println(localFileDAO.listByName(1L, "本.t"));
//        System.out.println(localFileDAO.listByLocalType(1L, new String[]{"txt","mp3","mp4","gif"}));
//    }
//
//}
