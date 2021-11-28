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


public class SpeciesBlockingKeyCascadedGenerator extends
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
        String categoryToken = category.substring(0, Math.min(2,category.length())).toUpperCase();

        // check category tp see if it is insect
        if (category.equals("INSECT")) {

            // declare orders
            String orders;

            // get the Order from species for second part of the blocking key
            if (species.getOrders() != null) {
                orders = species.getOrders().get(0).toUpperCase();
            } else {
                orders = "";
            }

            // add another part to the key if it belongs to one of the biggest categories in Insects
            String orderToken = "";

            switch (orders){
                case "COLEOPTERA": orderToken = "COL";
                    break;
                case "HYMENOPTERA": orderToken = "HYM";
                    break;
                case "LEPIDOPTERA": orderToken = "LEP";
                    break;
                case "DIPTERA": orderToken = "DIP";
                    break;
                case "TRICHOPTERA": orderToken = "TRI";
                    break;
                case "ODONATA": orderToken = "ODO";
                    break;
                case "NEUROPTERA": orderToken = "NEU";
                    break;
                case "HEMIPTERA": orderToken = "HEM";
                    break;
                case "ORTHOPTERA": orderToken = "ORT";
                    break;
            }

            blockingKeyValue = blockingKeyValue + orderToken;
        }

        // add category token to blockingKey value
        blockingKeyValue = blockingKeyValue + categoryToken;

        // declare scientificName
        String scientificName;

        // get the scientific name for blocking
        if(species.getScientificName() != null) {
            scientificName = species.getScientificName();
        } else {
            scientificName = "OTHER";
        }
        // extract first three letters from string
        String scientificNameToken = scientificName.substring(0, Math.min(2,scientificName.length())).toUpperCase();

        // add scientificName token to blockingKey
        blockingKeyValue = blockingKeyValue + scientificNameToken;

        // collect the result pair of blockingKey and species
        resultCollector.next(new Pair<>(blockingKeyValue, species));

    }

}
