$(function () {
  constructFilter(courseList);
  renderCourseList(courseList);
  $(".filterBtn").click(function (e) {
    e.preventDefault();
    filterAction(courseList);
  });
  $(".clearBtn").click(function (e) {
    e.preventDefault();
    clearAction(courseList);
  });
  LowestFeeInput = $("#lowestFee");
  HighestFeeInput = $("#highestFee");
  warningFee = $("#warningFee");

  document.getElementById('searchButton').addEventListener('click', function (event) {
    // Prevent the default form submission
    event.preventDefault();
    warningFee.css("display", "none");
    // Get the form and input field values
    const form = document.getElementById('searchForm');
    const courseName = form.elements['courseName'].value;
    const startDate = form.elements['startDate'].value;
    const endDate = form.elements['endDate'].value;
    const lowestFee = form.elements['lowestFee'].value;
    const highestFee = form.elements['highestFee'].value;

    // Perform validation checks here
    // if (courseName.trim() === '') {
    //   alert('Please enter a course name.');
    //   return;
    // }
    if (lowestFee > highestFee) {
      warningFee.css("display", "block");
      return;
    }
    // Validate other fields as needed...

    // If all validations pass, submit the form
    form.submit();
  });

  // LowestFeeInput.on("input", function () {
  //   // To prevent Highest value from being less than Lowest value
  //   // Update Highest value every time the lower value is higher them itself.
  //   lowestValue = parseInt($(LowestFeeInput).val());
  //   highestValue = parseInt($(HighestFeeInput).val());
  //   console.log(lowestValue, highestValue);
  //   if (lowestValue > highestValue) {
  //     console.log(lowestValue, ">", highestValue);
  //     $(HighestFeeInput).val(lowestValue);
  //   }
  // });
  // HighestFeeInput.on("input", function () {
  //   // To prevent Lowest value from being more than Lowest value
  //   // Update Lowest value every time the highest value is higher them itself.
  //   lowestValue = parseInt($(LowestFeeInput).val());
  //   highestValue = parseInt($(HighestFeeInput).val());
  //   if (highestValue < lowestValue) {
  //     $(LowestFeeInput).val(highestValue);
  //   }
  // });
});

const clearAction = (courseList) => {
  clearCheckInputs();
  renderCourseList(courseList);
};

const filterAction = (courseList) => {
  const inputFilters = structureInputFilters();

  const filteredCourseList = courseList.filter((e) => {
    let condition = true;
    if (inputFilters["levels"]) {
      condition = condition && inputFilters["levels"].includes(e.level);
    }
    if (inputFilters["categories"]) {
      condition = condition && inputFilters["categories"].includes(e.category);
    }
    if (inputFilters["classTypes"]) {
      condition = condition && inputFilters["classTypes"].includes(e.classType);
    }
    return condition;
  });
  renderCourseList(filteredCourseList);
};

const clearCheckInputs = () => {
  $(".filterContainer").find("input:checked").prop("checked", false);
};

const structureInputFilters = () => {
  const checkedBoxes = $(".filterContainer").find("input:checked");
  const filterList = {};
  checkedBoxes.each(function (index, element) {
    const parent = $(element).data("parent");
    const value = element.name;
    if (!filterList[parent]) {
      // Create Array
      filterList[parent] = [value];
    } else {
      filterList[parent].push(value);
    }
  });
  return filterList;
};

const constructFilter = (courseList) => {
  const filterItems = getFilterItems(courseList);
  renderFilterItems(filterItems);
};

const getFilterItems = (courseList) => {
  const levels = {};
  const categories = {};
  const classTypes = {};

  courseList.forEach((element) => {
    levels[element.level] = levels[element.level]
      ? levels[element.level] + 1
      : 1;
    categories[element.category] = categories[element.category]
      ? categories[element.category] + 1
      : 1;
    classTypes[element.classType] = classTypes[element.classType]
      ? classTypes[element.classType] + 1
      : 1;
  });

  return { levels, categories, "class Types": classTypes };
};

