window.onload = function() {

    $("#admin_general").addClass('highlighted');

    function convertToSimpleJson(formArray) {

        var returnArray = {};
        for (var i = 0; i < formArray.length; i++){
          returnArray[formArray[i]['name']] = formArray[i]['value'];
        }
        return returnArray;
      }

    function loadPage(pageType) {
        let url = '/admin?subPage=' + pageType;
        $.ajax({
            type: 'GET',
            contentType: 'text/plain',
            url: url,
            data: pageType,
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
    });
};