package com.atmosware.internship_project_tmt.mapper;

import com.atmosware.internship_project_tmt.dto.request.CreateProjectRequest;
import com.atmosware.internship_project_tmt.dto.response.ProjectResponse;
import com.atmosware.internship_project_tmt.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    // dto to entity
    public Project mapToEntity(CreateProjectRequest request) {
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        return project;
    }

    // entity to dto
    public ProjectResponse mapToResponse(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setCreatedDate(project.getCreatedDate());
        return response;
    }
}