package by.madcat.development.f1newsreader.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import by.madcat.development.f1newsreader.Models.OnlinePost;
import by.madcat.development.f1newsreader.Models.RaceMode;
import by.madcat.development.f1newsreader.Models.TimingElement;

import static by.madcat.development.f1newsreader.Utils.DocParseUtils.*;

public class JsonParseUtils {

    public static RaceMode getRaceMode(String jsonString){
        RaceMode mode = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            mode = RaceMode.updateInstance(
                    jsonObject.getString(ONLINE_JSON_RACE_MODE),
                    jsonObject.getString(ONLINE_JSON_RACE_FLAG),
                    jsonObject.getInt(ONLINE_JSON_RACE_SAFETY_CAR),
                    jsonObject.getString(ONLINE_JSON_RACE_CURRENT_LAP),
                    jsonObject.getString(ONLINE_JSON_RACE_TOTAL_LAPS),
                    jsonObject.getInt(ONLINE_JSON_RACE_TRACK_TEMP),
                    jsonObject.getInt(ONLINE_JSON_RACE_AIR_TEMP),
                    jsonObject.getString(ONLINE_JSON_RACE_STATUS)
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mode;
    }

    public static List<OnlinePost> getPostsFromJsonString(String jsonString){
        LinkedList<OnlinePost> posts = new LinkedList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(ONLINE_JSON_ARRAY);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject currJsonObject = jsonArray.getJSONObject(i);
                posts.add(
                        new OnlinePost(
                                currJsonObject.getString(ONLINE_JSON_ELEMENT_MESSAGE),
                                currJsonObject.getString(ONLINE_JSON_ELEMENT_DATE)
                        )
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return posts;
    }

    public static int getSessionId(String jsonString){
        JSONObject jsonObject;
        int session_id = 0;
        try {
            jsonObject = new JSONObject(jsonString);
            session_id = jsonObject.getInt(ONLINE_JSON_SESSION_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return session_id;
    }

    public static List<TimingElement> getRaceTimings(String jsonString){
        LinkedList<TimingElement> timings = new LinkedList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(ONLINE_JSON_ARRAY_RACE);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject currJsonObject = jsonArray.getJSONObject(i);
                timings.add(
                        new TimingElement(
                                currJsonObject.getString(ONLINE_JSON_ARRAY_RACE_NAME),
                                String.valueOf(currJsonObject.getInt(ONLINE_JSON_ARRAY_RACE_POSITION)),
                                currJsonObject.getString(ONLINE_JSON_ARRAY_RACE_GAP),
                                currJsonObject.getString(ONLINE_JSON_ARRAY_RACE_PITS),
                                currJsonObject.getString(ONLINE_JSON_ARRAY_RACE_INTERVAL),
                                currJsonObject.getString(ONLINE_JSON_ARRAY_RACE_LASTLAP)
                        )
                );
            }
        } catch (JSONException e) {
        }

        return timings;
    }

    public static List<TimingElement> getPracticeTimings(String jsonString){
        LinkedList<TimingElement> timings = new LinkedList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(ONLINE_JSON_ARRAY_RACE);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject currJsonObject = jsonArray.getJSONObject(i);
                timings.add(
                        new TimingElement(
                                currJsonObject.getString(ONLINE_JSON_ARRAY_RACE_NAME),
                                String.valueOf(currJsonObject.getInt(ONLINE_JSON_ARRAY_RACE_POSITION)),
                                currJsonObject.getString(ONLINE_JSON_ARRAY_RACE_GAP),
                                currJsonObject.getString(ONLINE_JSON_ARRAY_RACE_LASTLAP),
                                currJsonObject.getString(ONLINE_JSON_ARRAY_RACE_PITS),
                                currJsonObject.getString(ONLINE_JSON_ARRAY_SESSION_BESTLAP)
                        )
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return timings;
    }

    public static OnlinePost getPostFromJsonString(String jsonString, int index){
        OnlinePost post = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(ONLINE_JSON_ARRAY);
            JSONObject currJsonObject = jsonArray.getJSONObject(0);

            post = new OnlinePost(
                    currJsonObject.getString(ONLINE_JSON_ELEMENT_MESSAGE),
                    currJsonObject.getString(ONLINE_JSON_ELEMENT_DATE)
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return post;
    }

    public static String putOnlinePostToJsonString(OnlinePost post){
        StringBuilder jsonString = new StringBuilder();

        jsonString.append("{");

        jsonString.append("\"messages\": [{\"id\": 1, \"tm\": \"");
        jsonString.append(post.getOnlinePostTime().toString());
        jsonString.append("\", \"msg\": \"");
        jsonString.append(post.getOnlinePostText().toString());
        jsonString.append("\"}]");

        jsonString.append("}");

        return jsonString.toString();
    }
}
