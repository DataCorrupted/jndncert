package net.named_data.jndncert;

import net.named_data.jndn.Face;
import net.named_data.jndn.security.KeyChain;
import net.named_data.jndncert.challenge.ChallengeFactory;
import net.named_data.jndncert.challenge.ChallengeModule;
import net.named_data.jndncert.client.ClientModule;
import net.named_data.jndncert.client.RequestState;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.Scanner;

public class JNdnCertClient {
    private int stepCnt = 0;
    private ClientModule client;
    static private final Scanner scanner = new Scanner(System.in);

    public JNdnCertClient(ClientModule c){
        client = c;
    }

    private void stepGuide(String info){
        System.err.println("Step " + stepCnt + ": " + info);
        stepCnt ++;
    }

    private ClientModule.ErrorCallback errCb =
            (errInfo -> System.err.println("Error: " + errInfo));

    private ClientModule.RequestCallback downloadCb = (state -> {
        stepGuide("Done! " +
                "Certificate has already been installed to local keychain."
        );
    });

    // Cannot use lambda expression as there is a self-referencing here
    // When the validation fails another validation will be initiated
    // and thus using self as a callback function.
    private ClientModule.RequestCallback validateCb =
            new ClientModule.RequestCallback() {
        @Override
        public void onRequest(RequestState state) {
            if (state.m_status.equals(ChallengeModule.SUCCESS)){
                System.err.println("Done! Certificate has already been issued.");
                client.requestDownload(state, downloadCb, errCb);
                return;
            }
            // Handle failed case. Doing the valid again.
            // Gen challenge requirement
            ChallengeModule challenge =
                    ChallengeFactory.getChallenge(state.m_challengeType);
            ArrayList<String> requirementList =
                    challenge.getRequirementForValidate(state.m_status);

            // Gather info to validate
            stepGuide("Please satisfy following instruction(s)");
            ArrayList<String> paramList = new ArrayList<>();
            for (String str: requirementList){
                System.err.println("\t" + str);
                paramList.add(scanner.nextLine());
            }

            // Form a JSON
            JsonObject paramJson =
                    challenge.doGenValidateParamsJson(state.m_status, paramList);

            // send Validate interest
            client.sendValidate(state, paramJson, validateCb, errCb);
        }
    };

    private ClientModule.RequestCallback selectCb = (state -> {
        // Gen challenge requirement
        ChallengeModule challenge =
                ChallengeFactory.getChallenge(state.m_challengeType);
        ArrayList<String> requirementList =
                challenge.getRequirementForValidate(state.m_status);

        // Gather info to validate
        stepGuide("Please satisfy following instruction(s)");
        ArrayList<String> paramList = new ArrayList<>();
        for (String str: requirementList){
            System.err.println("\t" + str);
            paramList.add(scanner.nextLine());
        }

        // Form a JSON
        JsonObject paramJson =
                challenge.doGenValidateParamsJson(state.m_status, paramList);

        // send Validate interest
        client.sendValidate(state, paramJson, validateCb, errCb);
    });

    public ClientModule.RequestCallback newCb = (state -> {
        stepGuide("Please select one challenge from following types\n");
        for (String str: state.m_challengeList){
            System.err.println("\t" + str);
        }
        String choice = scanner.nextLine();
        ChallengeModule challenge = ChallengeFactory.getChallenge(choice);

        ArrayList<String> requirementList =
                challenge.getRequirementForSelection();
        ArrayList<String> paramList = new ArrayList<>();
        if (requirementList.size() != 0){
            stepGuide("Please fill answer following questions: ");
            for (String str: requirementList){
                System.err.println("\t" + str);
                paramList.add(scanner.nextLine());
            }
        }
        JsonObject paramJson =
                challenge.genSelectParamsJson(state.m_status, paramList);
        client.sendSelect(state, choice, paramJson, validateCb, errCb);
    });

    static public void main(String[] args) throws Exception {
        // TODO: Change this for more robust case.
        String configFilePath = "res/test/client.conf.test";

        ClientModule client = new ClientModule(new Face(), new KeyChain());
        client.getClientConfig().load(configFilePath);
        JNdnCertClient certClient = new JNdnCertClient(client);


    }
}