const renderFilterItems = (filterItems) => {
  const keys = Object.keys(filterItems);
  keys.forEach((key, index) => {
    const filterContainer = $(".filterContainer");
    const id = `${key.replace(/\s/g, "")}`;
    // Render Title
    filterContainer.append(
      `<li class="list-group-item" id="${id}"><b class="text-capitalize">${key}</b></li>`
    );
    // Render CheckBox
    const checkBoxContainer = filterContainer.find(`#${id}`);
    const filterKeys = Object.keys(filterItems[key]);
    filterKeys.forEach((item, j) => {
      const count = filterItems[key][item];

      //const checkBoxId = `${id}-item`;
      checkBoxContainer.append(
        `
        <div class="mt-1">
            <input type="checkbox" name="${item}" data-parent="${id}" id="${item}" value="${item} checked">
            <label for="${item}">${item} (${count})</label>
        </div>
        `
      );
    });
  });
};

const renderCourseList = (courseList) => {
  $("#zero-result-msg").hide();
  // If list empty display text
  if ((courseList == null, courseList.length == 0)) {
    $("#zero-result-msg").show();
    $("#courseList").hide();
    $("#courseList").empty();
    $("#course").pagination({
      dataSource: courseList,
      pageSize: 24,
      showNavigator: true,
      position: "top",
      className: "paginationjs-theme-blue",
      formatNavigator: '<span style="color: #f00">1</span> of 1 page, 0 entry'
    });
    return;
  }

  $("#course").pagination({
    dataSource: courseList,
    pageSize: 24,
    showNavigator: true,
    position: "top",
    className: "paginationjs-theme-blue",
    formatNavigator:
      '<span style="color: #f00"><%= currentPage %></span> of <%= totalPage %> pages, <%= totalNumber %> entries',
    callback: function (data, pagination) {
      // template method of yourself
      $("#courseList").hide();
      $("#courseList").empty();
      const currentDate = new Date();
      data.forEach((e) => {
        const options = {
          timeZone: 'Asia/Tokyo',
          year: 'numeric',
          month: 'numeric',
          day: 'numeric',
        };
        const startDate = new Date(e.startDate).toLocaleDateString('en-US', options);
        const endDate = new Date(e.endDate).toLocaleDateString('en-US', options);
        var dates;
        if (e.classType == "VIDEO") {
          dates = ` <h4>&nbsp</h4>
          <i> &nbsp</i>`;
        } else {
          dates = ` <h4>Date</h4>
        <i>${startDate} - ${endDate}</i>`;
        }

        const registeredDate = new Date(e.registeredDate);
        const differenceInMilliseconds = currentDate - registeredDate;
        const differenceInDays = differenceInMilliseconds / (1000 * 60 * 60 * 24);

        const template = `
        <div class="col-sm-12 col-md-12 col-xl-12 my-3">
          <div class="d-flex flex-column flex-md-column flex-sm-column flex-lg-column flex-xl-row">
          <span>
            <img width="200" height="200" src="${e.coursePhoto.url}" class="detail__img" alt="${e.coursePhoto.name}">
          </span>
            <div>
              <div class="tags-div">
                ${differenceInDays <= 7 ? '<div class="new-tag shadow-sm">New</div>' : ''}
              </div>
              <h2>${e.courseName}</h2>
              <a href="/guest/explore/teacher/${e.teacherId}">${e.teacherName}</a>
              <h6 class="mt-1">${e.category} &gt;${e.level}</h6>
              
               <span>${dates}</span>
              <h4 class="mt-2">${e.fees ? e.fees + 'MMK' : 'FREE'}</h4>
              <a href="/guest/course-detail/${e.courseId}" class="btn btn-primary">See Detail</a>
            </div>
          </div>
          
        </div>
        <hr>
        `;
        $(template).appendTo($("#courseList"));

        $("#courseList").fadeIn("slow");
      });
    },
  });
};
// const renderCourseList = (courseList) => {
//
//   $("#courseList").hide();
//   $("#courseList").empty();
//   courseList.forEach((e, index) => {
//

//     const template =`
//         <div class="card" >
//           <div class="card-body">
//             <div >
//               <h2>${e.courseName}</h2>
//               <h5>${e.teacherName}</h5>
//               <h6>${e.category} &gt;${e.level}</h6>
//               <p>${e.aboutCourse}</p>
//               <h4>Prerequisites</h4>
//               <p>${e.prerequisite}</p>
//               <h4>${e.fees}</h4>

//             </div>
//             <a href="#" class="btn btn-primary">Detail</a>
//           </div>

//         </div>
//         `
//     $(template).appendTo($("#courseList"));

//     $("#courseList").show('slow');
//   });
// };