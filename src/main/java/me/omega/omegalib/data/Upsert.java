package me.omega.omegalib.data;

import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;

public class Upsert {

    public static final UpdateOptions UPDATE_OPTIONS = new UpdateOptions().upsert(true);
    public static final ReplaceOptions REPLACE_OPTIONS = new ReplaceOptions().upsert(true);

}
