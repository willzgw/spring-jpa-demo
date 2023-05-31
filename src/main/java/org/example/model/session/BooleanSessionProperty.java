package org.example.model.session;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Entity
@JsonTypeName("BOOLEAN")
@DiscriminatorValue("BOOLEAN")
public class BooleanSessionProperty
        extends SessionProperty
{
    @Override
    public boolean isExpectedValue(String value)
    {
        try {
            Iterable<String> allowedValues = DEFAULT_SPLITTER.split(maxValue);
            Set<String> allowedValueSet = StreamSupport.stream(allowedValues.spliterator(), false)
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());
            return allowedValueSet.contains(value.toLowerCase());
        }
        catch (Exception e) {
            log.error("Exception during parse boolean: value={}, max={}, {}", value, maxValue, e);
            return false;
        }
    }
}
