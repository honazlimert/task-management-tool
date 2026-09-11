package com.atmosware.internship_project_tmt.service;

import com.atmosware.internship_project_tmt.dto.request.CreateProjectRequest;
import com.atmosware.internship_project_tmt.dto.response.ProjectResponse;
import com.atmosware.internship_project_tmt.entity.Project;
import com.atmosware.internship_project_tmt.exception.ProjectNotFoundException;
import com.atmosware.internship_project_tmt.mapper.ProjectMapper;
import com.atmosware.internship_project_tmt.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void createProject_Success() {
        CreateProjectRequest request = new CreateProjectRequest();
        request.setName("Yeni Proje");
        request.setDescription("Açıklama");

        Project project = new Project();
        project.setName("Yeni Proje");
        project.setDescription("Açıklama");

        Project savedProject = new Project();
        savedProject.setId(1L);
        savedProject.setName("Yeni Proje");
        savedProject.setDescription("Açıklama");

        ProjectResponse response = new ProjectResponse();
        response.setId(1L);
        response.setName("Yeni Proje");

        when(projectMapper.mapToEntity(request)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(savedProject);
        when(projectMapper.mapToResponse(savedProject)).thenReturn(response);

        ProjectResponse result = projectService.createProject(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Yeni Proje", result.getName());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void getAllProjects_Success() {
        Project project1 = new Project();
        project1.setId(1L);
        project1.setName("Proje 1");

        Project project2 = new Project();
        project2.setId(2L);
        project2.setName("Proje 2");

        ProjectResponse response1 = new ProjectResponse();
        response1.setId(1L);
        response1.setName("Proje 1");

        ProjectResponse response2 = new ProjectResponse();
        response2.setId(2L);
        response2.setName("Proje 2");

        when(projectRepository.findAll()).thenReturn(List.of(project1, project2));
        when(projectMapper.mapToResponse(project1)).thenReturn(response1);
        when(projectMapper.mapToResponse(project2)).thenReturn(response2);

        List<ProjectResponse> result = projectService.getAllProjects();

        assertEquals(2, result.size());
        assertEquals("Proje 1", result.get(0).getName());
        assertEquals("Proje 2", result.get(1).getName());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void getProjectById_Success() {
        Long projectId = 1L;

        Project project = new Project();
        project.setId(projectId);
        project.setName("Proje 1");

        ProjectResponse response = new ProjectResponse();
        response.setId(projectId);
        response.setName("Proje 1");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectMapper.mapToResponse(project)).thenReturn(response);

        ProjectResponse result = projectService.getProjectById(projectId);

        assertNotNull(result);
        assertEquals(projectId, result.getId());
        assertEquals("Proje 1", result.getName());
    }

    @Test
    void getProjectById_NotFound_ThrowsException() {
        Long projectId = 99L;

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.getProjectById(projectId));
        verify(projectMapper, never()).mapToResponse(any(Project.class));
    }

    @Test
    void deleteProject_Success() {
        Long projectId = 1L;

        when(projectRepository.existsById(projectId)).thenReturn(true);

        projectService.deleteProject(projectId);

        verify(projectRepository, times(1)).existsById(projectId);
        verify(projectRepository, times(1)).deleteById(projectId);
    }

    @Test
    void deleteProject_NotFound_ThrowsException() {
        Long projectId = 99L;

        when(projectRepository.existsById(projectId)).thenReturn(false);

        assertThrows(ProjectNotFoundException.class, () -> projectService.deleteProject(projectId));
        verify(projectRepository, never()).deleteById(anyLong());
    }
}
