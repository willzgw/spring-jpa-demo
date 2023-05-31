package org.example.model.session;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.airlift.units.Duration;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Slf4j
@Entity
@JsonTypeName("DURATION")
@DiscriminatorValue("DURATION")
public final class DurationSessionProperty
        extends SessionProperty
{
    @Override
    public boolean isExpectedValue(String value)
    {
        try {
            Duration duration = Duration.valueOf(value);
            Duration minDuration = Duration.valueOf(minValue);
            Duration maxDuration = Duration.valueOf(maxValue);

            if (duration.compareTo(minDuration) < 0 || duration.compareTo(maxDuration) > 0) {
                log.error("Duration exceed threshold: value={}, min={}, max={}", value, minValue, maxValue);
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
