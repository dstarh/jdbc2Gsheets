package com.purpleasphalt.jdbc2Gsheets.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.google.common.collect.ImmutableList;

@Component
public class Connections {
    private static final Logger logger = LoggerFactory.getLogger(Connections.class);
    private ImmutableList<DatabaseConnection> connections;
    private Map<String, DataSource> dataSources;

    @PostConstruct
    public void init() {
        try {
            InputStream is = new ClassPathResource("connections.yaml").getInputStream();
            Yaml yaml = new Yaml();
            Map<String, List<Map<String, Object>>> configMap = yaml.load(is);
            List<DatabaseConnection> rawList = new ArrayList<>();
            List<Map<String, Object>> conns = configMap.get("connections");
            conns.forEach(conn -> {
                DatabaseConnection connection = new DatabaseConnection();
                connection.setUrl((String) conn.get("url"));
                connection.setDriverClass((String) conn.get("class"));
                connection.setName((String) conn.get("name"));
                connection.setUsername((String)conn.get("username"));
                connection.setPassword((String)conn.get("password"));
                rawList.add(connection);
            });
            connections = ImmutableList.copyOf(rawList);
            dataSources = new LinkedHashMap<>();
            connections.forEach(conn -> {
                DataSource ds = DataSourceBuilder.create()
                                 .driverClassName(conn.getDriverClass())
                                 .url(conn.getUrl())
                                 .username(conn.getUsername())
                                 .password(conn.getPassword())
                                 .build();

                dataSources.put(conn.getName(), ds);
            });
        } catch (IOException e) {
            logger.error("Couldn't load connection file", e);
        }
    }

    public Map<String, DataSource> getDataSources() {
        return dataSources;
    }
}
