package ro.hasna.tutorials.db_comparison.simulation;


import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import lombok.SneakyThrows;

import java.util.Properties;

import static io.gatling.javaapi.http.HttpDsl.http;

public abstract class BaseSimulation extends Simulation {

    protected final String baseUrl;
    protected final HttpProtocolBuilder httpProtocol;

    @SneakyThrows
    public BaseSimulation() {
        Properties prop = new Properties();
        prop.load(BaseSimulation.class.getClassLoader().getResourceAsStream("config.properties"));
        baseUrl = prop.getProperty("base-url");
        httpProtocol = http.baseUrl(baseUrl).acceptHeader("application/json");

        runTest();
    }

    public abstract void runTest();
}
