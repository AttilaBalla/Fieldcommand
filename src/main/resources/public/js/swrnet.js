$.ajax({
type: 'GET',
url: "/swrstatus",
success: function(response) {
    let status = JSON.parse(response)["nameValuePairs"];
    $swr_net = $(".swr_net").removeClass("text-success text-danger").empty();
    if(status["successful"] == false) {
        $swr_net.addClass("text-danger");
        $swr_net.append(
            $("<img/>")
            .addClass("mr-2")
            .attr({
                "src": "/img/swrnetlogo_off.png",
                "width":"80",
                "height":"20"
            })
        );
        $swr_net.append(`<i class="fa fa-exclamation-triangle ml-1 mt-1" aria-hidden="true"></i>`);
    } else {
        if(status["count"] > 0) {
            $swr_net.addClass("text-success");
        }
        $swr_net.append(
            $("<img/>")
            .addClass("mr-2")
            .attr({
                "src": "/img/swrnetlogo_on.png",
                "width":"80",
                "height":"20"
            })
        );
        $swr_net.append(
            `<span>` + status["count"] +`</span>
            <i class="fa fa-user ml-1 mt-1" aria-hidden="true"></i>`);
    }
},
error: function(response) {
    console.log(response);
    //TODO
}
});
