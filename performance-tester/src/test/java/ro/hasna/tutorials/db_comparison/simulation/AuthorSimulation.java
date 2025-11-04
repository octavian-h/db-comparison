package ro.hasna.tutorials.db_comparison.simulation;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.PopulationBuilder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ro.hasna.tutorials.db_comparison.util.AppConstants;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.core.CoreDsl.listFeeder;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

@Slf4j
@NoArgsConstructor
public class AuthorSimulation extends BaseSimulation {
    private static final String AUTHOR_ID = "authorId";

    @Override
    public PopulationBuilder buildTest() {
        List<Map<String, Object>> params = IntStream.range(0, 5)
                .mapToObj(i -> Map.of("authorName", (Object) ("an_" + i)))
                .toList();
        FeederBuilder<Object> feeder = listFeeder(params).circular();

        return scenario("Test Create on Author API")
                .feed(feeder)
                .exec(http("Create author")
                        .post(AppConstants.AUTHOR_API_PATH)
                        .body(ElFileBody("createAuthorRequest.json"))
                        .asJson()
                        .check(status().is(201), jsonPath("$.id").saveAs(AUTHOR_ID)))
                .exitHereIfFailed()
                .exec(http("Read all authors with a given name")
                        .get(AppConstants.AUTHOR_API_PATH)
                        .queryParam("name", "#{authorName}")
                        .asJson()
                        .check(status().is(200), jsonPath("$").count()
                                .is(1)))
                .exitHereIfFailed()
                .injectOpen(
                        atOnceUsers(100)
//                        constantUsersPerSec(100).during(Duration.ofSeconds(10))
                );
    }
}
