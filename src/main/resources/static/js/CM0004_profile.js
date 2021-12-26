$(function () {

    $("#paymentAlert").hide();

    $("#submitPayment").click((e) => {
        console.log("submit payment button clicked");
        const paymentPost = $('#paymentPost');
        const fields = paymentPost.find('select, textarea, :input:not([type=hidden])').serializeArray();
        let hasError = false;
        $.each(fields, function (i, field) {
            if (!field.value) {
                hasError = true;

            }
        });
        if (hasError) {
            $("#paymentAlert").show();
        } else {
            $('#paymentPost').submit();
        }


    });


});
