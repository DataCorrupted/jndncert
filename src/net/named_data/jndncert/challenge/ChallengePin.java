package net.named_data.jndncert.challenge;

import javax.json.JsonObject;
import java.util.ArrayList;

public class ChallengePin extends ChallengeModule {
    protected static final String CHALLENGE_TYPE = "Pin";
    public ArrayList<String> genSelectRequirements(){
        return null;
    }
    public ArrayList<String> genValidateRequirements(){
        return null;
    }
    public JsonObject doGenSelectParamsJson(
            String status, ArrayList<String> paramList){
        return null;
    }
    public JsonObject doGenValidateParamsJson(
            String status, ArrayList<String> paramList){
        return null;
    }
}
