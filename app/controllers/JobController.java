package controllers;

import java.util.List;

import com.walmartlabs.productgenome.rulegenerator.EMMSWorkflowDriver;
import com.walmartlabs.productgenome.rulegenerator.config.JobMetadata;
import com.walmartlabs.productgenome.rulegenerator.model.analysis.DatasetEvaluationSummary;
import com.walmartlabs.productgenome.rulegenerator.model.analysis.JobEvaluationSummary;
import com.walmartlabs.productgenome.rulegenerator.model.analysis.RuleEvaluationSummary;
import com.walmartlabs.productgenome.rulegenerator.model.rule.Rule;

import play.Logger;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.job;
import views.html.results;
import static play.data.Form.form;

public class JobController extends Controller {
	
    public static Result index() {
        return ok(job.render());
    }
    
    public static Result submitJob()
    {
    	DynamicForm form = form().bindFromRequest();
    	Logger.info("PARAMETERS : " + form.data().toString());
    	
    	JobMetadata jobMeta = new JobMetadata();
    	jobMeta.setName(form.get("job_name"));
    	jobMeta.setAttributesToEvaluate(form.get("attributes_to_evaluate"));
    	jobMeta.setLearner(form.get("learning_algo"));
    	
    	MultipartFormData body = request().body().asMultipartFormData();
		FilePart srcFilePath = body.getFile("source_data_file_path");
		FilePart tgtFilePath = body.getFile("target_data_file_path");
		FilePart goldFilePath = body.getFile("gold_data_file_path");
		
    	jobMeta.setSourceFile(srcFilePath.getFile().getAbsolutePath());
    	jobMeta.setTargetFile(tgtFilePath.getFile().getAbsolutePath());
    	jobMeta.setGoldFile(goldFilePath.getFile().getAbsolutePath());

    	jobMeta.setDesiredPrecision(form.get("precision"));
    	jobMeta.setDesiredCoverage(form.get("coverage"));
    	
    	EMMSWorkflowDriver jobDriver = new EMMSWorkflowDriver();
    	JobEvaluationSummary matchRunResults = jobDriver.runEntityMatching(jobMeta);
    	DatasetEvaluationSummary trainPhaseSummary = matchRunResults.getTrainPhaseSumary();
    	DatasetEvaluationSummary tunePhaseSummary = matchRunResults.getTunePhaseSummary();
    	DatasetEvaluationSummary testPhaseSummary = matchRunResults.getTestPhaseSummary();
    	
    	List<RuleEvaluationSummary> topNRules = testPhaseSummary.getRankedRuleSummaries(testPhaseSummary.getRuleSummary());
    	
    	return ok(results.render(trainPhaseSummary, tunePhaseSummary, testPhaseSummary, topNRules));
    }
}
