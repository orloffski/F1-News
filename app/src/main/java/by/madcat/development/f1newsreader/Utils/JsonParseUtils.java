package by.madcat.development.f1newsreader.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import by.madcat.development.f1newsreader.dataInet.OnlinePost;

import static by.madcat.development.f1newsreader.Utils.DocParseUtils.ONLINE_JSON_ARRAY;
import static by.madcat.development.f1newsreader.Utils.DocParseUtils.ONLINE_JSON_ELEMENT_DATE;
import static by.madcat.development.f1newsreader.Utils.DocParseUtils.ONLINE_JSON_ELEMENT_MESSAGE;

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
