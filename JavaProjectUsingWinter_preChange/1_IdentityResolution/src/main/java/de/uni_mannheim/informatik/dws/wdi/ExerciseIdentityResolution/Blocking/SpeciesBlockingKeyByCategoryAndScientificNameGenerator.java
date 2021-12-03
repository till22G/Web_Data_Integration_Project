package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Species;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.BlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class SpeciesBlockingKeyByCategoryAndScientificNameGenerator extends
        RecordBlockingKeyGenerator<Species, Attribute> {

    private static final long serialVersionUID = 1L;


    @Override
    public void generateBlockingKeys(Species species, Processable<Correspondence<Attribute, Matchable>> correspondences,
                                     DataIterator<Pair<String, Species>> resultCollector) {

        // create the blockingKey value
        String blockingKeyValue = "";

        // declare category
        String category;
        // get the category for the first part of the blocking key
        if (species.getCategory() != null) {
            category = species.getCategory().toUpperCase();
        } else {
            category = "OTHER";
        }
        // extract first three letters from string (this uniquely identifies the category)
        String categoryToken = category.substring(0, Math.min(3,category.length())).toUpperCase();

        // add category token to blockingKey value
        blockingKeyValue = blockingKeyValue + categoryToken;


        // declare scientificName
        String scientificName;
        // get the scientific name for blocking
        if (species.getScientificName() != null) {
            scientificName = species.getScientificName().toUpperCase();
        } else {
            scientificName = "OTHER";
        }
        // extract first three letters from string
        String scientificNameToken = scientificName.substring(0, Math.min(3,scientificName.length())).toUpperCase();

        // add scientificName token to blockingKey
        blockingKeyValue = blockingKeyValue + scientificNameToken;

        // collect the result pair of blockingKey and species
        resultCollector.next(new Pair<>(blockingKeyValue, species));

    }

}
