class SwrNet extends React.Component {
	content;
	
	constructor(props) {
		super(props);
		this.state = {};
	}

	componentDidMount() {

        fetch("/swrstatus")
      	.then(res => res.json())
      	.then(
        (result) => {
		  	this.setState(result);
		  	console.log(this.state);
        },
        (error) => {
          	this.setState({
            isLoaded: true,
            error
          	});
        });
	}

	render() {
		if(this.state["successful"]) {

			const count = this.state["count"] > 0 ? (
				<span className="text-success">{this.state["count"]}</span>
			) : (
				<span>{this.state["count"]}<i className="fa fa-user ml-1 mt-1" aria-hidden="true"></i></span>
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