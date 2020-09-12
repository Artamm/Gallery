package lt.gallery.repository;

import lt.gallery.dao.Picture;
import lt.gallery.dao.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {
    List<Tag> findTagsByName(String name);
    Tag findTagByName(String name);
    List<Tag>findTagsByPictures(List<Picture>pictures);

}
