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

import de.uni_mannheim.informatik.dws.winter.matching.rules.comparators.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.comparators.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.util.function.Function;

public abstract class AbstractAttributeComparator<T extends Matchable, U> implements Comparator<T, Attribute> {

    private static final long serialVersionUID = 1L;

    protected final Function<T, U> attributeExtractor;
	private final String attributeName;

    protected ComparatorLogger comparisonLog;

    public AbstractAttributeComparator(Function<T, U> attributeExtractor, String attributeName) {
        this.attributeExtractor = attributeExtractor;
        this.attributeName = attributeName;
    }

    @Override
    public ComparatorLogger getComparisonLog() {
        return this.comparisonLog;
    }

    @Override
    public void setComparisonLog(ComparatorLogger comparatorLog) {
        this.comparisonLog = comparatorLog;
    }

    protected void log(String s1, String s2, double similarity){
        if (this.comparisonLog != null) {
            this.comparisonLog.setComparatorName(getName());

            this.comparisonLog.setRecord1Value(s1);
            this.comparisonLog.setRecord2Value(s2);

            this.comparisonLog.setSimilarity(Double.toString(similarity));
        }
    }

    @Override
    public String getName(Correspondence<Attribute, Matchable> schemaCorrespondence) {
        return getName();
    }

    private String getName() {
        return this.getClass().getSimpleName() + "." + attributeName;
    }
}
