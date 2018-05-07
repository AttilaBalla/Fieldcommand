'use strict';

const React = require('react');
const ReactDOM = require('react-dom');

class SwrNet extends React.Component {

	constructor(props) {
		super(props);
		this.state = {};
	}

	componentDidMount() {
		$.ajax({
			type: 'GET',
			url: "/swrstatus",
			success: function(response) {
				this.setState(JSON.parse(response)["nameValuePairs"]);
    		}
		});
	}

	render() {

		if(this.state["successful"]) {

			const count = (this.state["count"] > 0) ? (
				<span class="text-success">{this.state["count"]}</span>
			) : (
				<span>0</span>
			);

			const content =
			<div>
				<img class="mr-2" src="/img/swrnetlogo_on.png" width="80" height="20"/>
				{count}
			</div>;

		} else {
			const content =
			<div>
				<img class="mr-2" src="/img/swrnetlogo_off.png" width="80" height="20"/>
				<i class="fa fa-exclamation-triangle ml-1 mt-1" aria-hidden="true"></i>
			</div>;
		}

		return (
			<span class="swr_net">{content}</span>
		)
	}
}

ReactDOM.render(<SwrNet/>, document.getElementsByClassName('swr_net'));