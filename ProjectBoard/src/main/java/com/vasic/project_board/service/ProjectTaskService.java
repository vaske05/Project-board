package com.vasic.project_board.service;

import com.vasic.project_board.domain.Backlog;
import com.vasic.project_board.domain.ProjectTask;
import com.vasic.project_board.exceptions.ProjectNotFoundException;
import com.vasic.project_board.repository.BacklogRepository;
import com.vasic.project_board.repository.ProjectRepository;
import com.vasic.project_board.repository.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProjectTaskService {

    @Autowired
    private ProjectTaskRepository projectTaskRepository;
    @Autowired

    private BacklogRepository backlogRepository;
    @Autowired

    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask saveProjectTask(String backlog_id, ProjectTask projectTask, String username) { //projectIdentifier == backlog_id

        /*try {*/
            //Exceptions: project not found

            //PTs to be added to a specific project
            Backlog backlog = projectService.findProjectByIdentifier(backlog_id, username).getBacklog();
            //Set the BL to PT
            projectTask.setBacklog(backlog);

            // We want our project sequence to be like this: IDPRO-1, IDPRO-2
            Integer backlogSequence = backlog.getPTSequence();
            // Update the BL sequence
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);

            // Add sequence to Project Task
            projectTask.setProjectSequence(backlog_id + "-" + backlogSequence);
            projectTask.setProjectIdentifier(backlog_id);

            // INITIAL priority when priority is NULL
            if(projectTask.getPriority() == null || projectTask.getPriority() == 0) {
                projectTask.setPriority(3);
            }

            // INITIAL status when status is NULL
            if( projectTask.getStatus() == "" || projectTask.getStatus() == null) {
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);

       /* } catch (Exception e) {
            throw new ProjectNotFoundException("Project not found!");
        }*/

    }

    public Iterable<ProjectTask> findAll() {
        return projectTaskRepository.findAll();
    }

    public ProjectTask findById(Long id) {
        return projectTaskRepository.getById(id);
    }

    public void delete(Long id) {
        ProjectTask projectTask = findById(id);
        projectTaskRepository.delete(projectTask);
    }

    public Iterable<ProjectTask> findBacklogByIdentifier(String backlog_id, String username) {
        projectService.findProjectByIdentifier(backlog_id, username);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
    }

    public ProjectTask findProjectTaskByProjectSequence(String backlog_id, String pt_id, String username) {

        // make sure we are searching on an existing backlog
        projectService.findProjectByIdentifier(backlog_id, username);

        // make sure that our task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if(projectTask == null) {
            throw new ProjectNotFoundException("Project Task : '" + pt_id + "' not found");
        }

        // make sure that the backlog/project ID in the path corresponds to the right project
        if(!projectTask.getProjectIdentifier().equals(backlog_id)) {
            throw new ProjectNotFoundException("Project Task : '" + pt_id + "' does not exist in project: '" + backlog_id + "'");
        }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedProjectTask, String backlog_id, String pt_id, String username) {

        ProjectTask projectTask = findProjectTaskByProjectSequence(backlog_id, pt_id, username);
        projectTask = updatedProjectTask;
        return projectTaskRepository.save(projectTask);
    }

    public void deleteProjectTaskByProjectSequence(String backlog_id, String pt_id, String username) {

        ProjectTask projectTask = findProjectTaskByProjectSequence(backlog_id, pt_id, username);
        projectTaskRepository.delete(projectTask);
    }
}
