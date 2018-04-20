$(".activation_form").on("submit", function(event) {

    console.log(window.location["origin"]);

    event.preventDefault();
    $alert = $(".action_alert").removeClass("alert-danger alert-success").empty();
    $button = $(".btn-primary").addClass("disabled");
    password = $("#password").val();
    passwordAgain = $("#password_again").val();
    key = decodeURIComponent(window.location.search.substring(1).split("=")[1]);

    if(password.length < 6) {
        $alert.addClass("alert-danger").html("Password has to be at least 6 characters long!");
        $button.removeClass("disabled");
        return;
    }

    if(password === passwordAgain) {

        data = {"password": password, "key": key};

        $.ajax({
            type: 'POST',
            contentType: 'application/JSON',
            url: "/activate",
            data: JSON.stringify(data),
            success: function(response) {
                if(JSON.parse(response)["success"] === true){
                    $alert.addClass("alert-success").html("Your account has been activated! Redirecting...");
                    setTimeout(function() {
                        window.location.replace(window.location["origin"] + "/login");
                    }, 2000);
                } else {
                    $alert.addClass("alert-danger").html(JSON.parse(response)["information"]);
                    $button.removeClass("disabled");
                }
            },
            error: function(response) {
                console.log(response);
                //TODO
            }
        });

    } else {
        $alert.addClass("alert-danger").html("Passwords do not match!");
        $button.removeClass("disabled");
    }
 });
