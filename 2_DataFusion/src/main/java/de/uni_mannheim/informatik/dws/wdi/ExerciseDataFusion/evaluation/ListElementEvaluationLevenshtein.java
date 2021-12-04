package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.Species;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinEditDistance;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class ListElementEvaluationLevenshtein extends EvaluationRule<Species, Attribute> {

    private final Function<Species, List<String>> valueGetter;

    SimilarityMeasure<String> sim = new LevenshteinEditDistance();
    public ListElementEvaluationLevenshtein(Function<Species, List<String>> valueGetter) {
        this.valueGetter = valueGetter;
    }

    @Override
    public boolean isEqual(Species record1, Species record2, Attribute schemaElement) {

        if (valueGetter.apply(record1) == null && valueGetter.apply(record2) == null) {
            return true;
        } else if (valueGetter.apply(record1) == null ^ valueGetter.apply(record2) == null) {
            return false;
        } else {
            Set<String> set1 = new HashSet<>(valueGetter.apply(record1));
            Set<String> set2 = new HashSet<>(valueGetter.apply(record2));

            for(String elem : set1){
                for(String other : set2){
                    if(sim.calculate(elem, other) <= 1) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public boolean isEqual(Species record1, Species record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {
        return isEqual(record1, record2, (Attribute) null);
    }
}
