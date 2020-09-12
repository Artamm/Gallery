package lt.gallery;

import lt.gallery.dao.Picture;
import lt.gallery.repository.PictureRepository;
import lt.gallery.services.PictureService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GalleryControllerTest {

    @Autowired
    private WebApplicationContext context;


    private MockMvc mockMvc;

    @MockBean
    private PictureRepository pictureRepository;

//    @Autowired
//    private PictureService pictureService;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();


    }


    @WithMockUser(username="u",password="u", roles = {"USER"})
    @Test
    public void contextLoads() throws Exception {
        mockMvc.perform(get("/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("picture")) // form
                .andExpect(model().attributeExists("pictures")); // gallery
    }


//    @WithMockUser(username="a",password="a", roles = {"ADMIN"})
//    @Test
//    public void uploadImage() throws Exception{
//        MockMultipartFile multipartFile = new MockMultipartFile("file", "preview.jpg",
//                "multipart/form-data", "test data".getBytes());
//
//        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/add")
//                .file(multipartFile))
//                .andExpect(status().is(200));
//
//    }

    @WithMockUser(username="a",password="a", roles = {"ADMIN"})
    @Test
    public void redirectNullUpdate() throws Exception{
        mockMvc.perform(get("/add").param("action=Update",""))
                .andExpect(redirectedUrl(null));
    }

    @WithMockUser(username="u",password="u", roles = {"USER"})
    @Test
    public void searchRequestTest() throws Exception{
        mockMvc.perform(get("/filter/default"))
                .andExpect(status().isOk());

    }


}
