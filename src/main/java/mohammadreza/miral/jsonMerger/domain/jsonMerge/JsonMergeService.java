package mohammadreza.miral.jsonMerger.domain.jsonMerge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * This is responsible for our business logic of merging the Jsons. The logic is Async
 */
@Service
public class JsonMergeService {
    private final String userDataUrl;
    private final String userPostUrl;

    public JsonMergeService(@Value( "${user.data.url}" )String userDataUrl,@Value( "${user.post.url}" ) String userPostUrl) {
        this.userDataUrl = userDataUrl;
        this.userPostUrl = userPostUrl;
    }

    /**
     * Read the Json data from the two provided APIs, then merge them into a result
     * @param userId
     * @return the merges data of user and user posts
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws JsonProcessingException
     */
    public Map<String,Object> getMergedUserAndPosts(String userId) throws ExecutionException, InterruptedException, JsonProcessingException {
        HttpClient restClient = HttpClient.newHttpClient();
        /**
         * call the the first source API async
         */
        CompletableFuture<HttpResponse<String>> userDataFuture= restClient.sendAsync(HttpRequest.newBuilder()
                .uri(URI.create(userDataUrl+userId))
                .timeout(Duration.ofMinutes(1))
                .build(), HttpResponse.BodyHandlers.ofString());
        /**
         * call the the second source API async
         */
        CompletableFuture<HttpResponse<String>> userPostDataFuture= restClient.sendAsync(HttpRequest.newBuilder()
                .uri(URI.create(userPostUrl+userId))
                .timeout(Duration.ofMinutes(1))
                .build(), HttpResponse.BodyHandlers.ofString());
        /**
         * Waiting for result of all async calls
         */
        CompletableFuture.allOf(userDataFuture,userPostDataFuture).join();
        ObjectMapper mapper = new ObjectMapper();
        /**
         * Merging the results
         */
        Map<String,Object> mappedUser = mapper.readValue(userDataFuture.get().body(), Map.class);
        List<Map<String,Object>> mappedUserPostList = mapper.readValue(userPostDataFuture.get().body(), List.class);
        return mergeJsons(mappedUser,mappedUserPostList,"userId","posts");
    }

    /**
     * Responsible for merge the Jsons
     * @param mainMap should be a Map
     * @param listToBeMerged Should be a list that wanted be merged in the mainMap
     * @return
     */
    public Map<String,Object> mergeJsons(final Map<String,Object> mainMap,final List<Map<String,Object>> listToBeMerged,String matchingKey,String toBeMergedListName)
    {
        listToBeMerged.forEach(stringObjectMap -> stringObjectMap.remove(matchingKey) );
        mainMap.put(toBeMergedListName,listToBeMerged);
        return mainMap;
    }
}
