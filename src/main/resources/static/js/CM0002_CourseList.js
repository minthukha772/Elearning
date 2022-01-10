$(function () {
  constructFilter(courseList);
  renderCourseList(courseList);
  $("#filterBtn").click(function (e) {
    e.preventDefault();
    filterAction(courseList);
  });
});

const filterAction = (courseList) => {
    
    const inputFilters = structureInputFilters();
    console.log(inputFilters);
    const filteredCourseList = courseList.filter((e) => {
        let condition = true;
        if(inputFilters['levels']){
            condition = condition && inputFilters['levels'].includes(e.level);
        }
        if(inputFilters['categories']){
            condition = condition && inputFilters['categories'].includes(e.category);
        }
        if(inputFilters['classTypes']){
            condition = condition && inputFilters['classTypes'].includes(e.classType);
        }
        return condition;
    });
    renderCourseList(filteredCourseList);
    
    
};

const structureInputFilters = () =>{
    const checkedBoxes = $(".filterContainer").find("input:checked");
    const filterList = {};
    checkedBoxes.each(function (index, element) {
        const parent = $(element).data("parent")
        const value = element.name;
        if(!filterList[parent]){
            // Create Array
            filterList[parent] = [value];
        }
        else{
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

const renderCourseList = (courseList) =>{
    console.log("rendering");
    $("#courseList").empty();
    courseList.forEach((e,index) => {
        console.log(e);
        $("#courseList").append(`<b>${index}</b><p>${JSON.stringify(e)}</p><br>`);
    });
};