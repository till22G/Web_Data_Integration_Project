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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleFactory;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

public class SpeciesXMLReader extends XMLMatchableReader<Species, Attribute> implements
		FusibleFactory<Species, Attribute> {

	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.model.io.XMLMatchableReader#initialiseDataset(de.uni_mannheim.informatik.wdi.model.DataSet)
	 */
	@Override
	public void initialiseDataset(DataSet<Species, Attribute> dataset) {
		super.initialiseDataset(dataset);

		// the schema is defined in the Species class and not interpreted from the file, so we have to set the attributes manually
		dataset.addAttribute(Species.SCIENTIFICNAME);
		dataset.addAttribute(Species.COMMONNAMES);
		dataset.addAttribute(Species.CATEGORY);
		dataset.addAttribute(Species.ORDERS);
		dataset.addAttribute(Species.FAMILIES);
		dataset.addAttribute(Species.STATES);
//		dataset.addAttribute(Species.LABELS);
		dataset.addAttribute(Species.WHERELISTED);
		dataset.addAttribute(Species.DIFFERENTFROM);
//		dataset.addAttribute(Species.ENDEMICTO);
		dataset.addAttribute(Species.REGIONS);
		dataset.addAttribute(Species.REGIONNAMES);
		dataset.addAttribute(Species.LISTINGSTATUSES);

	}

	public String normalize(String s) {
		if (s == null) {
			return null;
		}
		s = s.replaceAll("\"", " ");
		s = s.replaceAll("\'", " ");
		s = s.replaceAll("´", " ");
		s = s.replaceAll("`", " ");
		s = s.replaceAll("-", " ");
		s = s.replaceAll("=", " ");
		return s.toLowerCase();
	}

	private List<String> getList(Node node, String childName) {
		List<String> list = getListFromChildElement(node, childName);
		if (list == null) {
			return null;
		}

		// apply normalizing function to each element in list
		list = list.stream().map(element -> normalize(element)).collect(Collectors.toList());

		List<String> cleanList = list.stream().filter(element -> element.length() > 0).collect(Collectors.toList());
		if (cleanList.isEmpty()) {
			return null;
		}
		return cleanList;
	}

	@Override
	public Species createModelFromElement(Node node, String provenanceInfo) {
		String id = getValueFromChildElement(node, "ID");
		String provenance = getValueFromChildElement(node, "Provenance");
		// apply normalizing function to scientific name
		String scientificName = normalize(getValueFromChildElement(node, "Scientific_Name"));

		List<String> commonNames = getList(node, "Common_Names");
		String category = getValueFromChildElement(node, "Category");
		List<String> orders = getList(node, "Orders");
		List<String> families = getList(node, "Families");
		List<String> states = getList(node, "States");
		List<String> labels = getList(node, "Labels");
		List<String> whereListed = getList(node, "Where_Listeds");
		List<String> differentFrom = getList(node, "Different_From_l");
		List<String> endemicTo = getList(node, "Endemic_To_l");
		List<String> regions = getList(node, "Regions");
		List<String> regionNames = getList(node, "Region_Names");
		List<String> listingStatuses = getList(node, "Listing_Statuses");


		return new Species(id, provenance, scientificName, commonNames, category, orders,
				families, states, labels, whereListed, differentFrom, endemicTo, regions, regionNames, listingStatuses);
	}

	@Override
	public Species createInstanceForFusion(RecordGroup<Species, Attribute> cluster) {

		List<String> ids = new LinkedList<>();

		for (Species m : cluster.getRecords()) {
			ids.add(m.getIdentifier());
		}

		Collections.sort(ids);

		String mergedId = StringUtils.join(ids, '+');

		return new Species(mergedId, "fused");
	}



}
