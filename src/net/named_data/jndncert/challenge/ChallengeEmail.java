package net.named_data.jndncert.challenge;

import javax.json.JsonObject;
import java.util.ArrayList;

public class ChallengeEmail extends ChallengeModule {
    // TODO: Fill this class
    protected static final String CHALLENGE_TYPE = "Email";
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
