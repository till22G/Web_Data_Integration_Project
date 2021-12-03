
package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.Species;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class ListEvaluationRule extends EvaluationRule<Species, Attribute> {

    private final Function<Species, List<String>> valueGetter;

    public ListEvaluationRule(Function<Species, List<String>> valueGetter) {
        this.valueGetter = valueGetter;
    }

    @Override
    public boolean isEqual(Species record1, Species record2, Attribute schemaElement) {
        Set<String> set1 = new HashSet<>(valueGetter.apply(record1));
        Set<String> set2 = new HashSet<>(valueGetter.apply(record2));

        return set1.containsAll(set2) && set2.containsAll(set1);
    }

    @Override
    public boolean isEqual(Species record1, Species record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {
        return isEqual(record1, record2, (Attribute) null);
    }
}