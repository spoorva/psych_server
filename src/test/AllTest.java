package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import authentication.AdminAuthenticationServletTest;
import authentication.AuthenticatingUserTest;
import autoFill.FetchCommonFieldServletTest;
import fieldValidation.CommonFieldsTest;
import fieldValidation.CommonFieldsVal;
import fieldValidation.ImageFieldsTest;
import fieldValidation.LocationFieldsTest;
import fieldValidation.QuestionCategoryFieldsValTest;
import fieldValidation.UserProfileFieldsTest;
import imageData.ImageCategoryServletTest;
import imageData.ImageFetcherTest;
import imageData.ImageUploadServletTest;
import location.LocationServletTest;
import profile.UserProfileServletTest;
import questionnaire.QuestionCategoryServlet;
import questionnaire.QuestionCategoryServletTest;
import questionnaire.QuestionServletTest;
import questionnaire.QuestionnaireTest;
import registration.Register;
import registration.RegisterTest;
import report.ReportServletTest;
import targetGroup.TargetGroupServletTest;
import training.TrainingServletTest;

@RunWith(Suite.class)
@SuiteClasses({
	AdminAuthenticationServletTest.class,
	UserProfileServletTest.class,
	UserProfileFieldsTest.class,
	FetchCommonFieldServletTest.class,
	LocationFieldsTest.class,
	LocationServletTest.class,
	TargetGroupServletTest.class,
	TrainingServletTest.class,
	QuestionCategoryFieldsValTest.class,
	QuestionCategoryServletTest.class,
	QuestionServletTest.class,
	ImageFieldsTest.class,
	ImageCategoryServletTest.class,
	ImageUploadServletTest.class,
	RegisterTest.class,
	AuthenticatingUserTest.class,
	CommonFieldsTest.class,
	QuestionnaireTest.class,
	ImageFetcherTest.class,
	QuestionnaireTest.class,
	ReportServletTest.class
	
})
public class AllTest {

}
