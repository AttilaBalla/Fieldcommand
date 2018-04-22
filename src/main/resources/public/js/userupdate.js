function convertToSimpleJson(formArray) {
    var returnArray = {};
    var versions = [];
    for (var i = 0; i < formArray.length; i++){
        if(formArray[i]['name'] != 'versions') {
            returnArray[formArray[i]['name']] = formArray[i]['value'];
        } else {
            versions.push(formArray[i]['value']);
        }
    }
    returnArray["versions"] = versions;
    return returnArray;
    }

function prepUpdateForm() {
    $(".user_edit").on("submit", function(event) {
        event.preventDefault();

        let data = convertToSimpleJson($(this).serializeArray());
        console.log(data);
        $.ajax({
            type: 'POST',
            contentType: 'application/JSON',
            url: "/admin/updateuser",
            data: JSON.stringify(data),
            success: function(response) {
                console.log(response);
                loadUsers();
            },
            error: function(response) {
                console.log(response);
                //TODO
            }
        });
    });
}