package lt.gallery;

import lt.gallery.dao.Picture;
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

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes ={PictureServiceImpl.class, PictureServiceImpl.class})

public class CriteriaTest {

    @MockBean
    private PictureRepository pictureRepository;

    @MockBean
    private TagRepository tagRepository;

    @Autowired
    private PictureService pictureService;



    @Test
    public void insensitiveSearch(){
        Picture picture = new Picture();
        picture.setAuthor("docker");
        picture.setFilename("docker");
        picture.setText("text");
        picture.setDate("2019-03-21 18:03");

        Mockito.when(pictureRepository.save(any(Picture.class))).thenReturn(picture);
        Picture pic = pictureRepository.save(picture);
        List<Picture> pictures = new ArrayList<>();
        pictures.add(pic);
        Mockito.when(pictureService.inSensitiveSearch(picture.getFilename())).thenReturn(pictures);

        List <Picture> list = pictureService.inSensitiveSearch(pic.getFilename());
        Assert.assertEquals(pictures,list);


    }
}
