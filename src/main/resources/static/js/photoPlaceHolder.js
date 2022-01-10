$(function () {

  const photoSubmitBtn = $("#photoSubmit");
  photoSubmitBtn.hide();

  if (isEditable) {
    $(document).on("change", ".uploadProfileInput", function () {
      
      let triggerInput = this;
      let currentImg = $(this).closest(".pic-holder").find(".pic").attr("src");
      let holder = $(this).closest(".pic-holder");
      let wrapper = $(this).closest(".profile-pic-wrapper");
      $(wrapper).find('[role="alert"]').remove();
      let files = !!this.files ? this.files : [];
      if (!files.length || !window.FileReader) {
        return;
      }
      photoSubmitBtn.show();
      if (/^image/.test(files[0].type)) {
        // only image file
        let reader = new FileReader(); // instance of the FileReader
        reader.readAsDataURL(files[0]); // read the local file

        reader.onloadend = function () {
          $(holder).addClass("uploadInProgress");
          $(holder).find(".pic").attr("src", this.result);
          $(holder).append(
            '<div class="upload-loader"><div class="spinner-border text-primary" role="status"><span class="sr-only">Loading...</span></div></div>'
          );

          // Dummy timeout; call API or AJAX below

          $(holder).removeClass("uploadInProgress");
          $(holder).find(".upload-loader").remove();
          $(document).on("change", ".uploadProfileInput", function () {
            let triggerInput = this;
            let currentImg = $(this).closest(".pic-holder").find(".pic").attr("src");
            let holder = $(this).closest(".pic-holder");
            let wrapper = $(this).closest(".profile-pic-wrapper");
            $(wrapper).find('[role="alert"]').remove();
            let files = !!this.files ? this.files : [];
            if (!files.length || !window.FileReader) {
              photoSubmitBtn.hide();
              return;
            }
            if (/^image/.test(files[0].type)) {
              // only image file
              let reader = new FileReader(); // instance of the FileReader
              reader.readAsDataURL(files[0]);
              photoSubmitBtn.show();// read the local file


            }
          });
          // If upload successful

        };
      }
    });
  }

});


