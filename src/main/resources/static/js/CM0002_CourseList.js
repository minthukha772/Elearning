$(function () {
  constructFilter(courseList);
  renderCourseList(courseList);
  $(".filterBtn").click(function (e) {
    e.preventDefault();
    filterAction(courseList);
  });
});

const filterAction = (courseList) => {
  const inputFilters = structureInputFilters();
  console.log(inputFilters);
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
  console.log("rendering");
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
      console.log(item, count);
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
  console.log(courseList);
  $("#course").pagination({
    dataSource: courseList,
    pageSize: 5,
    showNavigator: true,
    position:"top",
    className: 'paginationjs-theme-blue',
    formatNavigator: '<span style="color: #f00"><%= currentPage %></span> of <%= totalPage %> pages, <%= totalNumber %> entries',
    callback: function (data, pagination) {
      // template method of yourself
      $("#courseList").hide();
      $("#courseList").empty();
      data.forEach((e)=>{
        const template = `
        <div class="card col-12 col-xl-6 col-xxl-4">
          <div class="card-body">
            <div >
              <h2>${e.courseName}</h2>
              <a href="/guest/explore/teacher/${e.teacherId}">${e.teacherName}</a>
              <h6 class="mt-1">${e.category} &gt;${e.level}</h6>
              <p>${e.aboutCourse}</p>
              <h4>Date</h4>
              <i>${e.startDate.substring(0,10)} - ${e.endDate.substring(0,10)}</i>
              <h4 class="mt-2">${e.fees} MMK</h4>

          
            </div>
            <a href="#" class="btn btn-primary">Detail</a>
          </div>
          
        </div>
        `;
        $(template).appendTo($("#courseList"));

        $("#courseList").fadeIn('slow');
      
      });
     
    },
  });
};
// const renderCourseList = (courseList) => {
//   console.log("rendering");
//   $("#courseList").hide();
//   $("#courseList").empty();
//   courseList.forEach((e, index) => {
//     console.log(e);

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
