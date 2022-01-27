$(document).ready(function () {
  console.log("Doc on service");

  //   Action for on Form Submit
  onSubmit();
  //   Action for when Day check boxes are clicked
  onDayCheckBoxClick();
  //   Set Required Fileds
  setRequiredField();
  //   Set Disabled Fileds
  setDisabledField();

  onClassTypeCheckBoxClicked();

  // $("#btnReg").click(function (e) {
  //     e.preventDefault();
  //     e.stopPropagation();
  //     $(form).addClass("was-validated");

  // });
});

function onClassTypeCheckBoxClicked() {
  if ($("#liveCheck").is(":checked")) {
    $("#iflive").show();
    $("#ifvideo").hide();
  } else {
    $("#iflive").hide();
    $("#ifvideo").show();
  }

  setRequiredField();
}

const onDayCheckBoxClick = () => {
  for (let i = 0; i < 7; i++) {
    $("#day" + i).change(function (e) {
      e.preventDefault();
      // Disable if it is not checked
      isDisable = !this.checked;
      $("#startTime" + i).prop("disabled", isDisable);
      $("#endTime" + i).prop("disabled", isDisable);
      isRequired = this.checked;
      $("#startTime" + i).prop("required", isRequired);
      $("#endTime" + i).prop("required", isRequired);
    });
  }
};

const setRequiredField = () => {
  fields = ["#maxStu", "#startDate", "#endDate"];
  const isRequired = $("#liveCheck").is(":checked");
  fields.forEach((e) => {
    $(e).prop("required", isRequired);
  });
};

const setDisabledField = () => {
  for (let i = 0; i < 7; i++) {
    const isChecked = $("#day" + i).is(":checked");
    isDisable = !isChecked;

    $("#startTime" + i).prop("disabled", isDisable);
    $("#endTime" + i).prop("disabled", isDisable);
  }
};

const onSubmit = () => {
  const form = $("form");

  $("#btnReg").click((event) => {
    
    $(form).addClass("was-validated");
    let isFormValid = checkDate();
    isFormValid= checkTime() && isFormValid;
    if (!(form[0].checkValidity() && isFormValid)) {
      event.preventDefault();
      event.stopPropagation();
    }
  });
};

const checkDate = () => {
  const startDate = new Date($("#startDate").val());
  const endDate = new Date($("#endDate").val());
  if (startDate > endDate) {
    const errorMessage = "The end date should be later";
    $("#startDate")[0].setCustomValidity(errorMessage);
    $("#endDate")[0].setCustomValidity(errorMessage);
    $(".date-feedback").html(errorMessage);
    return false;
  } else {
    $("#startDate")[0].setCustomValidity("");
    $("#endDate")[0].setCustomValidity("");
    $(".date-feedback").html("This field must be filled");
  }
  return true;
};

const checkTime = () => {
  let isValid = true;
  for (let i = 0; i < 7; i++) {
    const isChecked = $("#day" + i).is(":checked");
    if (isChecked) {
      const startTimeInput = $("#startTime" + i);
      const endTimeInput = $("#endTime" + i);
      const startTime = minFromMidnight(startTimeInput.val());
      const endTime = minFromMidnight(endTimeInput.val());
      if(startTime < 0|| endTime <0){
        // If startTime or endTime is null;
        isValid = false;
        continue;
      }
      if(startTime>endTime){
        const errorMessage = "The end time should be later";
        startTimeInput[0].setCustomValidity(errorMessage);
        endTimeInput[0].setCustomValidity(errorMessage);
        $(".day"+i+"-feedback").html(errorMessage);
        isValid = false;
      }else{
        startTimeInput[0].setCustomValidity("");
        endTimeInput[0].setCustomValidity("");
        $(".day"+i+"-feedback").html("This field must be filled");
      }
    }
  }
  return isValid;
};

function minFromMidnight(tm) {
  try{
    var ampm = tm.substr(-2);
    var clk = tm.substr(0, 5);
    var m = parseInt(clk.match(/\d+$/)[0], 10);
    var h = parseInt(clk.match(/^\d+/)[0], 10);
    h += ampm.match(/pm/i) ? 12 : 0;
    return h * 60 + m;
  }catch(e){
    
  }
  return -1;
  
}

// $("input").on("click", function () {
//   $("#log").html($("input:checked").val() + " is checked!");
// });

// $('input[type="checkbox"]').click(function () {
//   if (!this.checked) {
//     $(this).closest("div").nextAll("div").find("input").attr("disabled", true);
//   } else {
//     $(this).closest("div").nextAll("div").find("input").attr("disabled", false);
//   }
// });
