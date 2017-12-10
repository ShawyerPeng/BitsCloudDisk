<!DOCTYPE html>
<html>
<head>
    <title>Spring MVC + Dropzone.js Example</title>

    <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>

    <script src="../utils/dropzone/dropzone.min.js"></script>
    <link href="../utils/dropzone/dropzone.min.css">
    <link href="../css/style.css"/>

    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link href="https://cdn.bootcss.com/bootstrap3-dialog/1.35.4/css/bootstrap-dialog.min.css">
    <script src="https://cdn.bootcss.com/bootstrap3-dialog/1.35.4/js/bootstrap-dialog.min.js"></script>
</head>

<body>
	<div class="container">
		<div class="panel panel-default">
			<div class="panel-heading text-center">
				<h3>Spring MVC + Dropzone.js Example</h3>
			</div>
			<div class="panel-body">
				<div>
					<form action="/upload" id="dropzone-form" class="dropzone" enctype="multipart/form-data">
						<div class="dz-default dz-message file-dropzone text-center well col-sm-12">
							<span class="glyphicon glyphicon-paperclip"></span> <span>
								To attach files, drag and drop here</span><br> <span>OR</span><br>
							<span>Just Click</span>
						</div>

						<!-- this is were the previews should be shown. -->
						<div class="dropzone-previews"></div>
					</form>
					<hr>
					<button id="upload-button" class="btn btn-primary">
						<span class="glyphicon glyphicon-upload"></span> Upload
					</button>
					<a class="btn btn-primary pull-right" href="/list">
						<span class="glyphicon glyphicon-eye-open"></span> View All Uploads
					</a>
				</div>
			</div>
		</div>
	</div>
</body>

<script type="text/javascript">
    $(document).ready(function() {
        $(".file-dropzone").on('dragover', handleDragEnter);
        $(".file-dropzone").on('dragleave', handleDragLeave);
        $(".file-dropzone").on('drop', handleDragLeave);

        function handleDragEnter(e) {
            this.classList.add('drag-over');
        }

        function handleDragLeave(e) {
            this.classList.remove('drag-over');
        }

        // "dropzoneForm" is the camel-case version of the form id "dropzone-form"
        Dropzone.options.dropzoneForm = {
            url : "/upload",
            autoProcessQueue : false,
            uploadMultiple : true,
            maxFilesize : 256, // MB
            parallelUploads : 100,
            maxFiles : 100,
            addRemoveLinks : true,
            previewsContainer : ".dropzone-previews",

            // The setting up of the dropzone
            init : function() {
                var myDropzone = this;

                // first set autoProcessQueue = false
                $('#upload-button').on("click", function(e) {
                    myDropzone.processQueue();
                });

                // customizing the default progress bar
                this.on("uploadprogress", function(file, progress) {
                    progress = parseFloat(progress).toFixed(0);
                    var progressBar = file.previewElement.getElementsByClassName("dz-upload")[0];
                    progressBar.innerHTML = progress + "%";
                });

                // displaying the uploaded files information in a Bootstrap dialog
                this.on("successmultiple", function(files, serverResponse) {
                    showInformationDialog(files, serverResponse);
                });
            }
        }

        function showInformationDialog(files, objectArray) {
            var responseContent = "";
            for (var i = 0; i < objectArray.length; i++) {
                var infoObject = objectArray[i];
                for ( var infoKey in infoObject) {
                    if (infoObject.hasOwnProperty(infoKey)) {
                        responseContent = responseContent + " " + infoKey + " -> " + infoObject[infoKey] + "<br>";
                    }
                }
                responseContent = responseContent + "<hr>";
            }

            BootstrapDialog.show({
                title : '<b>Server Response</b>',
                message : responseContent
            });
        }
    });
</script>
</html>