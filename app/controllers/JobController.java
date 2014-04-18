package controllers;

import java.util.List;

import models.Constants;

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
    	jobMeta.setDatasetName(form.get(Constants.PARAM_DATASET_NAME));
    	jobMeta.setDatasetName(form.get(Constants.PARAM_DATASET_NAME));
    	jobMeta.setAttributesToEvaluate(form.get(Constants.PARAM_ATTRIBUTES_TO_EVALUATE));
    	jobMeta.setLearner(form.get(Constants.PARAM_LEARNER));
    	
    	boolean isItemPairFormat = form.get(Constants.PARAM_DATA_FORMAT).equals(Constants.ITEM_PAIR_FILE_FORMAT);
    	
    	MultipartFormData body = request().body().asMultipartFormData();
    	if(!isItemPairFormat) {
    		FilePart srcFilePath = body.getFile(Constants.PARAM_SOURCE_FILE_PATH);
    		FilePart tgtFilePath = body.getFile(Constants.PARAM_TARGET_FILE_PATH);

        	jobMeta.setSourceFile(srcFilePath.getFile().getAbsolutePath());
        	jobMeta.setTargetFile(tgtFilePath.getFile().getAbsolutePath());
    	}
    	else {
    		FilePart itemPairFilePath = body.getFile(Constants.PARAM_ITEM_PAIR_FILE_PATH);
    		jobMeta.setItemPairFile(itemPairFilePath.getFile().getAbsolutePath());
    	}
    	
		FilePart goldFilePath = body.getFile(Constants.PARAM_GOLD_FILE_PATH);
    	jobMeta.setGoldFile(goldFilePath.getFile().getAbsolutePath());

    	jobMeta.setDesiredPrecision(form.get(Constants.PARAM_PRECISION));
    	jobMeta.setDesiredCoverage(form.get(Constants.PARAM_COVERAGE));
    	
    	EMMSWorkflowDriver jobDriver = new EMMSWorkflowDriver();
    	JobEvaluationSummary matchRunResults = jobDriver.runEntityMatching(jobMeta);
    	DatasetEvaluationSummary trainPhaseSummary = matchRunResults.getTrainPhaseSumary();
    	DatasetEvaluationSummary tunePhaseSummary = matchRunResults.getTunePhaseSummary();
    	DatasetEvaluationSummary testPhaseSummary = matchRunResults.getTestPhaseSummary();
    	
    	List<RuleEvaluationSummary> topNRules = testPhaseSummary.getRankedRuleSummaries(testPhaseSummary.getRuleSummary());
    	
    	return ok(results.render(trainPhaseSummary, tunePhaseSummary, testPhaseSummary, topNRules));
    }
}
