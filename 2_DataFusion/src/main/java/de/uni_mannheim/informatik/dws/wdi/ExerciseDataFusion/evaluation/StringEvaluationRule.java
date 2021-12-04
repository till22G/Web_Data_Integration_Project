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
package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.Movie;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.Species;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;


public class StringEvaluationRule extends EvaluationRule<Species, Attribute> {

	SimilarityMeasure<String> sim = new TokenizingJaccardSimilarity();
	private final Function<Species, String> valueGetter;

	public StringEvaluationRule(Function<Species, String> valueGetter) {
		this.valueGetter = valueGetter;
	}

	@Override
	public boolean isEqual(Species record1, Species record2, Attribute schemaElement) {
		//all the tokens should be there, but the order does not matter
		if(valueGetter.apply(record1)==null && valueGetter.apply(record2)==null)
			return true;
		else if(valueGetter.apply(record1)==null ^ valueGetter.apply(record2)==null)
			return false;
		else{
			return sim.calculate(valueGetter.apply(record1), valueGetter.apply(record2)) == 1.0;
		}


	}

	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.datafusion.EvaluationRule#isEqual(java.lang.Object, java.lang.Object, de.uni_mannheim.informatik.wdi.model.Correspondence)
	 */
	@Override
	public boolean isEqual(Species record1, Species record2,
			Correspondence<Attribute, Matchable> schemaCorrespondence) {
		return isEqual(record1, record2, (Attribute)null);
	}
	
}
