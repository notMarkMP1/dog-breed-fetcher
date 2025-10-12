package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) {
        final Request request = new Request.Builder().url(String.format("https://dog.ceo/api/breed/%s/list", breed)).build();
        try {
            final Response response = client.newCall(request).execute();
            final JSONObject jsonObject = new JSONObject(response.body().string());
            final JSONArray jsonSubBreeds = jsonObject.getJSONArray("message");
            List<String> subBreeds = new ArrayList<>();
            for (int i = 0; i < jsonSubBreeds.length(); i++) {
                subBreeds.add(jsonSubBreeds.getString(i));
            }
            return subBreeds;

        } catch (IOException event) {
            throw new RuntimeException(event);
        }
    }
}