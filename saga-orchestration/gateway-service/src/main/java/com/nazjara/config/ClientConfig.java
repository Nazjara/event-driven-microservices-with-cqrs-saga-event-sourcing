package com.nazjara.config;

import com.nazjara.client.CustomerSummaryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfig {

  @Value("${app.base-url}")
  private String baseUrl;

  @Bean
  CustomerSummaryClient customerClient() {
    var webClient = WebClient.builder().baseUrl(baseUrl).build();
    var adapter = WebClientAdapter.create(webClient);
    var factory = HttpServiceProxyFactory.builderFor(adapter).build();
    return factory.createClient(CustomerSummaryClient.class);
  }
}
