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

public class SpeciesBlockingKeyByCategoryGenerator extends
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
		if (species.getCategories().get(0) != null) {
			category = species.getCategories().get(0).toUpperCase();
		} else {
			category = "OTHER";
		}

		// extract first three letters from string (this uniquely identifies the category)
		String categoryToken = category.substring(0, Math.min(2,category.length())).toUpperCase();

		// add category token to blockingKey value
		blockingKeyValue = blockingKeyValue + categoryToken;

		// collect the result pair of blockingKey and species
		resultCollector.next(new Pair<>(blockingKeyValue, species));

    }

}
