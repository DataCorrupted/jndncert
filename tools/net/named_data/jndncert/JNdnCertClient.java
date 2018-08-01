package net.named_data.jndncert;

import net.named_data.jndn.Face;
import net.named_data.jndn.security.KeyChain;
import net.named_data.jndncert.challenge.ChallengeModule;
import net.named_data.jndncert.client.ClientModule;

import javax.json.Json;
import javax.json.JsonObject;

public class JNdnCertClient {
    private int stepCnt = 0;
    private ClientModule client;

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
    private ClientModule.RequestCallback validateCb = (state -> {
        if (state.m_status.equals(ChallengeModule.SUCCESS)){
            System.err.println("Done! Certificate has already been issued.");
            client.requestDownload(state, downloadCb, errCb);
            return;
        }
        // TODO: Handle failed case. Doing the valid again.
        // In ndncert, the code below is the same with selectCb.
        // Consider reuse it.

    });

    private ClientModule.RequestCallback selectCb = (state -> {
        // TODO: Gen challenge requirement

        stepGuide("Please satisfy following instruction(s)");
        // TODO: Gather info to validate

        // TODO: Form a JSON

        // TODO: send Validate interest

    });

    public ClientModule.RequestCallback newCb = (state -> {
        stepGuide("Please select one challenge from following types\n");
        for (String c: state.m_challengeList){
            System.err.println("\t" + c);
        }
        // TODO: Input choice
        String choice = "";

        // TODO: Using choice generate a param json
        JsonObject paramJson = Json.createObjectBuilder()
                .add("empty", "").build();

        // Send SELECT interest.
        client.sendSelect(state, choice, paramJson, selectCb, errCb);
    });

    static public void main(String[] args) throws Exception {
        // TODO: Change this for more robust case.
        String configFilePath = "res/test/client.conf.test";

        ClientModule client = new ClientModule(new Face(), new KeyChain());
        client.getClientConfig().load(configFilePath);
        JNdnCertClient certClient = new JNdnCertClient(client);

        // TODO: initiate a NEW.
    }
}
