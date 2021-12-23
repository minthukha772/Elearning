/* $(() => {
    $('#forgetPassword').click((event) =>{
        
        event.preventDefault();
        event.stopPropagation();
        console.log(uri + "/reset_password");
        const email = $('#email').val();
        $.ajax({
            type: "post",
            url: uri + "/reset_password",
            data: {email},

        }).done((data) => {
            console.log(data);
            window.location.href = uri+"?message="+data;
        }).fail((data) => {
            console.log("fail");
            window.location.href = uri+"?error="+data.responseText;
        });
       
        
        
    })
}) */