package com.vasic.project_board.controller;

import com.vasic.project_board.domain.Project;
import com.vasic.project_board.domain.ProjectTask;
import com.vasic.project_board.service.ProjectTaskService;
import com.vasic.project_board.service.ValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private ValidationErrorService errorService;

    private static final Logger LOGGER = Logger.getLogger(ProjectTaskController.class.getName());

    @PostMapping("/create/{backlog_id}")
    public ResponseEntity<?> addProjectTaskToBacklog(@Valid @RequestBody ProjectTask projectTask,
                                                     BindingResult result, @PathVariable String backlog_id) {

        ResponseEntity<?> errorMap = errorService.validateFields(result);
        if(errorMap != null) { return errorMap; }

        ProjectTask newProjectTask = projectTaskService.saveOrUpdateProjectTask(backlog_id, projectTask);

        LOGGER.log(Level.INFO, "Project task - created: summary = " + newProjectTask.getSummary());

        return new ResponseEntity<ProjectTask>(newProjectTask, HttpStatus.CREATED);

    }

    @GetMapping("/all/{backlog_id}") // project_identifier == backlog_identifier
    public Iterable<ProjectTask> getProjectBacklog(@PathVariable String backlog_id) {

        return projectTaskService.findBacklogByIdentifier(backlog_id);
    }

    @GetMapping("/get/{backlog_id}/{pt_id}") // project_identifier == backlog_identifier
    public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id) {

        ProjectTask projectTask = projectTaskService.findProjectTaskByProjectSequence(backlog_id, pt_id);
        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
    }

    @PatchMapping("/update/{backlog_id}/{pt_id}")
    public  ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult bindingResult,
                                                @PathVariable String backlog_id, @PathVariable String pt_id) {

        ResponseEntity<?> errorMap = errorService.validateFields(bindingResult);
        if(errorMap != null) { return errorMap; }

        ProjectTask updatedProjectTask = projectTaskService.updateByProjectSequence(projectTask, backlog_id, pt_id);

        return new ResponseEntity<ProjectTask>(updatedProjectTask, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{backlog_id}/{pt_id}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id) {

        projectTaskService.deleteProjectTaskByProjectSequence(backlog_id, pt_id);
        return new ResponseEntity<String>("Project task deleted", HttpStatus.OK);
    }
}
