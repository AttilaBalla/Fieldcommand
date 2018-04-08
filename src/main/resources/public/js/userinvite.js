
function convertToSimpleJson(formArray) {
    var returnArray = {};
    for (var i = 0; i < formArray.length; i++){
        returnArray[formArray[i]['name']] = formArray[i]['value'];
    }
    return returnArray;
    }

function prepInviteForm() {
    $(".userinvite_form").on("submit", function(event) {

        event.preventDefault();

        $(".invite_send_button").prop('disabled', true);

        let alert = $(".action_alert");
        alert.empty();
        alert.append("Sending, please wait...");
        alert.removeClass("alert-success alert-danger").addClass("alert-secondary");
        alert.show();

        let data = convertToSimpleJson($(this).serializeArray());

        $.ajax({
            type: 'POST',
            contentType: 'application/JSON',
            url: "/admin/invite",
            data: JSON.stringify(data),
            success: function(response) {
                alert.empty();
                if(JSON.parse(response)["success"] === true){
                    alert.append("The e-mail has been sent successfully!");
                    alert.removeClass("alert-secondary").addClass("alert-success");
                } else {
                    alert.append(JSON.parse(response)["information"]);
                    alert.removeClass("alert-secondary").addClass("alert-danger");
                }
                alert.fadeTo(5000, 5000).slideUp(500, function(){
                    alert.slideUp(500);
                     });
                $(".invite_send_button").prop('disabled', false);
            },
            error: function(response) {
                console.log(response);
                //TODO
            }
        });
    });
}