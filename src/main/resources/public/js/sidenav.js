window.onload = function() {

    $("#admin_general").addClass('highlighted');

    function loadPage(pageType) {
        let url = '/admin?subPage=' + pageType;
        $.ajax({
            type: 'GET',
            contentType: 'text/plain',
            url: url,
            success: function(response) {
                $('.admin_container').empty();
                $('.admin_container').append(response);

                if(pageType === "useradmin") {
                    prepInviteForm();
                }
            },
            error: function(response) {
                console.log(response);
                //TODO
            }
        });
    }

    $("#admin_general").click(function(event){
        $(".sidebar_button").removeClass('highlighted');
        $("#admin_general").addClass('highlighted');
        loadPage('general');
    });

    $("#admin_gamereport").click(function(event){
        $(".sidebar_button").removeClass('highlighted');
        $("#admin_gamereport").addClass('highlighted');
        loadPage('gamereport');
    });

    $("#admin_release").click(function(event){
        $(".sidebar_button").removeClass('highlighted');
        $("#admin_release").addClass('highlighted');
        loadPage('release');
    });

    $("#admin_user").click(function(event){
        $(".sidebar_button").removeClass('highlighted');
        $("#admin_user").addClass('highlighted');
        loadPage('useradmin');
        loadUsers();
    });
};