package com.atmosware.internship_project_tmt.service;

import com.atmosware.internship_project_tmt.dto.request.CreateProjectRequest;
import com.atmosware.internship_project_tmt.dto.response.ProjectResponse;
import com.atmosware.internship_project_tmt.entity.Project;
import com.atmosware.internship_project_tmt.exception.ProjectNotFoundException;
import com.atmosware.internship_project_tmt.mapper.ProjectMapper;
import com.atmosware.internship_project_tmt.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;


    public ProjectResponse createProject(CreateProjectRequest request) {
        Project project = projectMapper.mapToEntity(request);
        Project savedProject = projectRepository.save(project);
        return projectMapper.mapToResponse(savedProject);
    }


    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectMapper::mapToResponse)
                .collect(Collectors.toList());
    }


    public ProjectResponse getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Proje bulunamadı!"));
        return projectMapper.mapToResponse(project);
    }


    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}