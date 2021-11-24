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
package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model;

import java.util.List;
import java.util.stream.Collectors;

import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

public class SpeciesXMLReader extends XMLMatchableReader<Species, Attribute> {

    /* (non-Javadoc)
     * @see de.uni_mannheim.informatik.wdi.model.io.XMLMatchableReader#initialiseDataset(de.uni_mannheim.informatik.wdi.model.DataSet)
     */
    @Override
    protected void initialiseDataset(DataSet<Species, Attribute> dataset) {
        super.initialiseDataset(dataset);

    }

    private List<String> getList(Node node, String childName) {
        List<String> list = getListFromChildElement(node, childName);
        if (list == null ) {
            return null;
        }
        List<String> cleanList = list.stream().filter(element -> element.length() > 0).collect(Collectors.toList());
        if (cleanList.isEmpty()){
            return null;
        }
        return cleanList;
    }

    @Override
    public Species createModelFromElement(Node node, String provenanceInfo) {
        String id = getValueFromChildElement(node, "ID");
        String provenance = getValueFromChildElement(node, "Provenance");
        String scientificName = getValueFromChildElement(node, "Scientific_Name");

        List<String> commonNames = getList(node, "Common_Names");
        List<String> categories = getList(node, "Categories");
        List<String> orders = getList(node, "Orders");
        List<String> families = getList(node, "Families");
        List<String> states = getList(node, "States");
        List<String> labels = getList(node, "Labels");
        List<String> whereListed = getList(node, "Where_Listed_l");
        List<String> differentFrom = getList(node, "Different_From_l");
        List<String> endemicTo = getList(node, "Endemic_To_l");
        List<String> regions = getList(node, "Regions");
        List<String> regionNames = getList(node, "Region_Names");
        List<String> listingStatuses = getList(node, "Listing_Statuses");


        return new Species(id, provenance, scientificName, commonNames, categories, orders,
                families, states, labels, whereListed, differentFrom, endemicTo, regions, regionNames, listingStatuses);
    }

}
