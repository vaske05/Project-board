import axios from "axios";
import { GET_ERRORS, GET_BACKLOG, DELETE_PROJECT_TASK, GET_PROJECT_TASK } from "./types";

//Endpoint urls
const createTaskPath = "/api/backlog/create";
const getTasksPath = "/api/backlog/all";
const deleteTaskPath = "/api/backlog/delete";
const getTaskPath = "/api/backlog/get";

/*
* Http Post request to add new project task
*/
export const addProjectTask = (backlog_id, project_task, history) => async dispatch => {

    try {
        await axios.post(createTaskPath + `/${backlog_id}`, project_task);
        history.push(`/projectBoard/${backlog_id}`); //Redirect to project board page
        dispatch({
            type: GET_ERRORS,
            payload: {} // clear errors
        });
        
    } catch (error) {
        dispatch({
            type: GET_ERRORS,
            payload: error.response.data // load errors
        });
    }
};

/*
* Http Get request to get all tasks
*/
export const getBacklog = (backlog_id) => async dispatch => {
    const res = await axios.get(getTasksPath + `/${backlog_id}`);
    dispatch({
        type: GET_BACKLOG,
        payload: res.data
    });
};

/*
* Http Delete request to delete appropriate task.
*/
export const deleteProjectTask = pt_id => async dispatch => {
    if( (window.confirm(`Are you sure to delete task ${pt_id}`)) ) {
        
        await axios.delete(deleteTaskPath + `/${pt_id}`);
        dispatch({
            type: DELETE_PROJECT_TASK,
            payload: pt_id
        });
    }
};

/*
* Http Get request to grab appropriate task.
*/
export const getProjectTask = (pt_id, history) => async dispatch => {
    try {
        const res = await axios.get(getTaskPath + `/${pt_id}`);
        dispatch({
            type: GET_PROJECT_TASK,
            payload: res.data
        });
    } catch (error) {
            history.push("/");
    }
}

