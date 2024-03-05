package com.example.demo.api;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@RunWith(SpringRunner.class)
@ContextConfiguration
public abstract class AbstractApiTest {

    @Value("${server.port}")
    private int serverPort;

    @Value("${env.host.ip}")
    private String hostIp;

    @Value("${env.host.scheme}")
    private String scheme;

    private @Autowired WebApplicationContext context;

    private final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    protected MockMvc mockMvc;

    @Rule
    public JUnitRestDocumentation getRestDocumentation() {
        return restDocumentation;
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withResponseDefaults(prettyPrint())
                        .withRequestDefaults(prettyPrint())
                        .and()
                        .uris()
                        .withScheme(scheme).withHost(hostIp).withPort(serverPort)
                )
                .build();
    }
}

