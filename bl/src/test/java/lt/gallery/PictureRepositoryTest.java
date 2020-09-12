package lt.gallery;

import lt.gallery.dao.Picture;
import lt.gallery.repository.PictureRepository;
import lt.gallery.repository.TagRepository;
import lt.gallery.services.PictureService;
import lt.gallery.services.impl.PictureServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.context.junit4.SpringRunner;


import static org.mockito.ArgumentMatchers.any;


// Should I test it at all?
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PictureServiceImpl.class)
public class PictureRepositoryTest {

    @MockBean
    private PictureRepository pictureRepository;

    @MockBean
    private TagRepository tagRepository;

    @Autowired
    private PictureService pictureService;



    @Test
    public void testSave() {
        Picture picture = new Picture();
        picture.setAuthor("author");
        picture.setFilename("filename");
        picture.setText("text");
        picture.setDate("2019-03-21 18:03");

        boolean isPictureCreated = pictureService.savePicture(picture);
        Assert.assertTrue(isPictureCreated);

        Mockito.verify(pictureRepository,Mockito.times(1)).save(picture);

        Mockito.when(pictureRepository.save(any(Picture.class))).thenReturn(picture);
        Picture pic = pictureRepository.save(picture);
        Assert.assertEquals(picture, pic);
    }
}
