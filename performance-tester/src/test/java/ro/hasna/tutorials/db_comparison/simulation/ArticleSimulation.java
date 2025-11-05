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
public class ArticleSimulation extends BaseSimulation {

    private static final String ARTICLE_ID = "articleId";
    private static final String ARTICLE_ID_PATH = "/#{" + ARTICLE_ID + "}";


    @Override
    public PopulationBuilder buildTest() {
        List<Map<String, Object>> params = IntStream.range(0, 10000)
                .mapToObj(i -> Map.of("authorName", (Object) ("an_" + (i % 100))))
                .toList();
        FeederBuilder<Object> feeder = listFeeder(params).circular();

        return scenario("Test CRUD on Article API")
                .feed(feeder)
                .exec(http("Create article")
                        .post(AppConstants.ARTICLES_API_PATH)
                        .body(ElFileBody("createArticleRequest.json"))
                        .asJson()
                        .check(status().is(201), jsonPath("$.id").saveAs(ARTICLE_ID)))
                .exitHereIfFailed()
                .exec(http("Read all articles")
                        .get(AppConstants.ARTICLES_API_PATH)
                        .asJson()
                        .check(status().is(200), jsonPath("$").count()
                                .gte(1)))
                .exitHereIfFailed()
                .exec(http("Read created article")
                        .get(AppConstants.ARTICLES_API_PATH + ARTICLE_ID_PATH)
                        .asJson()
                        .check(status().is(200)))
                .exitHereIfFailed()
                .exec(http("Delete article")
                        .delete(AppConstants.ARTICLES_API_PATH + ARTICLE_ID_PATH)
                        .asJson()
                        .check(status().is(204)))
                .exitHereIfFailed()
                .injectOpen(
                        atOnceUsers(5000)
//                        constantUsersPerSec(1000).during(Duration.ofSeconds(10))
                );

    }
}
