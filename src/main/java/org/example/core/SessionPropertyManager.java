package org.example.core;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.example.model.session.SessionProperty;
import org.example.repository.session.SessionPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Component
public class SessionPropertyManager
{
    private Map<String, SessionProperty> allSessionProperties = ImmutableMap.of();;
    private final SessionPropertyRepository sessionPropertyRepo;

    @Autowired
    public SessionPropertyManager(SessionPropertyRepository sessionPropertyRepo)
    {
        this.sessionPropertyRepo = sessionPropertyRepo;
        refreshSessionProperties();
    }

    public void refreshSessionProperties()
    {
        try {
            Iterable<SessionProperty> properties = sessionPropertyRepo.findAll();
            this.allSessionProperties = StreamSupport.stream(properties.spliterator(), false)
                    .collect(Collectors.toMap(SessionProperty::getUniqueKey, Function.identity()));
            log.warn("Session properties updated.");
        }
        catch (Exception e) {
            log.error("Refresh Session Properties Exception: " + e);
        }
    }

    public Set<String> findIllegalProperties(Set<String> properties)
    {
        ImmutableSet.Builder<String> illegalProperties = ImmutableSet.builder();
        if (allSessionProperties.isEmpty()) {
            log.error("Session properties white list is empty.");
            return illegalProperties.build();
        }

        properties.forEach(property -> {
            if (!allSessionProperties.keySet().contains(property.toLowerCase())) {
                illegalProperties.add(property);
            }
        });
        return illegalProperties.build();
    }

    public boolean isExpectedValue(String property, String value)
    {
        SessionProperty sessionProperty = allSessionProperties.get(property);
        if (sessionProperty == null) {
            log.error("Cache Exception or Session property not found: {}", property);
            // Because "findIllegalProperties" is invoked before "isExpectedValue", so it will only happen when the cache occurs exception.
            // Skip checking in that case.
            return true;
        }
        return sessionProperty.isExpectedValue(value);
    }

    public String getDefaultValue(String property)
    {
        SessionProperty sessionProperty = allSessionProperties.get(property);
        if (sessionProperty == null) {
            log.error("Cache Exception or Session property not found: {}", property);
            return null;
        }
        return sessionProperty.getDefaultValue();
    }
}

