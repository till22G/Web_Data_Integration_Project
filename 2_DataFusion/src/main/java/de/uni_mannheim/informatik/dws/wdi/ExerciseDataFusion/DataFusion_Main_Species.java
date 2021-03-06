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
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.meta.FavourSources;
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

        // Scores can be adjusted here
        ds1.setScore(2.0);
        ds2.setScore(2.0);
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
        correspondences.loadCorrespondences(new File("data/correspondences/biodiversity_endangeredSpecies_correspondences_final.csv"), ds2, ds1);
        correspondences.loadCorrespondences(new File("data/correspondences/wikidata_biodiversity_correspondences_final.csv"), ds2, ds3);

        // write group size distribution
        correspondences.printGroupSizeDistribution();


        // load the gold standard
        logger.info("*\tEvaluating results\t*");
        DataSet<Species, Attribute> gs = new FusibleHashedDataSet<>();
        new SpeciesXMLReader().loadFromXML(new File("data/goldstandard/goldstandard_xml.xml"), "/Animals_And_Plants/Species", gs);
        System.out.println(gs.get().size());


        for (Species m : gs.get()) {
            logger.info(String.format("gs: %s", m.getIdentifier()));
        }

        // define the fusion strategy
        DataFusionStrategy<Species, Attribute> strategy = new DataFusionStrategy<>(new SpeciesXMLReader());
        // write debug results to file
        //strategy.activateDebugReport("data/output/debugResultsDatafusion.csv", -1, gs);

        // add attribute fusers
        strategy.addAttributeFuser(Species.SCIENTIFICNAME, new GenericAttributeFuser<>(new Voting<>(), Species::getScientificName, Species::setScientificName, Species.SCIENTIFICNAME), new StringEvaluationRuleLevenshtein(Species::getScientificName));
        strategy.addAttributeFuser(Species.COMMONNAMES, new GenericAttributeFuser<>(new Union<>(), Species::getCommonNames, Species::setCommonNames, Species.COMMONNAMES), new ListElementEvaluationLevenshtein(Species::getCommonNames));
        strategy.addAttributeFuser(Species.CATEGORY, new GenericAttributeFuser<>(new Voting<>(),Species::getCategory, Species::setCategory, Species.CATEGORY), new StringEvaluationRuleLevenshtein(Species::getCategory));
        strategy.addAttributeFuser(Species.ORDERS, new GenericAttributeFuser<>(new Voting<>(), Species::getOrders, Species::setOrders, Species.ORDERS), new ListEvaluationRuleEqual(Species::getOrders));
        strategy.addAttributeFuser(Species.FAMILIES, new GenericAttributeFuser<>(new Voting<>(), Species::getFamilies, Species::setFamilies, Species.FAMILIES), new ListElementEvaluationLevenshtein(Species::getFamilies));
        strategy.addAttributeFuser(Species.STATES, new GenericAttributeFuser<>(new Union<>(), Species::getStates, Species::setStates, Species.STATES), new ListEvaluationRuleEqual(Species::getStates));
        strategy.addAttributeFuser(Species.REGIONS, new GenericAttributeFuser<>(new Union<>(), Species::getRegions, Species::setRegions, Species.REGIONS), new ListEvaluationRuleEqual(Species::getRegions));
        strategy.addAttributeFuser(Species.REGIONNAMES, new GenericAttributeFuser<>(new Union<>(), Species::getRegionNames, Species::setRegionNames, Species.REGIONNAMES), new ListElementEvaluationLevenshtein(Species::getRegionNames));
        strategy.addAttributeFuser(Species.LISTINGSTATUSES, new GenericAttributeFuser<>(new Voting<>(), Species::getListingStatuses, Species::setListingStatuses, Species.LISTINGSTATUSES), new ListEvaluationRuleEqual(Species::getListingStatuses));
        strategy.addAttributeFuser(Species.WHERELISTED, new GenericAttributeFuser<>(new Union<>(), Species::getWhereListed, Species::setWhereListed, Species.WHERELISTED), new ListElementEvaluationLevenshtein(Species::getWhereListed));
        strategy.addAttributeFuser(Species.DIFFERENTFROM, new GenericAttributeFuser<>(new Union<>(), Species::getDifferentFrom, Species::setDifferentFrom, Species.DIFFERENTFROM), new ListElementEvaluationLevenshtein(Species::getDifferentFrom));

        // create the fusion engine
        DataFusionEngine<Species, Attribute> engine = new DataFusionEngine<>(strategy);

        // print consistency report
        logger.info("*\tprinting consistency report\t*");
        engine.printClusterConsistencyReport(correspondences, null);

        // print record groups sorted by consistency
        engine.writeRecordGroupsByConsistency(new File("data/output/recordGroupConsistencies.csv"), correspondences, null);

        // run the fusion
        logger.info("*\tRunning data fusion\t*");
        FusibleDataSet<Species, Attribute> fusedDataSet = engine.run(correspondences, null);

        // write the result
        new SpeciesXMLFormatter().writeXML(new File("data/output/fused_final.xml"), fusedDataSet);

        // evaluate
        DataFusionEvaluator<Species, Attribute> evaluator = new DataFusionEvaluator<>(strategy, new RecordGroupFactory<Species, Attribute>());

        double accuracy = evaluator.evaluate(fusedDataSet, gs, null);

        logger.info(String.format("*\tAccuracy: %.2f", accuracy));

        fusedDataSet.printDataSetDensityReport();

        //System.out.println(fusedDataSet.size());

    }
}
