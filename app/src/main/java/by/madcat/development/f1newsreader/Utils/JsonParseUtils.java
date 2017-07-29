package by.madcat.development.f1newsreader.Utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import by.madcat.development.f1newsreader.dataInet.Models.OnlinePost;
import by.madcat.development.f1newsreader.dataInet.Models.TimingElement;

import static by.madcat.development.f1newsreader.Utils.DocParseUtils.ONLINE_JSON_ARRAY;
import static by.madcat.development.f1newsreader.Utils.DocParseUtils.ONLINE_JSON_ARRAY_SESSION;
import static by.madcat.development.f1newsreader.Utils.DocParseUtils.ONLINE_JSON_ARRAY_SESSION_BESTLAP;
import static by.madcat.development.f1newsreader.Utils.DocParseUtils.ONLINE_JSON_ARRAY_SESSION_GAP;
import static by.madcat.development.f1newsreader.Utils.DocParseUtils.ONLINE_JSON_ARRAY_SESSION_LASTLAP;
import static by.madcat.development.f1newsreader.Utils.DocParseUtils.ONLINE_JSON_ARRAY_SESSION_NAME;
import static by.madcat.development.f1newsreader.Utils.DocParseUtils.ONLINE_JSON_ARRAY_SESSION_PITS;
import static by.madcat.development.f1newsreader.Utils.DocParseUtils.ONLINE_JSON_ARRAY_SESSION_POSITION;
import static by.madcat.development.f1newsreader.Utils.DocParseUtils.ONLINE_JSON_ELEMENT_DATE;
import static by.madcat.development.f1newsreader.Utils.DocParseUtils.ONLINE_JSON_ELEMENT_MESSAGE;
import static by.madcat.development.f1newsreader.Utils.DocParseUtils.ONLINE_JSON_SESSION_ID;

public class JsonParseUtils {
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

    public static List<TimingElement> getTimingsFromJsonString(String jsonString){
        LinkedList<TimingElement> timings = new LinkedList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(ONLINE_JSON_ARRAY_SESSION);

            Log.d("test", "json array lenght: " + jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject currJsonObject = jsonArray.getJSONObject(i);
                timings.add(
                        new TimingElement(
                                currJsonObject.getString(ONLINE_JSON_ARRAY_SESSION_NAME),
                                currJsonObject.getInt(ONLINE_JSON_ARRAY_SESSION_POSITION),
                                currJsonObject.getString(ONLINE_JSON_ARRAY_SESSION_GAP),
                                currJsonObject.getString(ONLINE_JSON_ARRAY_SESSION_BESTLAP),
                                currJsonObject.getString(ONLINE_JSON_ARRAY_SESSION_PITS),
                                currJsonObject.getInt(ONLINE_JSON_ARRAY_SESSION_LASTLAP)
                        )
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("test", "parse error");
        }

        Log.d("test", "timings in parse: " + timings.size());

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
