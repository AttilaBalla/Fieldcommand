
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
        let data = convertToSimpleJson($(this).serializeArray());
        console.log(data);

        $.ajax({
            type: 'POST',
            contentType: 'application/JSON',
            url: "/admin/invite",
            data: JSON.stringify(data),
            success: function(response) {
                console.log(response);
            },
            error: function(response) {
                console.log(response);
                //TODO
            }
        });
    });
}