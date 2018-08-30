package net.named_data.jndncert.challenge;

import org.json.JSONObject;

import java.util.ArrayList;

public class ChallengeCredential extends ChallengeModule {
    // TODO: Fill this class
    protected static final String CHALLENGE_TYPE = "Credential";
    public ArrayList<String> getSelectRequirements(){
        return null;
    }
    public ArrayList<String> getValidateRequirements(String status){
        return null;
    }
    public JSONObject doGenSelectParamsJson(
            String status, ArrayList<String> paramList){
        return null;
    }
    public JSONObject doGenValidateParamsJson(
            String status, ArrayList<String> paramList){
        return null;
    }
}
