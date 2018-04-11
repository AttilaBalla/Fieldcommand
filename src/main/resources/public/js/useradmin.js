function loadUsers() {
    console.log("I work");
    let url = '/admin/users';
    $.ajax({
        type: 'GET',
        contentType: 'text/plain',
        url: url,
        success: function(response) {
           jQuery.each(JSON.parse(response), function(i, user) {
            populateUserTable(i, user);
          });

        },
        error: function(response) {
            console.log(response);
            //TODO
        }
    });
}

function populateUserTable(i, user) {

    var $cardBody = $("<div/>")
    .addClass("card-body")
    .html("stuff will go here");
    var $collapseBody = $("<div/>")
                    .addClass("collapse")
                    .attr({
                        "id": "collapse" + i,
                        "data-parent": "#accordion",
                      })
                    .html($cardBody);

    var $cardHeader = $("<div/>")
                .addClass("card-header collapsed")   
                .attr({
                    "data-toggle": "collapse",
                    "data-target": "#collapse" + i,
                    "aria-expanded": "true",
                    "aria-controls": "collapse" + i
                  })
                .html("<h6 class=\"mb-0\">"+ user["username"] + "</h6>");

    var $card = $("<div/>")   
                 .addClass("card border-danger")
                 .html($cardHeader);
    
    $card.append($collapseBody);

    $("#accordion").append($card);
    
}