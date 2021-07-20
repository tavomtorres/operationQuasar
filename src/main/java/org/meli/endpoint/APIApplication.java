package org.meli.endpoint;

import javax.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.info.*;
@OpenAPIDefinition(
    tags = {
            @Tag(name="widget", description="Widget operations."),
            @Tag(name="gasket", description="Operations related to gaskets")
    },
    info = @Info(
        title="Quasar Operation Fire",
        version = "1.0.2",
        contact = @Contact(
            name = "Email Gustavo Mendez",
            email = "gmendeztorres5d@gmail.com")
            )
)
public class APIApplication extends Application {
    
}
