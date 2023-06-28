package me.omega.omegalib.utils;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import me.omega.omegalib.OmegaLib;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class Http {

    public static Optional<Object> getUrlContent(@NonNull String url, boolean isList) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.connect();

        if (connection.getResponseCode() == 200) {
            Reader reader = new InputStreamReader(connection.getInputStream());
            if (isList) {
                Type listType = new TypeToken<List<JsonObject>>() {
                }.getType();
                return Optional.ofNullable(OmegaLib.getGson().fromJson(reader, listType));
            } else {
                return Optional.ofNullable(OmegaLib.getGson().fromJson(reader, JsonObject.class));
            }
        }

        connection.disconnect();
        return Optional.empty();
    }

}
