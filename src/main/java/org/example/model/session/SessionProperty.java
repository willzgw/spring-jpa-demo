package org.example.model.session;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Splitter;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.TimeBasedModel;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Entity
@Table(name = "session_properties_tab")
@DiscriminatorColumn(name = "property_type")
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BooleanSessionProperty.class, name = "BOOLEAN"),
        @JsonSubTypes.Type(value = DataSizeSessionProperty.class, name = "DATA_SIZE"),
        @JsonSubTypes.Type(value = DoubleSessionProperty.class, name = "DOUBLE"),
        @JsonSubTypes.Type(value = DurationSessionProperty.class, name = "DURATION"),
        @JsonSubTypes.Type(value = IntegerSessionProperty.class, name = "INTEGER"),
        @JsonSubTypes.Type(value = StringSessionProperty.class, name = "STRING")
})
public abstract class SessionProperty
        implements TimeBasedModel<SessionProperty>
{
    protected static final Splitter DEFAULT_SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();

    @JsonCreator
    public SessionProperty (
            @JsonProperty("id") Long id,
            @JsonProperty("type") SessionPropertyType type,
            @JsonProperty("uniqueKey") String uniqueKey,
            @JsonProperty("defaultValue") String defaultValue,
            @JsonProperty("minValue") String minValue,
            @JsonProperty("maxValue") String maxValue,
            @JsonProperty("description") String description,
            @JsonProperty("createTime") Long createTime,
            @JsonProperty("updateTime") Long updateTime)
    {
        this.id = id;
        this.type = type;
        this.uniqueKey = uniqueKey;
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.description = description;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "uniq_property_name", unique = true, nullable = false)
    protected String uniqueKey;
    @Column(name = "property_type", columnDefinition = "ENUM('BOOLEAN', 'DATA_SIZE', 'DOUBLE', 'DURATION', 'INTEGER', 'STRING')",
            insertable = false, updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    protected SessionPropertyType type;
    @Column(name = "default_value", nullable = false)
    protected String defaultValue;
    @Column(name = "min_value")
    protected String minValue;
    @Column(name = "max_value", nullable = false)
    protected String maxValue;
    @Column(name = "property_description")
    protected String description;
    @Column(name = "create_time")
    protected Long createTime;
    @Column(name = "update_time")
    protected Long updateTime;

    @JsonProperty
    public Long getId()
    {
        return id;
    }

    @JsonProperty
    public String getUniqueKey()
    {
        return uniqueKey.toLowerCase();
    }

    @JsonProperty
    public SessionPropertyType getType()
    {
        return type;
    }

    public void setType(SessionPropertyType type)
    {
        this.type = type;
    }

    @JsonProperty
    public String getDefaultValue()
    {
        return defaultValue;
    }

    @JsonProperty
    public String getMinValue()
    {
        return minValue;
    }

    @JsonProperty
    public String getMaxValue()
    {
        return maxValue;
    }

    @JsonProperty
    public String getDescription()
    {
        return description;
    }

    @JsonProperty
    public Long getCreateTime()
    {
        return createTime;
    }

    @JsonProperty
    public Long getUpdateTime()
    {
        return updateTime;
    }

    abstract public boolean isExpectedValue(String value);

    public enum SessionPropertyType
    {
        BOOLEAN,
        DATA_SIZE,
        DOUBLE,
        DURATION,
        INTEGER,
        STRING;
    }
}
