package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import java.io.File;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Species;
import org.slf4j.Logger;
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
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.SpeciesXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.MaximumBipartiteMatchingAlgorithm;
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

public class IR_using_machine_learning_bio_es {
	
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

	private static final Logger logger = WinterLogManager.activateLogger("info");
	
    public static void main( String[] args ) throws Exception
    {
		// loading data
		logger.info("*\tLoading datasets\t*");
		HashedDataSet<Species, Attribute> dataBiodiversity = new HashedDataSet<>();
		new SpeciesXMLReader().loadFromXML(new File("data/input/biodiversity.xml"), "/Animals_And_Plants/Species", dataBiodiversity);
		HashedDataSet<Species, Attribute> dataEndangeredSpecies = new HashedDataSet<>();
		new SpeciesXMLReader().loadFromXML(new File("data/input/endangered_species.xml"), "/Animals_And_Plants/Species", dataEndangeredSpecies);
		
		// load the training set
		MatchingGoldStandard gsTraining = new MatchingGoldStandard();
		gsTraining.loadFromCSVFile(new File("data/goldstandard/gs_biodiversity_endangeredSpecies_train.csv"));
		
		// generate the feature data set
//		matchingRule.exportTrainingData(dataBiodiversity, dataEndangeredSpecies, gsTraining, new File("output/features.csv"));

		// create a matching rule
		String options[] = new String[1];
				
		//Logistic Regression
//		options[0] = "-S";
//		String modelType = "SimpleLogistic";
		
		//LogitBoost
//		String modelType = "LogitBoost";
//		options[0] = "-Q";
		
		//Tree model
//		String modelType = "J48";
//		options[0] = "-U";

		//DecisionTable
//		String modelType = "DecisionTable";
//		options[0] = "-R";
		
		//Random Forest
		String modelType = "RandomForest";
		options[0] = "-attribute-importance";
//		options[0] = "-print";
		
		WekaMatchingRule<Species, Attribute> matchingRule = new WekaMatchingRule<>(0.93, modelType, options);
		matchingRule.activateDebugReport("data/output/debugResultsMatchingRule_bio_es.csv", 1000, gsTraining);
		
		// add comparators
		matchingRule.addComparator(new StringAttributeComparatorJaccard<>(Species::getScientificName, "scientificName"));
		matchingRule.addComparator(new StringAttributeComparatorLevenshtein<>(Species::getScientificName, "scientificName"));
		
		matchingRule.addComparator(new StringAttributeComparatorEqual<>(Species::getCategory, "Category"));
		
		matchingRule.addComparator(new StringListAttributeComparatorLevenshtein<>(Species::getCommonNames, "commonNames"));
//		matchingRule.addComparator(new StringListAttributeComparatorJaccard<>(Species::getCommonNames, "commonNames"));
//		matchingRule.addComparator(new StringListAttributeAsWholeComparatorLevenshtein<>(Species::getCommonNames, "commonNames"));
		matchingRule.addComparator(new StringListAttributeAsWholeComparatorJaccard<>(Species::getCommonNames, "commonNames"));
		
		matchingRule.addComparator(new StringListAttributeAsWholeComparatorJaccard<>(Species::getRegionNames, "regionNames"));


		

		// train the matching rule's model
		logger.info("*\tLearning matching rule\t*");
		RuleLearner<Species, Attribute> learner = new RuleLearner<>();
		learner.learnMatchingRule(dataBiodiversity, dataEndangeredSpecies, null, matchingRule, gsTraining);
		logger.info(String.format("Matching rule is:\n%s", matchingRule.getModelDescription()));

		// create a blocker (blocking strategy)
		//Block by Scientific Names
		StandardRecordBlocker<Species, Attribute> blocker = new StandardRecordBlocker<Species, Attribute>(new SpeciesBlockingKeyByScientificNameGenerator());
		//Block by Categories
		//StandardRecordBlocker<Species, Attribute> blocker = new StandardRecordBlocker<Species, Attribute>(new SpeciesBlockingKeyByCategoryGenerator());
		//Block by Categories and certain families
		//StandardRecordBlocker<Species, Attribute> blocker = new StandardRecordBlocker<Species, Attribute>(new SpeciesBlockingKeyByCategoryAndScientificNameGenerator());
		//Block by Categories and certain Families and Scientific Name
		//StandardRecordBlocker<Species, Attribute> blocker = new StandardRecordBlocker<Species, Attribute>(new SpeciesBlockingKeyCascadedGenerator());

		blocker.collectBlockSizeData("data/output/debugResultsBlocking_bio_es.csv", 100);

		// Initialize Matching Engine
		MatchingEngine<Species, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		logger.info("*\tRunning identity resolution\t*");
		Processable<Correspondence<Species, Attribute>> correspondences = engine.runIdentityResolution(
				dataBiodiversity, dataEndangeredSpecies, null, matchingRule, blocker);
		
		// Create a top-1 global matching
		correspondences = engine.getTopKInstanceCorrespondences(correspondences, 1, 0.0);
		
		// Alternative: Create a maximum-weight, bipartite matching
//		MaximumBipartiteMatchingAlgorithm<Species,Attribute> maxWeight = new MaximumBipartiteMatchingAlgorithm<>(correspondences);
//		maxWeight.run();
//		correspondences = maxWeight.getResult();

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/biodiversity_endangeredSpecies_correspondences.csv"), correspondences);

		// load the gold standard (test set)
		logger.info("*\tLoading gold standard\t*");
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File(
				"data/goldstandard/gs_biodiversity_endangeredSpecies_test.csv"));

		// evaluate your result
		logger.info("*\tEvaluating result\t*");
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
