package ro.hasna.tutorials.db_comparison.simulation;


import io.gatling.javaapi.core.Assertion;
import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import lombok.SneakyThrows;

import java.util.Properties;

import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.http.HttpDsl.http;

public abstract class BaseSimulation extends Simulation {

    protected final String baseUrl;

    @SneakyThrows
    public BaseSimulation() {
        Properties prop = new Properties();
        prop.load(BaseSimulation.class.getClassLoader()
                .getResourceAsStream("config.properties"));
        baseUrl = prop.getProperty("base-url");

        HttpProtocolBuilder httpProtocol = http
                .baseUrl(baseUrl)
                .acceptHeader("application/json");

        Assertion assertion = global().failedRequests().count().lt(1L);

        setUp(buildTest())
                .assertions(assertion)
                .protocols(httpProtocol);
    }

    public abstract PopulationBuilder buildTest();
}
