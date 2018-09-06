package net.named_data.jndncert.challenge;

import org.json.JSONObject;

import java.util.ArrayList;

public interface ChallengeModule {
    String WAIT_SELECTION = "wait-selection";
    String SUCCESS = "success";
    String PENDING = "pending";
    String FAILURE = "failure";

    String getChallengeType();
    ArrayList<String> getSelectRequirements();
    ArrayList<String> getValidateRequirements(String status);
    JSONObject genSelectParamsJson(
            String status, ArrayList<String> paramList);
    JSONObject genValidateParamsJson(
            String status, ArrayList<String> paramList);
}
