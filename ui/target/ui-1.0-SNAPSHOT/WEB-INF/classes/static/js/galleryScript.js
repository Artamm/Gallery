window.onscroll = function () {
    scrollFunction()
};

function scrollFunction() {

    if (document.body.scrollTop > 30 || document.documentElement.scrollTop > 30) {
        document.getElementById("top").style.display = "block";

    } else {

        document.getElementById("top").style.display = "none";

    }

}


// When the user clicks on the button, scroll to the top of the document

function topFunction() {

    document.body.scrollTop = 0; // For Safari
    document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera

}

// To instantly show preview of the  picture + data from the form
$(document).ready(function () {

    $("#imgAdd").on('change', function () {
        //sets time Preview
        var dt = new Date();
        var time = dt.getFullYear() + "-" + dt.getMonth()+"-"+ dt.getDay()+" "+
            dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
        var date_Preview = $("#DatePreview");
        date_Preview.text("Posted: "+ time);


        //Get count of selected file
        var countFiles = $(this)[0].files.length;
        var imgPath = $(this)[0].value;
        var extn = imgPath.substring(imgPath.lastIndexOf('.') + 1).toLowerCase();
        var image_holder = $("#image-holder");
        image_holder.empty();
        if (extn == "gif" || extn == "png" || extn == "jpg" || extn == "jpeg") {
            if (typeof (FileReader) != "undefined") {
                //loop for each file selected for uploaded.
                for (var i = 0; i < countFiles; i++) {
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        $("<img />", {
                            "src": e.target.result,
                            "class": "card-img-top"
                        }).appendTo(image_holder);
                    }
                    image_holder.show();
                    reader.readAsDataURL($(this)[0].files[i]);
                }
            } else {
                alert("This browser does not support FileReader.");
            }
        } else {
            alert("Please,select only images");
        }
    });


    $("#authorId").on('change', function () {
        var value = $("#authorId").val();
        var authorPreview = $("#AuthorPreview");
        authorPreview.text("By: " + value);
        authorPreview.show();
    });
    $("#textId").on('change', function () {
        var value = $("#textId").val();
        var authorPreview = $("#TextPreview");
        authorPreview.text(value);
        authorPreview.show();
    });

    $("#stringTagId").on('change', function () {
        var value = $("#stringTagId").val().split(',');
        var authorPreview = $("#stringTagsPreview");
        authorPreview.text(value);
        authorPreview.show();
    });


});