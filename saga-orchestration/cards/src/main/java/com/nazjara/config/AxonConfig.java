package com.nazjara.config;

import com.thoughtworks.xstream.XStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

  @Bean
  public XStream xStream() {
    var xStream = new XStream();
    xStream.allowTypesByWildcard(new String[] {"com.nazjara.**"});
    return xStream;
  }
}
