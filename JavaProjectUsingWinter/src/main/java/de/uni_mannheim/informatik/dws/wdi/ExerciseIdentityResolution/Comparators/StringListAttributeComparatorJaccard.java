/*
 * Copyright (c) 2017 Data and Web Science Group, University of Mannheim, Germany (http://dws.informatik.uni-mannheim.de/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators;

import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;

import java.util.List;
import java.util.function.Function;

public class StringListAttributeComparatorJaccard<T extends Matchable> extends AbstractAttributeComparator<T, List<String>> {

    private static final long serialVersionUID = 1L;
    private TokenizingJaccardSimilarity sim = new TokenizingJaccardSimilarity();

    public StringListAttributeComparatorJaccard(Function<T, List<String>> attributeExtractor, String attributeName) {
        super(attributeExtractor, attributeName);
    }

    @Override
    public double compare(
            T record1,
            T record2,
            Correspondence<Attribute, Matchable> schemaCorrespondences) {

        List<String> s1 = attributeExtractor.apply(record1);
        List<String> s2 = attributeExtractor.apply(record2);

        if (s1 == null || s2 == null) {
            return 0.0;
        }

        double max_sim = 0;
        for (String element : s1) {
            for (String other : s2) {
                double similarity = sim.calculate(element, other);
                max_sim = Math.max(similarity, max_sim);
            }
        }

        log(s1.toString(), s2.toString(), max_sim);

        return max_sim;
    }

}
