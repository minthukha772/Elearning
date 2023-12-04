$(function () {
  $("#deleteCourseConfirmModal").on("show.bs.modal", function (event) {
    const button = $(event.relatedTarget);

    const title = button.data("bs-title"); // Extract info from data-* attributes
    const courseId = button.data("bs-id"); // Extract info from data-* attributes

    const modal = $(this);
    modal.find(".modal-title").text("Delete " + title);
    modal.find("#deleteCourse").click((e) => {
      $.ajax({
        type: "delete",
        url: "/admin/course-details/delete/",
        data: { courseId },
      }).done(function () {
        //reload page
        window.location.href = "/admin/top/";
      }).fail(function () {
        alert("Something went wrong");
      });
    });

    /* modal.find('.modal-body input').val(recipient) */
  });

  //verify course
  $("#verifyCourseConfirmModal").on("show.bs.modal", function (event) {
    const button = $(event.relatedTarget);

    const title = button.data("bs-title"); // Extract info from data-* attributes
    const courseId = button.data("bs-id"); // Extract info from data-* attributes

    const modal = $(this);
    modal.find(".modal-title").text("Verify " + title);
    modal.find("#verifyCourse").click((e) => {
      $.ajax({
        type: "post",
        url: "/admin/course-details/verify/",
        data: { courseId },
      }).done(function () {
        //reload page
        window.location.href = "/guest/course-detail/" + courseId;
      }).fail(function () {
        alert("Something went wrong");
      });
    });

    /* modal.find('.modal-body input').val(recipient) */
  });
});

// Upload a video Dialog start
let upload_a_video = document.querySelector('#upload_a_video');
let upload_a_video_box = document.querySelector('.upload-a-video-box');
let upload_a_video_cancel = document.querySelector('#upload_a_video_cancel');

upload_a_video.addEventListener('click', () => {
  upload_a_video_box.classList.add('animate__fadeIn');
  upload_a_video_box.classList.remove('d-none', 'animate__fadeOut');
})

upload_a_video_cancel.addEventListener('click', () => {
  upload_a_video_box.classList.remove('animate__fadeIn');
  upload_a_video_box.classList.add('animate__fadeOut');
  setTimeout(() => {
    upload_a_video_box.classList.add('d-none');
    removeErrorMsg(uploadVideoErrorMsgBox);
  }, 1000);
})
// Upload a video Dialog end

let uploadVideoErrorMsgBox = document.querySelector('#uploadVideoErrorMsgBox');
let editVideoErrorMsgBox = document.querySelector('#editVideoErrorMsgBox');
let videoUploadForm = document.querySelector('#videoUploadForm');
let uploadOrderNo = document.querySelector('#upload_order_no');
let uploadVideoTitle = document.querySelector('#upload_title');
let uploadVideoDescription = document.querySelector('#upload_description');
let editOrderNo = document.querySelector('#edit_order_no');
let editVideoTitle = document.querySelector('#edit_title');
let editVideoDescription = document.querySelector('#edit_description');
//let editVideoBtn = document.querySelector('#editVideoBtn');

let courseIdInput = document.querySelector('#courseIdElement');
let videoID = document.querySelector('#video_id');

function removeAllChildNodes(parent) {
  while (parent.firstChild) {
    parent.removeChild(parent.firstChild);
  }
}

function removeErrorMsg(errorMsgBox) {
  const noError = errorMsgBox.classList.contains("d-none");
  if (!noError) {
    errorMsgBox.classList.replace("d-flex", "d-none");
    if (errorMsgBox.classList.contains("border-danger")) {
      errorMsgBox.classList.remove("border-danger");
    }
    if (errorMsgBox.classList.contains("border-success")) {
      errorMsgBox.classList.remove("border-success");
    }
    removeAllChildNodes(errorMsgBox);
  }
}

// filename show start
let video_upload = document.querySelector('#videoUpload');
const input = document.querySelector("#fileDialog");
const preview = document.querySelector(".preview");

const openFileDialog = () => {
  input.click();
  removeErrorMsg(uploadVideoErrorMsgBox);
}
video_upload.addEventListener('click', openFileDialog)
input.addEventListener("change", showFileName);

function showFileName() {
  while (preview.firstChild) {
    preview.removeChild(preview.firstChild);
  }

  const curFiles = input.files;
  if (curFiles.length === 0) {
    const para = document.createElement("div");
    para.style.fontSize = "14px";
    para.textContent = "...";
    preview.appendChild(para);
  } else {
    const para = document.createElement("div");
    para.style.fontSize = "14px";
    preview.appendChild(para);

    for (const file of curFiles) {
      para.textContent = file.name;
    }
  }
}
// filename show end

