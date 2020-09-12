package lt.gallery.dao;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity(name = "picture")
@Table(name = "picture")
@Data
@NoArgsConstructor
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /*to store in DB*/
    private String filename;
    /*Extra fields */
    private String date;
    private String author;
    private String text;


    @Lob
    private byte[] fullImage;
    @Lob
    private byte[] thumbnail;


    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
    },
            fetch = FetchType.EAGER)
    @JoinTable(name = "picture_tag",
            joinColumns = @JoinColumn(name = "picture_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username")
    private User owner;


    private String stringTags;

    public Picture(String filename, String author, String text,String stringTags) {
        this.filename = filename;
        this.author = author;
        this.text = text;
        this.stringTags = stringTags;
    }
}
