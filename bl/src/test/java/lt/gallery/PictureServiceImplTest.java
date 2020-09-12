package lt.gallery;

import javafx.application.Application;
import lt.gallery.dao.Picture;
import lt.gallery.dao.Tag;
import lt.gallery.repository.PictureRepository;
import lt.gallery.repository.TagRepository;
import lt.gallery.services.PictureService;
import lt.gallery.services.impl.PictureServiceImpl;

import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.context.junit4.SpringRunner;



import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Arrays;




// Testing logic
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {PictureServiceImpl.class})

public class PictureServiceImplTest {

    @MockBean
    private PictureRepository pictureRepository;

    @MockBean
    private TagRepository tagRepository;

    @Autowired
    private PictureService pictureService;


//
//    @Test
//    public void addPictureWithImageTest() throws Exception{
//        Picture picture = new Picture();
//        picture.setFilename("SomeAuthor");
//        pictureService.postDate(picture);
//        MockMultipartFile multipartFile = new MockMultipartFile("file","preview.jpg",)
//
//    }
//


    @Test
    public void updateWithoutImageTest() throws Exception {
        Picture picture = new Picture("Test", "picture", "Something", "tag1,tag2,deleteTag");
        picture.setTags(Arrays.asList(new Tag("tag1"), new Tag("tag2"), new Tag("deleteTag")));

        Picture modified = new Picture("Test2", "picture2", "Something2", "tag1,tag2");
        modified.setTags(Arrays.asList(new Tag("tag1"), new Tag("tag2")));

        pictureService.updateValues(picture, modified);
        Assert.assertEquals(modified.getStringTags(), picture.getStringTags());
        Assert.assertEquals(modified.getAuthor(), picture.getAuthor());
        Assert.assertEquals(modified.getText(), picture.getText());
        Assert.assertEquals(modified.getFilename(), picture.getFilename());

    }

//    public void listTags() throws Exception {
//        String[] tags ={"Exist", "New"};
//        List<Tag> new_tags = Arrays.asList();
//        Mockito.when(tagRepository.findTagsByName(tags[0].toLowerCase()).isEmpty()).thenReturn(false);
//        Mockito.when(tagRepository.findTagsByName(tags[1].toLowerCase()).isEmpty()).thenReturn(true);
//
//        Tag tag= new Tag("Exist");
//        pictureService.fillTagList(tags,new_tags);
//        assertThat(new_tags,not(IsEmptyCollection.empty()));
//    }
//

    @Test
    public void createThumbnailTest() throws Exception {
        Path path = Paths.get("C:/Users/marta.mutalipova/IdeaProjects/ZK/images/as.jpg");
        String name = "as.jpg";
        String originalFileName = "as.jpg";
        String contentType = "image/jpg";
        byte[] content = null;

        content = Files.readAllBytes(path);
        Picture picture = new Picture("Test", "picture", "Something", "tag1,tag2,deleteTag");
        picture.setFullImage(content);
        Mockito.when(pictureRepository.save(picture)).thenReturn(picture);
        pictureService.setValues(picture, picture.getFilename());
        Assert.assertNotNull(picture.getThumbnail());
    }


}
