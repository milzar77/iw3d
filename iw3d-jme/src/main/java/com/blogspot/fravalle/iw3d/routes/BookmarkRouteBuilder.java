package com.blogspot.fravalle.iw3d.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;


public class BookmarkRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:inputCamel")
                .transform(simple("Welcome ${body}"))
                .log(LoggingLevel.WARN, "Prefixed message: ${body}")
                .bean(MyBean.class, "doSomething")
        .to("mock:result")
        ;
    }
}
