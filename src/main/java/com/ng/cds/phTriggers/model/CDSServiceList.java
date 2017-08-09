package com.ng.cds.phTriggers.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(locations = "classpath:ph-services.yml")
@JsonSerialize(using = CDSServiceListSerializer.class)
public class CDSServiceList implements Serializable {
    private List<CDSServiceInformation> services;

    @Data
    public static class CDSServiceInformation  {
        private String id;
        private Hook hook;
        private String title;
        private String description;
        //private Prefetch prefetch;


    }

    enum Hook {
        patient_view, medication_prescribe, order_review
    }
}


class CDSServiceListSerializer extends StdSerializer<CDSServiceList> {


    public CDSServiceListSerializer() {
        this (null);
    }
    public CDSServiceListSerializer(Class<CDSServiceList> t) {
        super(t);
    }


    @Override
    public void serialize(CDSServiceList cdsServiceList, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        jgen.writeFieldName("services");
        jgen.writeStartArray();
        for (CDSServiceList.CDSServiceInformation info: cdsServiceList.getServices()) {
            jgen.writeStartObject();
            jgen.writeStringField("id", info.getId());
            jgen.writeStringField("hook", info.getHook().toString());
            jgen.writeStringField("title", info.getTitle());
            jgen.writeStringField("description", info.getDescription());
            jgen.writeEndObject();
        }
        jgen.writeEndArray();
        jgen.writeEndObject();
    }
}