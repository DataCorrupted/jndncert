package net.named_data.jndncert.common;

import net.named_data.jndn.Name;

import javax.json.*;
import java.util.ArrayList;

public class JsonHelper {
    static final public String JSON_IDENTIFIER = "identifier";
    static final public String JSON_CA_INFO = "ca-info";
    static final public String JSON_STATUS = "status";
    static final public String JSON_REQUEST_ID = "request-id";
    static final public String JSON_CHALLENGES = "challenges";
    static final public String JSON_CHALLENGE_TYPE = "challenge-type";
    static final public String JSON_FAILURE_INFO = "failure-info";
    static final public String JSON_CERTIFICATE = "certificate";

    static public JsonObject genProbeResponseJson(
            Name identifier, Name caInfo
    ){
        return Json.createObjectBuilder()
                .add(JSON_IDENTIFIER, identifier.toUri())
                .add(JSON_CA_INFO, caInfo.toUri())
                .build();
    }

    static public JsonObject genNewResponseJson(
            String requestId, String status, ArrayList<String> challenges
    ){
        JsonObjectBuilder objBuilder = Json.createObjectBuilder();
        objBuilder = objBuilder
                .add(JSON_REQUEST_ID, requestId)
                .add(JSON_STATUS, status);
        JsonArrayBuilder array = Json.createArrayBuilder();
        for (String c: challenges){
            array = array.add(Json.createObjectBuilder()
                    .add(JSON_CHALLENGE_TYPE, c));
        }
        objBuilder = objBuilder.add(JSON_CHALLENGES, array);
        return objBuilder.build();
    }

    static public JsonObject genChallengeResponseJson(
            String requestId, String challengeType, String status
    ){
        return genChallengeResponseJson(
                requestId, challengeType, status, new Name());
    }
    static public JsonObject genChallengeResponseJson(
            String requestId, String challengeType,
            String status, Name name
    ){
        JsonObjectBuilder objBuilder = Json.createObjectBuilder();
        objBuilder = objBuilder
                .add(JSON_REQUEST_ID, requestId)
                .add(JSON_CHALLENGE_TYPE, challengeType)
                .add(JSON_STATUS, status);
        // TODO: This is weird, new Name() gets me "/"
        if (! name.toUri().equals("/") || ! name.toUri().isEmpty()){
            objBuilder = objBuilder.add(JSON_CERTIFICATE, name.toUri());
        }
        return objBuilder.build();
    }

    static public JsonObject genFailureJson(
            String requestId, String challengeType,
            String status, String failureInfo
    ){
        return Json.createObjectBuilder()
                .add(JSON_REQUEST_ID, requestId)
                .add(JSON_CHALLENGE_TYPE, challengeType)
                .add(JSON_STATUS, status)
                .add(JSON_FAILURE_INFO, failureInfo)
                .build();
    }
}
