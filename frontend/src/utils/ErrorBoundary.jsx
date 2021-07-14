import React from "react";
import PropTypes from "prop-types";

class ErrorBoundary extends React.Component {
    static propTypes = {
        children: PropTypes.oneOfType([
            PropTypes.node,
            PropTypes.arrayOf(PropTypes.node)
        ]).isRequired,
        render: PropTypes.func.isRequired
    };

    state = {
        hasError: false,
        error: null,
        errorInfo: null
    };

    componentDidCatch(error, errorInfo) {
        this.setState({hasError: true, error, errorInfo});

        console.error(error.message);
    }

    render() {
        if (this.state.hasError) {
            return this.props.render(this.state.error, this.state.errorInfo);
        }
        return this.props.children;
    }
}

export default ErrorBoundary;
