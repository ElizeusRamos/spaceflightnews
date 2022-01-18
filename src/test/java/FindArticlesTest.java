
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty","json:target/cucumber.json"},
        features = "classpath:features",
        tags = "@FindArticlesTest",
        glue = "passos.articles",
        monochrome = true,
        dryRun = false
)
public class FindArticlesTest {
}
