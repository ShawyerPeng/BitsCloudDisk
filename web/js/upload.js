// $(document).ready(function () {
//     $.ajax({
//         url: 'http://localhost:8080/info/',
//         type: 'POST',
//         dataType: "json",
//         success: function (returndata) {
//             $("#sum").html(returndata.tags.length);
//         },
//         error: function (returndata) {
//             console.log(returndata.msg);
//         }
//     });
// });

function ajaxFileUpload() {
    $.ajax({
        url: 'http://localhost:8080/uploadImg/',
        type: 'POST',
        data: new FormData($("#uploadForm")[0]),
        dataType: "json",
        processData: false,
        contentType: false,
        success: function (returndata) {
            removeUpload();
        },
        error: function (returndata) {
            console.log(returndata.msg);
        }
    });
}

function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            $('.image-upload-wrap').hide();
            $('.file-upload-image').attr('src', e.target.result);
            $('.file-upload-content').show();
            $('.image-title').html(input.files[0].name);
        };
        reader.readAsDataURL(input.files[0]);
    } else {
        removeUpload();
    }
}

function removeUpload() {
    $('.file-upload-input').replaceWith($('.file-upload-input').clone());
    $('.file-upload-content').hide();
    $('.image-upload-wrap').show();
}
$('.image-upload-wrap').bind('dragover', function () {
    $('.image-upload-wrap').addClass('image-dropping');
});
$('.image-upload-wrap').bind('dragleave', function () {
    $('.image-upload-wrap').removeClass('image-dropping');
});