function loadUsers() {
    $("#accordion").empty();
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

function loadRoles() {
    console.log("I work");
    let url = '/admin/roles';
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

function populateUserTable(i, user) {
    console.log(user);
    var roleBorders = {"ROLE_ADMIN": "border-danger", "ROLE_DEVELOPER": "border-warning", "ROLE_USER": "border-primary", "ROLE_NEW": "border-info", "ROLE_DISABLED": "border-dark",}

    var $cardBody = $("<div/>")
    .addClass("card-body")
    .html(buildForm(user));
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
                 .addClass("card " + roleBorders[user["role"]])
                 .html($cardHeader);
    
    $card.append($collapseBody);

    $("#accordion").append($card);
    
}

function buildForm(user) {

    $versions = $("<div/>")
                    .addClass("float-right mt-2 user_versions")
                    .html(`
                    <h6> Versions </h6>
                    <input type="checkbox" name="versions" value="Official"/><span class="badge badge_version ml-2">Official</span>
                    <input type="checkbox" name="versions" value="BP 1.5"/><span class="badge badge_version ml-2">BP 1.5</span>
                    `);

    $roles = $("<div/>")
                    .addClass("float-right mt-2 user_roles")
                    .html(`
                    <h6>Roles</h6>
                    <input type="checkbox" name="roles" value="Admin"/><span class="badge badge-danger ml-2">Admin</span>
                    <input type="checkbox" name="roles" value="Developer"/><span class="badge badge-warning ml-2">Developer</span>
                    <input type="checkbox" name="roles" value="User"/><span class="badge badge-primary ml-2">User</span>
                    <input type="checkbox" name="roles" value="Disabled"/><span class="badge badge-dark ml-2">Disabled</span>
                `);

    $userDetails = $("<div/>")
                    .addClass("float-left user_details")
                    .html(`
                    <label for="username">Username</label>
                    <input type="text" name="username" class="form-control user_username" value="` + user["username"] + `"/> 
                
                    <label for="email" class="mt-3">E-mail address</label>
                    <input type="text" name="email" class="form-control user_email" value="` + user["email"] + `"/>
                    <input type="hidden" name="id" value=" `+ user["id"] +`">
                    `);

    $bottom = $("<div/>")
                .addClass("float-right w-100")
                .html("<button class=\"btn btn-warning user_update_button float-right mb-3\" type=\"submit\">Save changes</button>"
                );

    $form = $("<form/>").append($userDetails, $roles, $versions, $bottom);
    return $form;
}
