package com.yzcx.onlinecar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Primary
public class Swagger2Config implements SwaggerResourcesProvider {

    @Value("${swagger.api.names}")
    private String[] apiNames;

    @Override
    public List<SwaggerResource> get() {
        List resources = new ArrayList<>();
        if (apiNames != null) {
            Arrays.stream(apiNames).forEach(s ->
                    resources.add(swaggerResource(s, "/onlinecar/" + s + "/v2/api-docs", "1.0"))
            );
        }
        return resources;
    }
    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}