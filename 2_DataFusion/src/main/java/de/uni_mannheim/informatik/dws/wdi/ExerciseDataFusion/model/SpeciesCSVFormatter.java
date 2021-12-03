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
package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVDataSetFormatter;

import java.util.List;

public class SpeciesCSVFormatter extends CSVDataSetFormatter<Species, Attribute> {

	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.model.io.CSVDataSetFormatter#getHeader(de.uni_mannheim.informatik.wdi.model.DataSet)
	 */
	@Override
	public String[] getHeader(List<Attribute> orderedHeader) {
		return new String[] { "ID", "Scientific Name", "Commmon Names", "Category",
				"Orders", "Families", "States", "Labels", "Where Listed", "Different From", "Endemic To", "Regions", "Region Names", "Listing Statuses"};
	}

	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.model.io.CSVDataSetFormatter#format(de.uni_mannheim.informatik.wdi.model.Matchable, de.uni_mannheim.informatik.wdi.model.DataSet)
	 */
	@Override
	public String[] format(Species record, DataSet<Species, Attribute> dataset, List<Attribute> orderedHeader) {
		return new String[] {
				record.getIdentifier(),
				record.getScientificName().toString(),
				record.getCommonNames().toString(),
				record.getCategory().toString(),
				record.getOrders().toString(),
				record.getFamilies().toString(),
				record.getStates().toString(),
				record.getLabels().toString(),
				record.getWhereListed().toString(),
				record.getDifferentFrom().toString(),
				record.getEndemicTo().toString(),
				record.getRegions().toString(),
				record.getRegionNames().toString(),
				record.getListingStatuses().toString()
		};
	}

}