// Check input data and send data to backend for Upload a video Dialog
document.getElementById('videoUploadForm').addEventListener('submit', function (e) {
  e.preventDefault();
  removeErrorMsg(document.getElementById('uploadVideoErrorMsgBox'));
  const file = e.target.videoFile.files[0];

  // Check if a file is selected
  if (!file) {
    createErrorMsg(document.getElementById('uploadVideoErrorMsgBox'), "Please select a video!");
    return;
  }

  // Check if file type is correct
  const allowedFileType = "video/mp4";
  if (file.type !== allowedFileType) {
    createErrorMsg(document.getElementById('uploadVideoErrorMsgBox'), "The uploaded video file type must be MP4.");
    return;
  }

  // Check if file size is above 500MB
  const allowedFileSize = 500000000;
  if (file.size > allowedFileSize) {
    createErrorMsg(document.getElementById('uploadVideoErrorMsgBox'), "Uploaded file size exceeds the maximum allowed file size, which is 500MB.");
    return;
  }

  // Check if all 3 input boxes are filled
  const courseId = document.getElementById('courseIdElement');
  const uploadOrderNo = document.getElementById('upload_order_no');
  const uploadVideoTitle = document.getElementById('upload_title');
  const uploadVideoDescription = document.getElementById('upload_description');

  if (uploadOrderNo.value === "" || uploadVideoTitle.value === "" || uploadVideoDescription.value === "") {
    createErrorMsg(document.getElementById('uploadVideoErrorMsgBox'), "Please fill all the required fields to upload the video.");
    return;
  }

  const formData = new FormData();
  formData.append('file', file);
  formData.append('course_id', courseIdInput.value);
  formData.append('order_no', uploadOrderNo.value);
  formData.append('video_title', uploadVideoTitle.value);
  formData.append('video_description', uploadVideoDescription.value);

  // Define a function to show a dialog box with a success message
  function showDialogWithSuccessMessage(message) {

    var successDialog = document.getElementById('successDialog');
    var successMessage = document.getElementById('successMessage');


    successMessage.textContent = message;


    successDialog.style.display = 'block';


    setTimeout(function () {
      successDialog.style.display = 'none';

      window.location.reload();
    }, 2000);
  }

  // Use the Fetch API to send the data to the backend
  fetch("/upload", {
    method: "POST",
    body: formData
  }).then(response => {
    if (response.ok) {
      // showDialogWithSuccessMessage("Successfully uploaded the video");
      createSuccessMsg(document.getElementById('uploadVideoErrorMsgBox'), "Successfully uploaded the video");
      setTimeout(function () {
        window.location.reload();
      }, 2000);

    } else {
      createErrorMsg(document.getElementById('uploadVideoErrorMsgBox'), "Failed to upload the video. Please try again.");
    }
  });
});

//Cancel Upload

$(document).ready(function () {

  $("#cancelUploadButton").click(function () {

    $.ajax({
      type: "POST",
      url: "/cancelUpload",
      success: function (response) {

        console.log("Upload canceled: " + response);
      }
    });
  });
});

//Upload progress bar
const progressBar = document.getElementById('progressBar');
const progressValue = document.getElementById('progressValue');
const socket = new SockJS('/websocket-progress');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
  stompClient.subscribe('/topic/progress', function (message) {
    const progress = parseFloat(message.body);
    updateProgress(progress);
  });
});

function updateProgress(progress) {
  progressBar.style.width = progress + '%';
  progressValue.textContent = progress.toFixed(2) + '% uploaded';
}

// Check input data and send data to backend for Edit a video Dialog
document.addEventListener('DOMContentLoaded', function () {
  const editButtons = document.querySelectorAll('.edit_a_video');

  editButtons.forEach(function (button) {
    button.addEventListener('click', function () {
      const videoId = button.getAttribute('data-video-id');



      editVideoBtn.setAttribute('data-video-id', videoId);
    });
  });


  editVideoBtn.addEventListener('click', function () {
    removeErrorMsg(editVideoErrorMsgBox);

    // Check if all 3 input boxes are filled
    if (editOrderNo.value == "" || editVideoTitle.value == "" || editVideoDescription.value == "") {
      createErrorMsg(editVideoErrorMsgBox, "Please fill all the required fields to edit the video.");
      return;
    }

    var videoId = editVideoBtn.getAttribute('data-video-id');

    data = {

      order_no: editOrderNo.value,
      video_title: editVideoTitle.value,
      video_description: editVideoDescription.value,
      id: videoId
    };
    data = JSON.stringify(data);
    console.log(data);

    function showDialogWithSuccessMessage(message) {

      var successDialog = document.getElementById('successDialogs');
      var successMessage = document.getElementById('successMessages');


      successMessage.textContent = message;


      successDialog.style.display = 'block';


      setTimeout(function () {
        successDialog.style.display = 'none';

        window.location.reload();
      }, 2000);
    }
    fetch("/editVideo", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: data
    }).then(response => {
      if (response.ok) {
        // showDialogWithSuccessMessage("Successfully updated the video");
        createSuccessMsg(editVideoErrorMsgBox, "Successfully updated the video");
        setTimeout(function () {
          window.location.reload();
        }, 2000);
        
      } else {
        createErrorMsg(editVideoErrorMsgBox, "Failed to update the video. Please try again.");
      }
    });
  });
});


