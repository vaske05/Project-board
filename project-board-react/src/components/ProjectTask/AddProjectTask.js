import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import PropTypes from "prop-types";
import { connect } from "react-redux";
import classnames from "classnames";
import { addProjectTask } from "../../actions/projectTaskActions";

class AddProjectTask extends Component {
    constructor() {
        super()
        this.state = {
            summary: "",
            acceptanceCriteria: "",
            status: "",
            errors: {}
        };
        this.onChange = this.onChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }

    componentWillReceiveProps(nextProps) {
        if(nextProps.errors) {
            this.setState({ errors: nextProps.errors });
        }
    }

    onChange(e) {
        this.setState({ [e.target.name]: e.target.value });
    }

    //Form submit
    onSubmit(e) {
        e.preventDefault()
        const newProjectTask = {
            summary: this.state.summary,
            acceptanceCriteria: this.state.acceptanceCriteria,
            status: this.state.status
        };
        // console.log(newProjectTask);
        this.props.addProjectTask(newProjectTask, this.props.history);

    }

    render() {
        const { errors } = this.state;
        return (
            <div className="addProjectTask">
                <div className="container">
                    <div className="row">
                        <div className="col-md-8">
                            <Link to="/projectBoard" className="btn btn-success">
                                <i className="fas fa-arrow-left"></i>
                                <i className=""> Back to Board</i>
                            </Link>
                        </div>
                        <div className="col-md-8 m-auto">
                            <h4 className="display-4 text-center">Add Project Task</h4>
                            <form className="form" onSubmit={this.onSubmit}>
                                <div className="form-group">
                                    <input
                                        type="text"
                                        className={classnames("form-control form-control-lg", {
                                            "is-invalid": errors.summary
                                        })}
                                        name="summary"
                                        value={this.state.summary}
                                        onChange={this.onChange}
                                        placeholder="Project Task summary" />
                                        { errors.summary &&
                                            <div className="invalid-feedback">{errors.summary}</div>
                                        }
                                        
                                </div>
                                <div className="form-group">
                                    <textarea
                                        className="form-control form-control-lg"
                                        placeholder="Acceptance Criteria"
                                        name="acceptanceCriteria"
                                        value={this.state.acceptanceCriteria}
                                        onChange={this.onChange}
                                    ></textarea>
                                </div>
                                <div className="form-group">
                                    <select className="form-control form-control-lg" name="status" value={this.state.status}
                                        onChange={this.onChange} >
                                        <option value="">Select Status</option>
                                        <option value="TO_DO">TO DO</option>
                                        <option value="IN_PROGRESS">IN PROGRESS</option>
                                        <option value="DONE">DONE</option>
                                    </select>
                                </div>
                                <input type="submit" className="btn btn-success btn-block mt-4" />
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

AddProjectTask.propTypes = {
    addProjectTask: PropTypes.func.isRequired,
    errors: PropTypes.object.isRequired 
};

const mapStateToProps = state => ({
    errors: state.errors
});

//Connect React component to a Redux store.
export default connect(mapStateToProps, { addProjectTask }) (AddProjectTask); 
