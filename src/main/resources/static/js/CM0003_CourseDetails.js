$(function(){
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
});