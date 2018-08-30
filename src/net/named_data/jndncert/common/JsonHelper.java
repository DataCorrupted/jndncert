package net.named_data.jndncert.common;

import net.named_data.jndn.Name;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

public class JsonHelper {
    static final public String JSON_IDENTIFIER = "identifier";
    static final public String JSON_CA_INFO = "ca-info";
    static final public String JSON_STATUS = "status";
    static final public String JSON_REQUEST_ID = "request-id";
    static final public String JSON_CHALLENGES = "challenges";
    static final public String JSON_CHALLENGE_TYPE = "challenge-type";
    static final public String JSON_FAILURE_INFO = "failure-info";
    static final public String JSON_CERTIFICATE = "certificate";
    static final private Logger log = Logger.getLogger("JsonHelper");

    static public JSONObject genProbeResponseJson(
            Name identifier, Name caInfo
    ){
        JSONObject obj = new JSONObject();
        try{
            obj.put(JSON_IDENTIFIER, identifier.toUri())
               .put(JSON_CA_INFO, caInfo.toUri());
        } catch (JSONException e){
            log.warning(e.getMessage());
        }
        return obj;
    }

    static public JSONObject genNewResponseJson(
            String requestId, String status, ArrayList<String> challenges
    ){
        JSONObject obj = new JSONObject();
        try {
            obj.put(JSON_REQUEST_ID, requestId)
                    .put(JSON_STATUS, status);
            JSONArray array = new JSONArray();
            for (String c : challenges) {
                array = array.put(
                        new JSONObject()
                                .put(JSON_CHALLENGE_TYPE, c)
                );
            }
            obj.put(JSON_CHALLENGES, array);
        } catch (JSONException e){
            log.warning(e.getMessage());
        }
        return obj;
    }

    static public JSONObject genChallengeResponseJson(
            String requestId, String challengeType, String status
    ){
        return genChallengeResponseJson(
                requestId, challengeType, status, new Name());
    }
    static public JSONObject genChallengeResponseJson(
            String requestId, String challengeType,
            String status, Name name
    ){
        JSONObject obj = new JSONObject();
        try{
            obj.put(JSON_REQUEST_ID, requestId)
               .put(JSON_CHALLENGE_TYPE, challengeType)
               .put(JSON_STATUS, status);
            // TODO: This is weird, new Name() gets me "/"
            if (! name.toUri().equals("/") && ! name.toUri().isEmpty()){
                obj.put(JSON_CERTIFICATE, name.toUri());
        }} catch (JSONException e){
            log.warning(e.getMessage());
        }
        return obj;
    }

    static public JSONObject genFailureJson(
            String requestId, String challengeType,
            String status, String failureInfo
    ){
        JSONObject obj = new JSONObject();
        try{
            obj.put(JSON_REQUEST_ID, requestId)
               .put(JSON_CHALLENGE_TYPE, challengeType)
               .put(JSON_STATUS, status)
               .put(JSON_FAILURE_INFO, failureInfo);
        } catch (JSONException e){
            log.warning(e.getMessage());
        }
        return obj;
    }
    static public JSONObject string2Json(String str){
        JSONObject obj = new JSONObject();
        try{
            obj = new JSONObject(str);
        } catch (JSONException e){
            log.warning(e.getMessage());
        }
        return obj;
    }
}