// Create error message to put in error message box
function createErrorMsg(errorMsgBox, errorMsg) {
  const uploadErrorMsg = document.createElement("div");
  uploadErrorMsg.style.fontSize = "14px";
  uploadErrorMsg.classList.add("text-danger", "text-center");
  uploadErrorMsg.textContent = errorMsg;
  errorMsgBox.appendChild(uploadErrorMsg);
  errorMsgBox.classList.add("animate__fadeIn", "border-danger");
  errorMsgBox.classList.replace("d-none", "d-flex");
  return;
}

// Create csv success message to put in csv error message box
function createSuccessMsg(successMsgBox, successMsg) {
  const uploadSuccessMsg = document.createElement("div");
  uploadSuccessMsg.style.fontSize = "14px";
  uploadSuccessMsg.classList.add("text-success", "text-center");
  uploadSuccessMsg.textContent = successMsg;
  successMsgBox.appendChild(uploadSuccessMsg);
  successMsgBox.classList.add("animate__fadeIn", "border-success");
  successMsgBox.classList.replace("d-none", "d-flex");
  return;
}

// Edit a Video Dialog start
let edit_a_videos = document.querySelectorAll('.edit_a_video');
let edit_a_video_box = document.querySelector('.edit-a-video-box');
let edit_a_video_cancel = document.querySelector('#edit_a_video_cancel');


edit_a_videos.forEach((editButton) => {
  editButton.addEventListener('click', () => {
    edit_a_video_box.classList.add('animate__fadeIn');
    edit_a_video_box.classList.remove('d-none', 'animate__fadeOut');
  });
});

edit_a_video_cancel.addEventListener('click', () => {
  edit_a_video_box.classList.remove('animate__fadeIn');
  edit_a_video_box.classList.add('animate__fadeOut');
  setTimeout(() => {
    edit_a_video_box.classList.add('d-none');
    removeErrorMsg(editVideoErrorMsgBox);
  }, 1000);
});
// Edit a Video Dialog end

// Delete a video start
document.addEventListener('DOMContentLoaded', function () {
  const deleteButtons = document.querySelectorAll('.delete_a_video');

  deleteButtons.forEach(function (button) {
    button.addEventListener('click', function () {
      const videoId = button.getAttribute('data-video-id');
      const videoTitle = button.getAttribute('data-video-title');

      Swal.fire({
        icon: 'warning',
        text: 'Are you sure to delete this video?',
        showCancelButton: true,
        confirmButtonText: 'Delete',
        confirmButtonColor: '#dc3545'
      }).then((result) => {
        if (result.isConfirmed) {

          const data = {
            id: videoId,
            title: videoTitle
          };
          // Send the data to the backend
          fetch('/delete-video', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
          })
            .then((response) => {
              if (response.ok) {
                // Swal.fire('Video deleted!', '', 'success');
                Swal.fire({
                  icon: 'success',
                  text: 'Video deleted!',
                  confirmButtonText: 'OK',
                  confirmButtonColor: '#3f877a'
                });
                window.location.reload();

              } else {
                // Swal.fire('Error deleting video', '', 'error');
                Swal.fire({
                  icon: 'error',
                  text: 'Error deleting video',
                  confirmButtonText: 'OK',
                  confirmButtonColor: '#3f877a'
                });
              }
            })
            .catch((error) => {
              // Swal.fire('Error deleting video', '', 'error');
              Swal.fire({
                icon: 'error',
                text: 'Error deleting video',
                confirmButtonText: 'OK',
                confirmButtonColor: '#3f877a'
              });

            });
        }
      });
    });
  });
});
// Delete a video end


document.getElementById("downloadstulist").addEventListener("click", function () {
  var table2excel = new Table2Excel();
  table2excel.export(document.querySelectorAll("#studentListTable"));
})