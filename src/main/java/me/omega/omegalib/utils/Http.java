package me.omega.omegalib.utils;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import me.omega.omegalib.Lib;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

/**
 * A utility class for HTTP requests.
 */
@UtilityClass
public class Http {

    /**
     * Retrieves the content of a URL as a JSON object or list, depending on the value of the boolean parameter.
     *
     * @param tempUrl the URL to retrieve content from
     * @param isList  a boolean indicating whether the content should be parsed as a list or as a single object
     * @return an Optional containing the parsed content, or an empty Optional if the response code is not 200
     * @throws IOException if an I/O exception occurs while making the request
     */
    public static Optional<Object> getUrlContent(@NonNull String tempUrl, boolean isList) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(tempUrl).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.connect();

        if (connection.getResponseCode() == 200) {
            Reader reader = new InputStreamReader(connection.getInputStream());
            if (isList) {
                Type listType = new TypeToken<List<JsonObject>>() {
                }.getType();
                return Optional.ofNullable(Lib.getGson().fromJson(reader, listType));
            } else {
                return Optional.ofNullable(Lib.getGson().fromJson(reader, JsonObject.class));
            }
        }

        connection.disconnect();
        return Optional.empty();
    }

}
