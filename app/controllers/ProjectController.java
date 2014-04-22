package controllers;

import java.util.List;

import models.Project;
import models.service.ProjectService;

import com.google.common.collect.Lists;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.project_dashboard;
import views.html.project_setup;
import views.html.job_setup;

public class ProjectController extends Controller {

	private static ProjectService projectService = new ProjectService();
	
    public static Result index() {
    	List<Project> projects = projectService.getProjects();
        return ok(project_dashboard.render(projects));
    }
    
    public static Result createProject()
    {
    	return ok(project_setup.render());
    }
    
    public static Result saveProject()
    {
    	String projectName = "";
    	return ok(job_setup.render(projectName));
    }
}
