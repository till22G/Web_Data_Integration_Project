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

import de.uni_mannheim.informatik.dws.winter.model.Matchable;

import java.util.List;

public class Species implements Matchable {

    protected String id;
    protected String provenance;
    private  String scientificName;
    private  List<String> commonNames;


    private String category;
    private List<String> orders;
    private  List<String> families;
    private  List<String> states;
    private  List<String> labels;
    private  List<String> whereListed;
    private  List<String> differentFrom;
    private  List<String> endemicTo;
    private  List<String> regions;
    private  List<String> regionNames;
    private  List<String> listingStatuses;

    public Species(String id,
                   String provenance,
                   String scientificName,
                   List<String> commonNames,
                   String category,
                   List<String> orders,
                   List<String> families,
                   List<String> states,
                   List<String> labels,
                   List<String> whereListed,
                   List<String> differentFrom,
                   List<String> endemicTo,
                   List<String> regions,
                   List<String> regionNames,
                   List<String> listingStatuses) {
        this.id = id;
        this.provenance = provenance;
        this.scientificName = scientificName;
        this.commonNames = commonNames;
        this.category = category;
        this.orders = orders;
        this.families = families;
        this.states = states;
        this.labels = labels;
        this.whereListed = whereListed;
        this.differentFrom = differentFrom;
        this.endemicTo = endemicTo;
        this.regions = regions;
        this.regionNames = regionNames;
        this.listingStatuses = listingStatuses;
    }


    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public String getProvenance() {
        return provenance;
    }


    @Override
    public int hashCode() {
        return getIdentifier().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Species) {
            return this.getIdentifier().equals(((Species) obj).getIdentifier());
        } else
            return false;
    }


    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public List<String> getCommonNames() {
        return commonNames;
    }

    public void setCommonNames(List<String> commonNames) {
        this.commonNames = commonNames;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

    public List<String> getFamilies() {
        return families;
    }

    public void setFamilies(List<String> families) {
        this.families = families;
    }

    public List<String> getStates() {
        return states;
    }

    public void setStates(List<String> states) {
        this.states = states;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<String> getWhereListed() {
        return whereListed;
    }

    public void setWhereListed(List<String> whereListed) {
        this.whereListed = whereListed;
    }

    public List<String> getDifferentFrom() {
        return differentFrom;
    }

    public void setDifferentFrom(List<String> differentFrom) {
        this.differentFrom = differentFrom;
    }

    public List<String> getEndemicTo() {
        return endemicTo;
    }

    public void setEndemicTo(List<String> endemicTo) {
        this.endemicTo = endemicTo;
    }

    public List<String> getRegions() {
        return regions;
    }

    public void setRegions(List<String> regions) {
        this.regions = regions;
    }

    public List<String> getRegionNames() {
        return regionNames;
    }

    public void setRegionNames(List<String> regionNames) {
        this.regionNames = regionNames;
    }

    public List<String> getListingStatuses() {
        return listingStatuses;
    }

    public void setListingStatuses(List<String> listingStatuses) {
        this.listingStatuses = listingStatuses;
    }
}
