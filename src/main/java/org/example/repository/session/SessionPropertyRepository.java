package org.example.repository.session;

import org.example.model.session.SessionProperty;
import org.example.repository.TimeBasedRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SessionPropertyRepository
        extends TimeBasedRepository<SessionProperty, Long>
{
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "Update session_properties_tab SET default_value = ?2, min_value = ?3, max_value = ?4, property_description = ?5 WHERE uniq_property_name = ?1")
    void updateSessionProperty(String name, String defaultValue, String min, String max, String description);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM session_properties_tab WHERE uniq_property_name = ?1")
    void delete(String property);
}
