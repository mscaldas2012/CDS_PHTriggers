package com.ng.cds.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.lang.reflect.Method;

/**
 * Created by caldama on 10/6/16.
 */
public class VersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Value("${server.apiContext}")
    private String apiContext;

    @Value("${server.versionContext}")
    private String versionContext;

    @Override
    protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
        HandlerMethod method = super.lookupHandlerMethod(lookupPath, request);
        if (method == null && lookupPath.contains(getApiAndVersionContext())) {
            String afterAPIURL = lookupPath.substring(lookupPath.indexOf(getApiAndVersionContext()) + getApiAndVersionContext().length());
            String version = afterAPIURL.substring(0, afterAPIURL.indexOf("/"));
            String path = afterAPIURL.substring(version.length() + 1);

            int previousVersion = getPreviousVersion(version);
            if (previousVersion != 0) {
                lookupPath = getApiAndVersionContext() + previousVersion + "/" + path;
                final String lookupFinal = lookupPath;
                return lookupHandlerMethod(lookupPath, new HttpServletRequestWrapper(request) {
                    @Override
                    public String getRequestURI() {
                        return lookupFinal;
                    }

                    @Override
                    public String getServletPath() {
                        return lookupFinal;
                    }});
            }
        }
        return method;
    }

    private String getApiAndVersionContext() {
        String result = "/";
        if (apiContext != null && !apiContext.isEmpty()) {
            result += apiContext + "/";
        }
        result += versionContext;
        return result;
    }

    private int getPreviousVersion(final String version) {
        return new Integer(version) - 1 ;
    }


    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo info = super.getMappingForMethod(method, handlerType);

        ApiVersion methodAnnotation = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        if(methodAnnotation != null) {
            RequestCondition<?> methodCondition = getCustomMethodCondition(method);
            // Concatenate our ApiVersion with the usual request mapping
            info = createApiVersionInfo(methodAnnotation, methodCondition).combine(info);
        } else {
            ApiVersion typeAnnotation = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
            if(typeAnnotation != null) {
                RequestCondition<?> typeCondition = getCustomTypeCondition(handlerType);
                // Concatenate our ApiVersion with the usual request mapping
                info = createApiVersionInfo(typeAnnotation, typeCondition).combine(info);
            }
        }

        return info;
    }

    private RequestMappingInfo createApiVersionInfo(ApiVersion annotation, RequestCondition<?> customCondition) {
        int[] values = annotation.value();
        String[] patterns = new String[values.length];
        for(int i=0; i<values.length; i++) {
            // Build the URL prefix
            patterns[i] = getApiAndVersionContext()+values[i];
        }


        return new RequestMappingInfo(
                new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(), useSuffixPatternMatch(), useTrailingSlashMatch(), getFileExtensions()),
                new RequestMethodsRequestCondition(),
                new ParamsRequestCondition(),
                new HeadersRequestCondition(),
                new ConsumesRequestCondition(),
                new ProducesRequestCondition(),
                customCondition);
    }
}
