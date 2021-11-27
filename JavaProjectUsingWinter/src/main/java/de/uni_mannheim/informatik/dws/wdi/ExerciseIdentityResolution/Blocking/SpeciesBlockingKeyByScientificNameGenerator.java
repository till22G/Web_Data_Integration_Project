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

public class SpeciesBlockingKeyByScientificNameGenerator extends
        RecordBlockingKeyGenerator<Species, Attribute> {

    private static final long serialVersionUID = 1L;


    @Override
    public void generateBlockingKeys(Species species, Processable<Correspondence<Attribute, Matchable>> correspondences,
                                     DataIterator<Pair<String, Species>> resultCollector) {

        // create the blockingKey value
        String blockingKeyValue = "";

        // get the scientific name for blocking
        String scientificName = species.getScientificName();
        // extract first three letters from string
        String scientificNameToken = scientificName.substring(0, Math.min(2,scientificName.length())).toUpperCase();

        // add scientificName token to blockingKey
        blockingKeyValue = blockingKeyValue + scientificNameToken;

        // collect the result pair of blockingKey and species
        resultCollector.next(new Pair<>(blockingKeyValue, species));

    }

}
