package org.example.model.session;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Slf4j
@Entity
@JsonTypeName("INTEGER")
@DiscriminatorValue("INTEGER")
public class IntegerSessionProperty
        extends SessionProperty
{
    @Override
    public boolean isExpectedValue(String value)
    {
        try {
            Integer val = Integer.valueOf(value);
            Integer min = Integer.valueOf(minValue);
            Integer max = Integer.valueOf(maxValue);

            if (val.compareTo(min) < 0 || val.compareTo(max) > 0) {
                log.error("Value exceed threshold: value={}, min={}, max={}", value, minValue, maxValue);
                return false;
            }
        }
        catch (IllegalArgumentException e) {
            log.error("Illegal argument for Integer: value={}, min={}, max={}, {}", value, minValue, maxValue, e);
            return false;
        }
        catch (Exception e) {
            log.error("Exception during parse Integer: value={}, min={}, max={}, {}", value, minValue, maxValue, e);
            return false;
        }

        return true;
    }
}
