package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.*;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.*;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.*;
import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEngine;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEvaluator;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionStrategy;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.list.Union;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleDataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleHashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroupFactory;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import org.slf4j.Logger;

import javax.swing.*;

public class DataFusion_Main_Species {
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

        FusibleDataSet<Species, Attribute> ds1 = new FusibleHashedDataSet<>();
        new SpeciesXMLReader().loadFromXML(new File("data/input/endangered_species.xml"), "/Animals_And_Plants/Species", ds1);
        ds1.printDataSetDensityReport();

        FusibleDataSet<Species, Attribute> ds2 = new FusibleHashedDataSet<>();
        new SpeciesXMLReader().loadFromXML(new File("data/input/biodiversity.xml"), "/Animals_And_Plants/Species", ds2);
        ds2.printDataSetDensityReport();

        FusibleDataSet<Species, Attribute> ds3 = new FusibleHashedDataSet<>();
        new SpeciesXMLReader().loadFromXML(new File("data/input/wd_species.xml"), "/Animals_And_Plants/Species", ds3);
        ds3.printDataSetDensityReport();

        // Maintain Provenance
        // Scores (e.g. from rating)
        ds1.setScore(1.0);
        ds2.setScore(1.0);
        ds3.setScore(1.0);

        // Date (e.g. last update)
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd")
                .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter(Locale.ENGLISH);

        ds1.setDate(LocalDateTime.parse("2012-01-01", formatter));
        ds2.setDate(LocalDateTime.parse("2010-01-01", formatter));
        ds3.setDate(LocalDateTime.parse("2008-01-01", formatter));

        // load correspondences
        CorrespondenceSet<Species, Attribute> correspondences = new CorrespondenceSet<>();
        correspondences.loadCorrespondences(new File("data/correspondences/biodiversity_endangeredSpecies_correspondences.csv"), ds2, ds1);
        correspondences.loadCorrespondences(new File("data/correspondences/wikidata_biodiversity_correspondences.csv"), ds2, ds3);

        // write group size distribution
        correspondences.printGroupSizeDistribution();
        /*

        // load the gold standard
        logger.info("*\tEvaluating results\t*");
        DataSet<Species, Attribute> gs = new FusibleHashedDataSet<>();
        new SpeciesXMLReader().loadFromXML(new File("data/goldstandard/gold.xml"), "/Animals_And_Plants/Species", gs);

        for (Species m : gs.get()) {
            logger.info(String.format("gs: %s", m.getIdentifier()));
        }
        */


        // define the fusion strategy
        DataFusionStrategy<Species, Attribute> strategy = new DataFusionStrategy<>(new SpeciesXMLReader());
        // write debug results to file
        //strategy.activateDebugReport("data/output/debugResultsDatafusion.csv", -1, gs);

        // add attribute fusers
        strategy.addAttributeFuser(Species.COMMONNAMES, new GenericAttributeFuser<>(new Union<>(), Species::getCommonNames, Species::setCommonNames, Species.COMMONNAMES), new ListEvaluationRule(Species::getCommonNames));

        // create the fusion engine
        DataFusionEngine<Species, Attribute> engine = new DataFusionEngine<>(strategy);

        // print consistency report
        engine.printClusterConsistencyReport(correspondences, null);

        // print record groups sorted by consistency
        engine.writeRecordGroupsByConsistency(new File("data/output/recordGroupConsistencies.csv"), correspondences, null);

        // run the fusion
        logger.info("*\tRunning data fusion\t*");
        FusibleDataSet<Species, Attribute> fusedDataSet = engine.run(correspondences, null);

        // write the result
        new SpeciesXMLFormatter().writeXML(new File("data/output/fused.xml"), fusedDataSet);

        // evaluate
        DataFusionEvaluator<Species, Attribute> evaluator = new DataFusionEvaluator<>(strategy, new RecordGroupFactory<Species, Attribute>());

        //double accuracy = evaluator.evaluate(fusedDataSet, gs, null);

        //logger.info(String.format("*\tAccuracy: %.2f", accuracy));
    }
}
