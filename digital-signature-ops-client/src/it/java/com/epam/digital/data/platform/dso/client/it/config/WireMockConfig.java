package com.epam.digital.data.platform.dso.client.it.config;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import java.net.MalformedURLException;
import java.net.URL;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class WireMockConfig {

  @Bean
  public WireMockServer restClientWireMock(@Value("${dso.url}") String urlStr)
      throws MalformedURLException {
    URL url = new URL(urlStr);
    WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(url.getPort()));
    WireMock.configureFor(url.getHost(), url.getPort());
    wireMockServer.start();
    return wireMockServer;
  }

  @Component
  public static class WireMockServerDestroyHelper {

    @Autowired
    private WireMockServer wireMockServer;

    @PreDestroy
    public void destroy() {
      wireMockServer.stop();
    }
  }
}
