import React, { Component } from "react";
import { Link } from "react-router-dom";
import ProjecItem from "./Project/ProjecItem";
import CreateProjectButton from "./Project/CreateProjectButton";
import PropTypes from 'prop-types';
import {gelAllProjects} from '../actions/projectActions';
import { connect } from "react-redux";
import Loader from "./Layout/Loader";


class Dashboard extends Component {

  
  constructor() {
    super();
    this.state = {
      isLoaded: false
    }
  }

  async componentDidMount() {
    await this.props.gelAllProjects();
    this.setState({isLoaded:true});

  }


  render() {

    const {projects} = this.props.project;

    return (
      <div className="projects">
        <div className="container">
          <div className="row">
            <div className="col-md-12">
              <h1 className="display-4 text-center">Projects</h1>
              <br />
              <CreateProjectButton></CreateProjectButton>
              <br />
              <hr />

              {
                
                /* Project Item Component */
                
                //If data is loaded show "No projects found" or found projects. Otherwise show loader animation
                this.state.isLoaded ? ( projects.length < 1 ? <div className="alert alert-info text-center" role="alert">No projects found.</div> :
                  projects.map(project=> (
                    <ProjecItem key={project.id} project={project}></ProjecItem>
                  ))) : (<Loader></Loader>)
                
              
                /* End of Project Item Component */
              }

            </div>
          </div>
        </div>
      </div>
    );
  }
}

Dashboard.propTypes = {
  gelAllProjects: PropTypes.func.isRequired,
  project:  PropTypes.object.isRequired
}

const mapStateToProps = state => ({
  project: state.project // from index.js - combine reducers
})

export default connect(mapStateToProps, { gelAllProjects })(Dashboard);
