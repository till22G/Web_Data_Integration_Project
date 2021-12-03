package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers;

import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.util.Collection;

public interface AttributeProvenancable<SchemaElementType extends Matchable> {
    void setAttributeProvenance(SchemaElementType attribute, Collection<String> provenance);
}
