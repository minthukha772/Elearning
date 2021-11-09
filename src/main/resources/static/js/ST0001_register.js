$(document).ready(function () {
  fillPassword();


 

  $(".back").click(function (event) {
    //TODO prevent external link

    event.preventDefault();

    if(isInternal() && history.length > 0){
      history.back(1);
    }
    
  });
});

function fillPassword() {
  //Initialize previous password value to the password field
  $("input[type='password']").each(function () {
    const oldPassword = $(this).attr("data-value");
    if (oldPassword && oldPassword.length > 0) {
      console.log("Password re enter");
      $(this).attr("value", oldPassword);
    }
  });

}

function isInternal(){
  /* Get Previous Page */
  const prevPage = document.referrer;
  /* Get current Page Host */
  const regExp = new RegExp(location.host);
  return regExp.test(prevPage);
  
}




/* $(document).on("change", ".uploadProfileInput", function () {
  var triggerInput = this;
  var currentImg = $(this).closest(".pic-holder").find(".pic").attr("src");
  var holder = $(this).closest(".pic-holder");
  var wrapper = $(this).closest(".profile-pic-wrapper");
  $(wrapper).find('[role="alert"]').remove();
  var files = !!this.files ? this.files : [];
  if (!files.length || !window.FileReader) {
    return;
  }
  if (/^image/.test(files[0].type)) {
    // only image file
    var reader = new FileReader(); // instance of the FileReader
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
      // If upload successful

    };
  } else {
    $(wrapper).append(
      '<div class="alert alert-danger d-inline-block p-2 small" role="alert">Please choose the valid image.</div>'
    );
    setTimeout(() => {
      $(wrapper).find('role="alert"').remove();
    }, 3000);
  }
});
 */