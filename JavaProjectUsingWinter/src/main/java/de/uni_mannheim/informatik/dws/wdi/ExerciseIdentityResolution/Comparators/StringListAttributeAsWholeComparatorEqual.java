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
import de.uni_mannheim.informatik.dws.winter.similarity.EqualsSimilarity;

import java.util.List;
import java.util.function.Function;

public class StringListAttributeAsWholeComparatorEqual<T extends Matchable>implements Comparator<T, Attribute>{

	private static final long serialVersionUID = 1L;
	private final Function<T, List<String>> attributeExtractor;
	private EqualsSimilarity<String> sim = new EqualsSimilarity<String>();

	private ComparatorLogger comparisonLog;

	public StringListAttributeAsWholeComparatorEqual(Function<T, List<String>> attributeExtractor){
		this.attributeExtractor = attributeExtractor;
	}

	@Override
	public double compare(
			T record1,
			T record2,
			Correspondence<Attribute, Matchable> schemaCorrespondences) {

		List<String> s1 = attributeExtractor.apply(record1);
		List<String> s2 = attributeExtractor.apply(record2);
		List<String> longer;
		List<String> shorter;

		if ( s1.size() > s2.size()) {
			shorter = s2;
			longer = s1;
		} else {
			shorter = s1;
			longer = s2;
		}

		double sum_similarities = 0;
		for(String element : longer){
			double max_sim = 0;
			for (String other : shorter) {
				double similarity = sim.calculate(element, other);
				Math.max(similarity, max_sim);

				if(this.comparisonLog != null){
					this.comparisonLog.setComparatorName(getClass().getName());

					this.comparisonLog.setRecord1Value(element);
					this.comparisonLog.setRecord2Value(other);

					this.comparisonLog.setSimilarity(Double.toString(similarity));
				}
			}
			sum_similarities += max_sim;
		}
		return sum_similarities / longer.size();
	}


	@Override
	public ComparatorLogger getComparisonLog() {
		return this.comparisonLog;
	}

	@Override
	public void setComparisonLog(ComparatorLogger comparatorLog) {
		this.comparisonLog = comparatorLog;
	}

}
