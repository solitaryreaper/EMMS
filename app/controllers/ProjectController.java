package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.project;

public class ProjectController extends Controller {
	
    public static Result index() {
        return ok(project.render());
    }
}
