function loadUsers() {
    console.log("I work");
    let url = '/admin/users';
    $.ajax({
        type: 'GET',
        contentType: 'text/plain',
        url: url,
        success: function(response) {
           console.log(response);
        },
        error: function(response) {
            console.log(response);
            //TODO
        }
    });

}