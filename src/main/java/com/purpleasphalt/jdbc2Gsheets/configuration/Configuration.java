package com.purpleasphalt.jdbc2Gsheets.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@org.springframework.context.annotation.Configuration
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class Configuration {
}
