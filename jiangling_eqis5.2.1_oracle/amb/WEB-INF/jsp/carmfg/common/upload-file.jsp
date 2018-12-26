<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Insert title here</title>
  <script type="text/javascript">
      function fileSelected() {
        var file = document.getElementById('uploadFile').files[0];
        if (file) {
          var fileSize = 0;
          if (file.size > 1024 * 1024)
            fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
          else
            fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';

          document.getElementById('uploadFileName').value =  file.name;
          document.getElementById('fileSize').innerHTML = 'Size: ' + fileSize;
          document.getElementById('fileType').innerHTML = 'Type: ' + file.type;
        }
      }

      function uploadFiles() {
        var fd = new FormData();
        fd.append("uploadFile", document.getElementById('uploadFile').files[0]);
        fd.append("uploadFileName", document.getElementById('uploadFileName').value);
        var xhr = new XMLHttpRequest();
        xhr.upload.addEventListener("progress", uploadProgress, false);
        xhr.addEventListener("load", uploadComplete, false);
        xhr.addEventListener("error", uploadFailed, false);
        xhr.addEventListener("abort", uploadCanceled, false);
        xhr.open("POST", "upload.htm");
        xhr.send(fd);
      }

      function uploadProgress(evt) {
        if (evt.lengthComputable) {
          var percentComplete = Math.round(evt.loaded * 100 / evt.total);
          document.getElementById('progressNumber').innerHTML = percentComplete.toString() + '%';
        }
        else {
          document.getElementById('progressNumber').innerHTML = 'unable to compute';
        }
      }

      function uploadComplete(evt) {
        /* This event is raised when the server send back a response */
        window.parent.setFileinfo(JSON.parse(evt.target.responseText));
      }

      function uploadFailed(evt) {
        alert("There was an error attempting to upload the file.");
      }

      function uploadCanceled(evt) {
        alert("The upload has been canceled by the user or the browser dropped the connection.");
      }
      function delFile(){
    	  $("#form1").reset();
   	  }
    </script>
</head>
<body>
<form id="form1" enctype="multipart/form-data" method="post" action="${carmfgctx}/common/upload.htm">
<div class="row">
      <label for="fileToUpload">选择文件</label><br/>
      <input type="file" name="uploadFile" id="uploadFile" onchange="fileSelected();"/>
  </div>
		<div id="fileNameDiv"><input name="uploadFileName" readonly=readonly id="uploadFileName"/><input type="button" value="删除" onclick="delFile()" /></div>
		<div id="fileSize"></div>
		<div id="fileType"></div>
		<div class="row">
		  <input type="button" onclick="uploadFiles()" value="Upload" />
       </div>
<div id="progressNumber"></div>
</form>
</body>
</html>