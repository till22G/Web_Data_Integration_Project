package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.ListElementEvaluationLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.ListEvaluationRuleEqual;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.StringEvaluationRuleLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.GenericAttributeFuser;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.Species;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.SpeciesXMLFormatter;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.SpeciesXMLReader;
import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEngine;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEvaluator;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionStrategy;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.Voting;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.list.Union;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleDataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleHashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroupFactory;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import org.slf4j.Logger;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

public class CompleteDatasetMain {
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

	public static void main(String[] args) throws Exception {
		// Load the Data into FusibleDataSet
		logger.info("*\tLoading datasets\t*");

		FusibleDataSet<Species, Attribute> fusedDs = new FusibleHashedDataSet<>();
		new SpeciesXMLReader().loadFromXML(new File("data/output/fused_final.xml"), "/Species/Species", fusedDs);
		fusedDs.printDataSetDensityReport();

		FusibleDataSet<Species, Attribute> ds1 = new FusibleHashedDataSet<>();
		new SpeciesXMLReader().loadFromXML(new File("data/input/endangered_species.xml"), "/Animals_And_Plants/Species", ds1);
		ds1.printDataSetDensityReport();

		FusibleDataSet<Species, Attribute> ds2 = new FusibleHashedDataSet<>();
		new SpeciesXMLReader().loadFromXML(new File("data/input/biodiversity.xml"), "/Animals_And_Plants/Species", ds2);
		ds2.printDataSetDensityReport();

		FusibleDataSet<Species, Attribute> ds3 = new FusibleHashedDataSet<>();
		new SpeciesXMLReader().loadFromXML(new File("data/input/wd_species.xml"), "/Animals_And_Plants/Species", ds3);
		ds3.printDataSetDensityReport();


		FusibleDataSet<Species, Attribute> completeDs = new FusibleHashedDataSet<>();
		new SpeciesXMLReader().initialiseDataset(completeDs);

		ds1.get().forEach(completeDs::add);
		ds2.get().forEach(completeDs::add);
		ds3.get().forEach(completeDs::add);

		for (Species species : fusedDs.get()) {
			String[] ids = species.getIdentifier().split("\\+");

			if (ids.length >= 1 && ids[0].startsWith("BIO")) {
				completeDs.removeRecord(ids[0]);
			}

			if (ids.length >= 2 && ids[1].startsWith("ES")) {
				completeDs.removeRecord(ids[1]);
			}

			String wdId = ids.length >= 2 && ids[1].startsWith("http://www.wikidata.org/entity") ? ids[1] :
					(ids.length >= 3 && ids[2].startsWith("http://www.wikidata.org/entity") ? ids[2] : null);
			if (wdId != null) {
				completeDs.removeRecord(wdId);
			}

			completeDs.add(species);
		}

		System.out.println("\n\nComplete Dataset");
		completeDs.printDataSetDensityReport();
		System.out.println(completeDs.size());
	}
}
