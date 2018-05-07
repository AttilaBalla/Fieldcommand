'use strict';

const React = require('react');
const ReactDOM = require('react-dom');

class SwrNet extends React.Component {
	content;
	

	constructor(props) {
		super(props);
		this.state = {};
	}

	componentDidMount() {
		$.ajax({
			type: 'GET',
			url: "/swrstatus",
			success: function(response) {
				console.log(JSON.parse(response)["nameValuePairs"]);
				this.setState(JSON.parse(response)["nameValuePairs"]);
    		}
		});
	}

	render() {

		if(this.state["successful"]) {

			const count = this.state["count"] > 0 ? (
				<span className="text-success">{this.state["count"]}</span>
			) : (
				<span>0</span>
			);

			this.content =
			<div>
				<img className="mr-2" src="/img/swrnetlogo_on.png" width="80" height="20"/>
				{count}
			</div>;

		} else {
			// noinspection CheckTagEmptyBody
            this.content =
			<div>
				<img className="mr-2" src="/img/swrnetlogo_off.png" width="80" height="20"/>
				<i className="fa fa-exclamation-triangle ml-1 mt-1 text-danger" aria-hidden="true"></i>
			</div>;
		}

		return (this.content);
	}
}

ReactDOM.render(<SwrNet/>, document.getElementById("swr_net"));