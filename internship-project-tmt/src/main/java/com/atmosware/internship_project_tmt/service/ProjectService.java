package com.atmosware.internship_project_tmt.service;

import com.atmosware.internship_project_tmt.entity.Project;
import com.atmosware.internship_project_tmt.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;


    public Project createProject(Project project) {
        return projectRepository.save(project);
    }


    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }


    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }


    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}