package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.Movie;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.string.ShortestString;
import de.uni_mannheim.informatik.dws.winter.model.*;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class GenericAttributeFuser<ValueType, RecordType extends Matchable & Fusible<SchemaElementType> & AttributeProvenancable<SchemaElementType>, SchemaElementType extends Matchable> extends
        AttributeValueFuser<ValueType, RecordType, SchemaElementType> {

    private final Function<RecordType, ValueType> valueGetter;
    private final BiConsumer<RecordType, ValueType> valueSetter;
    private final SchemaElementType schemaElementType;

    public GenericAttributeFuser(
            ConflictResolutionFunction<ValueType, RecordType, SchemaElementType> conflictResolution,
            Function<RecordType, ValueType> valueGetter,
            BiConsumer<RecordType, ValueType> valueSetter,
            SchemaElementType schemaElementType
    ) {
        super(conflictResolution);
        this.valueGetter = valueGetter;
        this.valueSetter = valueSetter;
        this.schemaElementType = schemaElementType;
    }

    @Override
    public void fuse(RecordGroup<RecordType, SchemaElementType> group, RecordType fusedRecord, Processable<Correspondence<SchemaElementType, Matchable>> schemaCorrespondences, SchemaElementType schemaElement) {
        // get the fused value
        FusedValue<ValueType, RecordType, SchemaElementType> fused = getFusedValue(group, schemaCorrespondences, schemaElement);

        // set the value for the fused record
        valueSetter.accept(fusedRecord, fused.getValue());

        // add provenance info
        fusedRecord.setAttributeProvenance(schemaElementType, fused.getOriginalIds());
    }

    @Override
    public boolean hasValue(RecordType record, Correspondence<SchemaElementType, Matchable> correspondence) {
        return record.hasValue(schemaElementType);
    }

    @Override
    public ValueType getValue(RecordType record, Correspondence<SchemaElementType, Matchable> correspondence) {
        return valueGetter.apply(record);
    }
}


