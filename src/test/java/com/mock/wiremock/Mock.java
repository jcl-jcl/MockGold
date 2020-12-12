package com.mock.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.reset;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class Mock {
    static WireMockServer wireMockServer;
    @BeforeAll
    public static void stubMock(){
        wireMockServer = new WireMockServer(wireMockConfig().port(8089));
        wireMockServer.start();
        configureFor("localhost", 8089);

//        reset();
//        wireMockServer.stop();
    }

    @Test
    public void statusMessage() {
        stubFor(get(urlEqualTo("/some/thing"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("Hello world!")));
        ;

        try {
            Thread.sleep(500000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void easyMock() {
        try {
        stubFor(get(urlEqualTo("/some/thing"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("Hello world!")));
        Thread.sleep(10000);

        reset();
        stubFor(get(urlEqualTo("/some/thing"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("你好，java")));
        Thread.sleep(10000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void proxyMock(){
        // Low priority catch-all proxies to otherhost.com by default
        try {
            stubFor(get(urlMatching(".*")).atPriority(10)
                    .willReturn(aResponse().proxiedFrom("https://ceshiren.com/")));


            // High priority stub will send a Service Unavailable response
            // if the specified URL is requested
            stubFor(get(urlEqualTo("/categories_and_latest")).atPriority(10)
                    .willReturn(aResponse().withBody(Files.readAllBytes(Paths.
                            get(Mock.class.getResource("/mock.json").getPath())))));
            Thread.sleep(500000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
