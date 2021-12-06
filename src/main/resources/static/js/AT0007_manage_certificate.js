$(function () {

    console.log(uri);

    $(".delete").click((e) => {
  
        const name = $(e).data('name');
        console.log(name);
        console.log("delete button clicked");
        $.ajax({
            type: "delete",
            url: uri,
            data: {id:1},
            dataType: "dataType",
            success: function (response) {
                console.log(response);
            }
        });

    })


});
