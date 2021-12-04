package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.SpeciesBlockingKeyByCategoryAndScientificNameGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.SpeciesBlockingKeyByCategoryGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.SpeciesBlockingKeyByScientificNameGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.SpeciesBlockingKeyCascadedGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.StringAttributeComparatorEqual;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.StringAttributeComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.StringAttributeComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.StringListAttributeAsWholeComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.StringListAttributeAsWholeComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.StringListAttributeComparatorEqual;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.StringListAttributeComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.StringListAttributeComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Species;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.SpeciesXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Record;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import org.slf4j.Logger;

import java.io.File;

public class IR_using_linear_combination_bio_es
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
		HashedDataSet<Species, Attribute> dataBiodiversity = new HashedDataSet<>();
		new SpeciesXMLReader().loadFromXML(new File("data/input/biodiversity.xml"), "/Animals_And_Plants/Species", dataBiodiversity);
		HashedDataSet<Species, Attribute> dataEndangeredSpecies = new HashedDataSet<>();
		new SpeciesXMLReader().loadFromXML(new File("data/input/endangered_species.xml"), "/Animals_And_Plants/Species", dataEndangeredSpecies);
		

		
		// load the gold standard (test set)
		logger.info("*\tLoading gold standard\t*");
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File(
				"data/goldstandard/gs_biodiversity_endangeredSpecies_full.csv"));
		
		// create a matching rule
		LinearCombinationMatchingRule<Species, Attribute> matchingRule = new LinearCombinationMatchingRule<>(0.8);
		matchingRule.activateDebugReport("data/output/debugResultsMatchingRule_bio_es.csv", 1000, gsTest);
		
		// add comparators
		//matchingRule.addComparator(new StringAttributeComparatorEqual<>(Species::getScientificName, "scientificName"), 1);
		//matchingRule.addComparator(new StringAttributeComparatorJaccard<>(Species::getScientificName, "scientificName"), 0.25);
		matchingRule.addComparator(new StringAttributeComparatorLevenshtein<>(Species::getScientificName, "scientificName"), 1);
//		//matchingRule.addComparator(new StringListAttributeAsWholeComparatorJaccard<>(Species::getCommonNames, "commonNames"), 0.5);
//		matchingRule.addComparator(new StringListAttributeComparatorLevenshtein<>(Species::getCommonNames, "commonNames"), 0.5);
//		//matchingRule.addComparator(new StringListAttributeAsWholeComparatorJaccard<>(Species::getOrders, "orders"), 0.125);
		//matchingRule.addComparator(new StringListAttributeAsWholeComparatorJaccard<>(Species::getFamilies, "families"), 0.125);
		//matchingRule.addComparator(new StringListAttributeComparatorEqual<>(Species::getStates, "states"), 0.125);
		//matchingRule.addComparator(matchingRule, 0)
		


		
		
		
		// create a blocker (blocking strategy)
		//Block by Scientific Names
		StandardRecordBlocker<Species, Attribute> blocker = new StandardRecordBlocker<Species, Attribute>(new SpeciesBlockingKeyByScientificNameGenerator());
		//Block by Categories
		//StandardRecordBlocker<Species, Attribute> blocker = new StandardRecordBlocker<Species, Attribute>(new SpeciesBlockingKeyByCategoryGenerator());
		//Block by Categories and certain families
		//StandardRecordBlocker<Species, Attribute> blocker = new StandardRecordBlocker<Species, Attribute>(new SpeciesBlockingKeyByCategoryAndScientificNameGenerator());
		//Block by Categories and certain Families and Scientific Name
		//StandardRecordBlocker<Species, Attribute> blocker = new StandardRecordBlocker<Species, Attribute>(new SpeciesBlockingKeyCascadedGenerator());
		
		
		
		
		// no blocker
		//NoBlocker<Species, Attribute> blocker = new NoBlocker<>();
		
		//SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByTitleGenerator(), 1);
		blocker.setMeasureBlockSizes(true);
		//Write debug results to file:
		blocker.collectBlockSizeData("data/output/debugResultsBlocking_bio_es.csv", 100);

		
		
		
		
		// Initialize Matching Engine
		MatchingEngine<Species, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		logger.info("*\tRunning identity resolution\t*");
		Processable<Correspondence<Species, Attribute>> correspondences = engine.runIdentityResolution(
				dataEndangeredSpecies, dataBiodiversity, null, matchingRule, blocker);

		// Create a top-1 global matching
		correspondences = engine.getTopKInstanceCorrespondences(correspondences, 1, 0.0);
//
////		 Alternative: Create a maximum-weight, bipartite matching
////		 MaximumBipartiteMatchingAlgorithm<Movie,Attribute> maxWeight = new MaximumBipartiteMatchingAlgorithm<>(correspondences);
////		 maxWeight.run();
////		 correspondences = maxWeight.getResult();

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/biodiversity_endangeredSpecies_correspondences.csv"), correspondences);

		
		
		logger.info("*\tEvaluating result\t*");
		// evaluate your result
		MatchingEvaluator<Species, Attribute> evaluator = new MatchingEvaluator<Species, Attribute>();
		Performance perfTest = evaluator.evaluateMatching(correspondences, gsTest);

		// print the evaluation result
		logger.info("Biodiversity <-> Endangered Species");
		logger.info(String.format(
				"Precision: %.4f",perfTest.getPrecision()));
		logger.info(String.format(
				"Recall: %.4f",	perfTest.getRecall()));
		logger.info(String.format(
				"F1: %.4f",perfTest.getF1()));
				
				
    }
}
