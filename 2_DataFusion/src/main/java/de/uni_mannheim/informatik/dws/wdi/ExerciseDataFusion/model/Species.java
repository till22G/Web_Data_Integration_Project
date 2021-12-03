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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.AttributeProvenancable;
import org.apache.commons.lang3.StringUtils;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import org.w3c.dom.Attr;

public class Species extends AbstractRecord<Attribute> implements Serializable, AttributeProvenancable<Attribute> {

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
				   String provenance){
		this.id = id;
		this.provenance = provenance;
	}

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
    
    //(?)
//	private Map<Attribute, Collection<String>> provenance = new HashMap<>();
//	private Collection<String> recordProvenance;
//
//	public void setRecordProvenance(Collection<String> provenance) {
//		recordProvenance = provenance;
//	}
//
//	public Collection<String> getRecordProvenance() {
//		return recordProvenance;
//	}
//
//	public void setAttributeProvenance(Attribute attribute,
//			Collection<String> provenance) {
//		this.provenance.put(attribute, provenance);
//	}
//
//	public Collection<String> getAttributeProvenance(String attribute) {
//		return provenance.get(attribute);
//	}
//
//	public String getMergedAttributeProvenance(Attribute attribute) {
//		Collection<String> prov = provenance.get(attribute);
//
//		if (prov != null) {
//			return StringUtils.join(prov, "+");
//		} else {
//			return "";
//		}
//	}

    
//	dataset.addAttribute(Species.SCIENTIFICNAME);
//	dataset.addAttribute(Species.COMMONNAMES);
//	dataset.addAttribute(Species.CATEGORY);
//	dataset.addAttribute(Species.ORDERS);
//	dataset.addAttribute(Species.FAMILIES);
//	dataset.addAttribute(Species.STATES);
//	dataset.addAttribute(Species.LABELS);
//	dataset.addAttribute(Species.WHERELISTED);
//	dataset.addAttribute(Species.DIFFERENTFROM);
//	dataset.addAttribute(Species.ENDEMICTO);
//	dataset.addAttribute(Species.REGIONS);
//	dataset.addAttribute(Species.REGIONNAMES);
//	dataset.addAttribute(Species.LISTINGSTATUSES);
	
	public static final Attribute SCIENTIFICNAME = new Attribute("scientificName");
	public static final Attribute COMMONNAMES = new Attribute("commonNames");
	public static final Attribute CATEGORY = new Attribute("category");
	public static final Attribute ORDERS = new Attribute("orders");
	public static final Attribute FAMILIES = new Attribute("families");
	public static final Attribute STATES = new Attribute("states");
	public static final Attribute LABELS = new Attribute("labels");
	public static final Attribute WHERELISTED = new Attribute("whereListed");
	public static final Attribute DIFFERENTFROM = new Attribute("differentFrom");
	public static final Attribute ENDEMICTO = new Attribute("endemicTo");
	public static final Attribute REGIONS = new Attribute("regions");
	public static final Attribute REGIONNAMES = new Attribute("regionnames");
	public static final Attribute LISTINGSTATUSES = new Attribute("listingStatuses");
	
	
	
	
	
	@Override
	public boolean hasValue(Attribute attribute) {
		if(attribute==SCIENTIFICNAME)
			return getScientificName() != null && !getScientificName().isEmpty();
		else if(attribute==COMMONNAMES)
			return getCommonNames() != null && getCommonNames().size() > 0;
		else if(attribute==CATEGORY)
			return getCategory() != null && !getCategory().isEmpty();
		else if(attribute==ORDERS)
			return getOrders() != null && getOrders().size() > 0;
		else if(attribute==FAMILIES)
			return getFamilies() != null && getFamilies().size() > 0;
		else if(attribute==STATES)
			return getStates() != null && !getStates().isEmpty();
		else if(attribute==LABELS)
			return getLabels() != null && getLabels().size() > 0;
		else if(attribute==WHERELISTED)
			return getWhereListed() != null && getWhereListed().size() > 0;	
		else if(attribute==DIFFERENTFROM)
			return getDifferentFrom() != null && !getDifferentFrom().isEmpty();
		else if(attribute==ENDEMICTO)
			return getEndemicTo() != null && getEndemicTo().size() > 0;
		else if(attribute==REGIONS)
			return getRegions() != null && getRegions().size() > 0;
		else if(attribute==REGIONNAMES)
			return getRegionNames() != null && getRegionNames().size() > 0;
		else if(attribute==LISTINGSTATUSES)
			return getListingStatuses() != null && getListingStatuses().size() > 0;
		else
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

    @Override
    public void setAttributeProvenance(Attribute attribute, Collection<String> provenance) {
//        this.provenance.put(attribute, provenance);
    }
}
