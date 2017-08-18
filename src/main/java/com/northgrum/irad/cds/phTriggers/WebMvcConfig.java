package com.northgrum.irad.cds.phTriggers;

import com.northgrum.irad.cds.rest.VersionRequestMappingHandlerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Created by caldama on 10/6/16.
 */
//@Configuration - No versioning for this project yet
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    private ContentNegotiationManager contentNegotiationManager;

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        VersionRequestMappingHandlerMapping handlerMapping = new VersionRequestMappingHandlerMapping();
        handlerMapping.setOrder(0);
        handlerMapping.setRemoveSemicolonContent(false);
        handlerMapping.setContentNegotiationManager(contentNegotiationManager);

        return handlerMapping;
    }



}
