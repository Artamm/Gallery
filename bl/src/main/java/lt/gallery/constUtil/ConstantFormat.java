package lt.gallery.constUtil;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstantFormat {

    // Picture - container for image and data
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final int IMG_WIDTH = 400;
    public static final int IMG_HEIGHT = 400;
    public static final String DEFAULT_FILENAME= "unknown.png";

}
