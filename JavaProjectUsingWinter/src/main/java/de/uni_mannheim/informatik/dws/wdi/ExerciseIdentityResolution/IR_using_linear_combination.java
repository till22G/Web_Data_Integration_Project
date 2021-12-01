package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.StringAttributeComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.StringAttributeComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Species;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.SpeciesXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import org.slf4j.Logger;

import java.io.File;

public class IR_using_linear_combination 
{
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

		// load the gold standard (test set)
		logger.info("*\tLoading gold standard\t*");
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File(
				"data/goldstandard/gs_academy_awards_2_actors_test.csv"));

		// create a matching rule
		LinearCombinationMatchingRule<Species, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
				0.7);
		matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsTest);
		
//		// add comparators
//		matchingRule.addComparator(new MovieDateComparator2Years(), 0.5);
		matchingRule.addComparator(new StringAttributeComparatorJaccard<>(Species::getScientificName, "scientificName"), 0.5);
		matchingRule.addComparator(new StringAttributeComparatorLevenshtein<>(Species::getScientificName, "scientificName"), 0.5);
//
//		// create a blocker (blocking strategy)
//		StandardRecordBlocker<Species, Attribute> blocker = new StandardRecordBlocker<Species, Attribute>(new MovieBlockingKeyByTitleGenerator());
////		NoBlocker<Movie, Attribute> blocker = new NoBlocker<>();
////		SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByTitleGenerator(), 1);
//		blocker.setMeasureBlockSizes(true);
//		//Write debug results to file:
//		blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);
//
//		// Initialize Matching Engine
//		MatchingEngine<Species, Attribute> engine = new MatchingEngine<>();
//
//		// Execute the matching
//		logger.info("*\tRunning identity resolution\t*");
//		Processable<Correspondence<Species, Attribute>> correspondences = engine.runIdentityResolution(
//				dataAcademyAwards, dataActors, null, matchingRule,
//				blocker);
//
//		// Create a top-1 global matching
////		  correspondences = engine.getTopKInstanceCorrespondences(correspondences, 1, 0.0);
//
////		 Alternative: Create a maximum-weight, bipartite matching
////		 MaximumBipartiteMatchingAlgorithm<Movie,Attribute> maxWeight = new MaximumBipartiteMatchingAlgorithm<>(correspondences);
////		 maxWeight.run();
////		 correspondences = maxWeight.getResult();
//
//		// write the correspondences to the output file
//		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/academy_awards_2_actors_correspondences.csv"), correspondences);
//
//
//
//		logger.info("*\tEvaluating result\t*");
//		// evaluate your result
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
