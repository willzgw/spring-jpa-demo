package org.example.model.session;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.airlift.units.DataSize;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Slf4j
@Entity
@JsonTypeName("DATA_SIZE")
@DiscriminatorValue("DATA_SIZE")
public class DataSizeSessionProperty
        extends SessionProperty
{
    @Override
    public boolean isExpectedValue(String value)
    {
        try {
            DataSize size = DataSize.valueOf(value);
            DataSize minSize = DataSize.valueOf(minValue);
            DataSize maxSize = DataSize.valueOf(maxValue);

            if (size.compareTo(minSize) < 0 || size.compareTo(maxSize) > 0) {
                log.error("DataSize exceed threshold: value={}, min={}, max={}", value, minValue, maxValue);
                return false;
            }
        }
        catch (IllegalArgumentException e) {
            log.error("Illegal argument for Duration: value={}, min={}, max={}, {}", value, minValue, maxValue, e);
            return false;
        }
        catch (Exception e) {
            log.error("Exception during parse Duration: value={}, min={}, max={}, {}", value, minValue, maxValue, e);
            return false;
        }

        return true;
    }
}
