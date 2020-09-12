package lt.gallery.repository;

import lt.gallery.dao.Picture;
import lt.gallery.dao.Tag;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository ("PictureRepository")
public interface PictureRepository extends JpaRepository<Picture, Long>, PictureRepositoryExtended, JpaSpecificationExecutor<Picture> {

    Iterable<Picture> findByTags(List<Tag> tags);
    Picture findByFilename(String filename);
    Iterable<Picture>findByText(String text);
    Iterable<Picture>findByAuthor(String text);
    Iterable<Picture>findByDate(String text);
    Optional<Picture> findById(Long id);
    Picture findFirstByFilename(String filename);






    //Iterable<Picture> findByTag(Tag tag);

}
