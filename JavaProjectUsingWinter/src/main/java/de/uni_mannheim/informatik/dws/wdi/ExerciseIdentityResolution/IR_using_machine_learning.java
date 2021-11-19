package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import java.io.File;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Species;
import org.slf4j.Logger;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.MovieBlockingKeyByTitleGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieDateComparator10Years;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieDateComparator2Years;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieDirectorComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieDirectorComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieDirectorComparatorLowerCaseJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieTitleComparatorEqual;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieTitleComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieTitleComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.SpeciesXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;

public class IR_using_machine_learning {
	
	/*
	 * Logging Options:
	 * 		default: 	level INFO	- console
	 * 		trace:		level TRACE     - console
	 * 		infoFile:	level INFO	- console/file
	 * 		traceFile:	level TRACE	- console/file
	 *  
	 * To set the log level to trace and write the log to winter.log and console, 
	 * activate the "traceFile" logger as follows:
	 *     private static final Logger logger = WinterLogManager.activateLogger("traceFile");
	 *
	 */

	private static final Logger logger = WinterLogManager.activateLogger("default");
	
    public static void main( String[] args ) throws Exception
    {
		// loading data
		logger.info("*\tLoading datasets\t*");
		HashedDataSet<Species, Attribute> dataFinalSchema = new HashedDataSet<>();
		new SpeciesXMLReader().loadFromXML(new File("data/input/Final_schema_XML.xml"), "/Animals_And_Plants/Species", dataFinalSchema);
		HashedDataSet<Species, Attribute> dataBiodiversity = new HashedDataSet<>();
		new SpeciesXMLReader().loadFromXML(new File("data/input/biodiversity.xml"), "/Animals_And_Plants/Species", dataBiodiversity);
		
		// load the training set
		MatchingGoldStandard gsTraining = new MatchingGoldStandard();
		gsTraining.loadFromCSVFile(new File("data/goldstandard/gs_academy_awards_2_actors_training.csv"));

		// create a matching rule
		String options[] = new String[] { "-S" };
		String modelType = "SimpleLogistic"; // use a logistic regression
		WekaMatchingRule<Species, Attribute> matchingRule = new WekaMatchingRule<>(0.7, modelType, options);
		matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsTraining);
		
//		// add comparators
//		matchingRule.addComparator(new MovieTitleComparatorEqual());
//		matchingRule.addComparator(new MovieDateComparator2Years());
//		matchingRule.addComparator(new MovieDateComparator10Years());
//		matchingRule.addComparator(new MovieDirectorComparatorJaccard());
//		matchingRule.addComparator(new MovieDirectorComparatorLevenshtein());
//		matchingRule.addComparator(new MovieDirectorComparatorLowerCaseJaccard());
//		matchingRule.addComparator(new MovieTitleComparatorLevenshtein());
//		matchingRule.addComparator(new MovieTitleComparatorJaccard());
//
//
//		// train the matching rule's model
//		logger.info("*\tLearning matching rule\t*");
//		RuleLearner<Species, Attribute> learner = new RuleLearner<>();
//		learner.learnMatchingRule(dataFinalSchema, dataBiodiversity, null, matchingRule, gsTraining);
//		logger.info(String.format("Matching rule is:\n%s", matchingRule.getModelDescription()));
//
//		// create a blocker (blocking strategy)
//		StandardRecordBlocker<Species, Attribute> blocker = new StandardRecordBlocker<Species, Attribute>(new MovieBlockingKeyByTitleGenerator());
//		//SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByYearGenerator(), 30);
//
//		blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);
//
//		// Initialize Matching Engine
//		MatchingEngine<Species, Attribute> engine = new MatchingEngine<>();
//
//		// Execute the matching
//		logger.info("*\tRunning identity resolution\t*");
//		Processable<Correspondence<Species, Attribute>> correspondences = engine.runIdentityResolution(
//				dataFinalSchema, dataBiodiversity, null, matchingRule,
//				blocker);
//
//		// write the correspondences to the output file
//		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/academy_awards_2_actors_correspondences.csv"), correspondences);
//
//		// load the gold standard (test set)
//		logger.info("*\tLoading gold standard\t*");
//		MatchingGoldStandard gsTest = new MatchingGoldStandard();
//		gsTest.loadFromCSVFile(new File(
//				"data/goldstandard/gs_academy_awards_2_actors_test.csv"));
//
//		// evaluate your result
//		logger.info("*\tEvaluating result\t*");
//		MatchingEvaluator<Species, Attribute> evaluator = new MatchingEvaluator<Species, Attribute>();
//		Performance perfTest = evaluator.evaluateMatching(correspondences,
//				gsTest);
//
//		// print the evaluation result
//		logger.info("Academy Awards <-> Actors");
//		logger.info(String.format(
//				"Precision: %.4f",perfTest.getPrecision()));
//		logger.info(String.format(
//				"Recall: %.4f",	perfTest.getRecall()));
//		logger.info(String.format(
//				"F1: %.4f",perfTest.getF1()));
    }
}
