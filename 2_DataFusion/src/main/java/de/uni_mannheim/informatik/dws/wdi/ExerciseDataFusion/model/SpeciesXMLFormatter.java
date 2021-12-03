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

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;


public class SpeciesXMLFormatter extends XMLFormatter<Species> {


	@Override
	public Element createRootElement(Document doc) {
		return doc.createElement("Species");
	}

	@Override
	public Element createElementFromRecord(Species record, Document doc) {
		Element species = doc.createElement("Species");

		species.appendChild(createTextElement("ID", record.getIdentifier(), doc));
		species.appendChild(createTextElement("Scientific_Name", record.getScientificName(), doc));
		species.appendChild(createListElement("Common_Names", "Common_Name", record.getCommonNames(), doc));
		species.appendChild(createTextElement("Category", record.getCategory(), doc));
		species.appendChild(createListElement("Orders", "Order", record.getOrders(), doc));
		species.appendChild(createListElement("Families", "Family", record.getFamilies(), doc));
		species.appendChild(createListElement("States", "State", record.getStates(), doc));
		species.appendChild(createListElement("Labels", "Label", record.getLabels(), doc));
		species.appendChild(createListElement("Where_Listed_l", "Where_Listed", record.getWhereListed(), doc));
		species.appendChild(createListElement("Different_From_l", "Different_From", record.getDifferentFrom(), doc));
		species.appendChild(createListElement("Endemic_To_l", "Endemic_To", record.getEndemicTo(), doc));
		species.appendChild(createListElement("Regions", "Region", record.getRegions(), doc));
		species.appendChild(createListElement("Region_Names", "Region_Name", record.getRegionNames(), doc));
		species.appendChild(createListElement("Listing_Statuses", "Listing_Status", record.getListingStatuses(), doc));


		return species;
	}

	protected Element createTextElementWithProvenance(String name,
													  String value, String provenance, Document doc) {
		Element elem = createTextElement(name, value, doc);
		elem.setAttribute("provenance", provenance);
		return elem;
	}

	protected Element createListElement(String listName, String elementName, List<String> elements, Document doc) {
		Element actorRoot = doc.createElement(listName);

		for (String element : elements) {
			actorRoot.appendChild(createTextElement(elementName, element, doc));
		}

		return actorRoot;
	}
}
