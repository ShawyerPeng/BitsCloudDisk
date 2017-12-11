//import controller.LoginController;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//@ContextConfiguration(classes = {WebConfig.class, RootConfig.class})
//public class ControllerTest {
//    @Autowired
//    private WebApplicationContext webAppContext;
//    @Autowired
//    private LoginController loginController;
//    private MockMvc mockMvc;
//
//    @Before
//    public void setup() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
//    }
//
//    /**
//     * 测试（200）与Run on server（404）结果不一致，
//     * 如果没有在WebConfig配置类里加上ComponentScan则Controller不会被扫描到，
//     * 但测试时Controller却被扫描到了
//     */
//    @Test
//    public void testLoginController() throws Exception {
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
//        mockMvc.perform(MockMvcRequestBuilders.post("/authentication")).andExpect(MockMvcResultMatchers.view().name("login"));
//    }
//}