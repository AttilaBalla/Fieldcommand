function loadUsers() {
    $("#accordion").empty();
    let url = '/admin/users';
    $.ajax({
        type: 'GET',
        url: url,
        success: function(response) {
            jQuery.each(JSON.parse(response), function(i, user) {
            populateUserTable(i, user);
          });
          loadRoles();
        },
        error: function(response) {
            console.log(response);
            //TODO
        }
    });
}

function loadRoles() {
    let url = '/admin/userRoles';
    $.ajax({
        type: 'GET',
        url: url,
        success: function(response) {
            jQuery.each(JSON.parse(response), function(id, roleSet) {
            setUserRoles(id, roleSet);
            });
        },
        error: function(response) {
            console.log(response);
            //TODO
        }
    });
}

function populateUserTable(i, user) {
    
    var nameColors = {"ROLE_ADMIN": "text-danger", "ROLE_DEVELOPER": "text-warning", "ROLE_USER": "text-primary", "ROLE_NEW": "text-info", "ROLE_DISABLED": "text-dark"}

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
                .html("<h6 class=\"mb-0 " + nameColors[user["role"]] + "\">" + user["username"] + "</h6>");

    var $card = $("<div/>")
                .addClass("card")
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
                    .attr({
                        "data-userId": user["id"]
                      })
                    .html(`
                    <h6>Roles</h6>
                    <input type="radio" name="roles" value="ROLE_ADMIN"/><span class="badge badge-danger ml-2">Admin</span>
                    <input type="radio" name="roles" value="ROLE_DEVELOPER"/><span class="badge badge-warning ml-2">Developer</span>
                    <input type="radio" name="roles" value="ROLE_USER"/><span class="badge badge-primary ml-2">User</span>
                    <input type="radio" name="roles" value="ROLE_DISABLED"/><span class="badge badge-dark ml-2">Disabled</span>
                `);

    $userDetails = $("<div/>")
                    .addClass("float-left user_details")
                    .attr({
                        "data-userId": user["id"]
                      })
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

    $form = $("<form/>").addClass("user_edit").append($userDetails, $roles, $versions, $bottom);
    return $form;
}

function setUserRoles(userId, roleSet) {

    $roles = $(".user_roles")
                .filter(function(){
                    return this.dataset.userid == userId;
                });

    $roles.children('input').each(function() {
            $input = $(this);
            if(roleSet.includes($input.val())) {
                $input.attr({
                "checked":"checked"
                });
            }
    });
}
