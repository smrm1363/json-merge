package mohammadreza.miral.jsonMerger.domain.jsonMerge;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServlet;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * This class is Rest  Api for testing the result of the merging
 */
@RestController
@RequestMapping(path = "/json-merge")
@Api
public class JsonMergeRestApi extends HttpServlet {
    private final JsonMergeService jsonMergeService;
    @Autowired
    public JsonMergeRestApi(JsonMergeService jsonMergeService) {
        this.jsonMergeService = jsonMergeService;
    }

    /**
     *
     * @param usedId is ID of the user in the Json APIs.
     * @return a merged user info
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws JsonProcessingException
     */
    @GetMapping("/{id}")
    public Map getMergedJsons(@PathVariable("id") String usedId) throws InterruptedException, ExecutionException, JsonProcessingException {
        return jsonMergeService.getMergedUserAndPosts(usedId);
    }

}
