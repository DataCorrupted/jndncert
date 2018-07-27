package net.named_data.jndncert.client;

import net.named_data.jndn.*;
import net.named_data.jndn.security.KeyChain;
import net.named_data.jndn.security.SigningInfo;
import net.named_data.jndn.security.ValidityPeriod;
import net.named_data.jndn.security.VerificationHelpers;
import net.named_data.jndn.security.pib.Pib;
import net.named_data.jndn.security.pib.PibIdentity;
import net.named_data.jndn.security.v2.CertificateV2;
import net.named_data.jndn.util.Blob;
import net.named_data.jndncert.common.JsonHelper;

import javax.json.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static net.named_data.jndn.encoding.tlv.Tlv.SignatureInfo;


public class ClientModule {
    // TODO: Fill this class.
    protected ClientConfig m_config;
    protected Face m_face;
    protected KeyChain m_keyChain;
    protected int m_retryTimes;
    private final Logger log = Logger.getLogger("ClientModule");
    // Constructor
    // Java has no default function arguments, using function
    // overload to achieve that.
    public ClientModule(Face face, KeyChain keyChain){
        this(face, keyChain, 2);
    }
    public ClientModule(Face face, KeyChain keyChain, int retryTimes){
        m_face = face;
        m_retryTimes = retryTimes;
        m_keyChain = keyChain;
    }

    // Interfaces used to define callback functions.
    public interface LocalhostListCallback{
        void onLocalhostList(ClientConfig clientConfig);
    }
    public interface ListCallback{
        void onList(ArrayList<Name> caList, Name assignedName, Name schema);
    }
    public interface RequestCallback{
        void onRequest(RequestState state);
    }
    public interface ErrorCallback{
        void onError(String errInfo);
    }

    public void reuqestCaTrustAnchor(
            Name caName,
            OnData trustAnchorCb, ErrorCallback errorCb
    ){
        // Unlike C++, java creates nothing but a reference,
        // to guarantee that the original data is unchanged,
        // we use provided copy constructor.
        Name interestName = new Name(caName);
        interestName.append("CA").append("_DOWNLOAD").append("ANCHOR");
        Interest interest = new Interest(interestName);
        interest.setMustBeFresh(true);
        try {
            m_face.expressInterest(
                    interest,
                    trustAnchorCb,
                    getTimeoutCallbackFunc(m_retryTimes, trustAnchorCb, errorCb),
                    getNetworkNackCallbackFunc(errorCb)
            );
        } catch (IOException e){
            log.warning(e.getMessage());
        }
    }

    public void requestLocalhostList(
            LocalhostListCallback listCb, ErrorCallback errorCb){
        Interest interest = new Interest(new Name("/localhost/CA/_LIST"));
        interest.setMustBeFresh(true);
        OnData onData = (
                (i, d) -> handleLocalhostListResponse(i, d, listCb, errorCb)
        );
        try{
            m_face.expressInterest(
                    interest,
                    onData,
                    getTimeoutCallbackFunc(m_retryTimes, onData, errorCb),
                    getNetworkNackCallbackFunc(errorCb)
            );
        } catch (IOException e){
            log.warning(e.getMessage());
        }
    }

    // TODO: We are not doing this now. Let's focus on what matters first.
    public void handleLocalhostListResponse(
            Interest request, Data reply,
            LocalhostListCallback listCb, ErrorCallback errorCb
    ){
        ;
    }

    public void requestList(
            ClientCaItem ca, String additionalInfo,
            ListCallback listCb, ErrorCallback errorCb
    ){
        Name request = new Name(ca.m_caName);
        request.append("_LIST");
        if (! additionalInfo.equals("")){
            request.append(additionalInfo);
        }
        Interest interest = new Interest(request);
        interest.setMustBeFresh(true);
        OnData onData = ((i, d) -> handleListResponse(i, d, ca, listCb, errorCb));

        try{
            m_face.expressInterest(
                    interest,
                    onData,
                    getTimeoutCallbackFunc(m_retryTimes, onData, errorCb),
                    getNetworkNackCallbackFunc(errorCb)
            );
        } catch (IOException e){
            log.warning(e.getMessage());
        }
    }

    // TODO: We are not doing this now. Let's focus on what matters first.
    public void handleListResponse(
            Interest request, Data reply, ClientCaItem ca,
            ListCallback listCb, ErrorCallback errorCb
    ){
        ;
    }

    // TODO: We are not doing this now. Let's focus on what matters first.
    public void sendProbe(
            ClientCaItem ca, String probInfo,
            RequestCallback requestCb, ErrorCallback errorCb
    ){
        ;
    }

    // TODO: We are not doing this now. Let's focus on what matters first.
    public void handleProbeResponse(
            Interest request, Data reply, RequestState state,
            RequestCallback requestCb, ErrorCallback errorCb){
        ;
    }

    public void sendNew(
            ClientCaItem ca, Name identityName,
            RequestCallback requestCb, ErrorCallback errorCb){
        Pib pib = m_keyChain.getPib();
        RequestState state = new RequestState();
        try{
            try{
                PibIdentity id = pib.getIdentity(identityName);
                state.m_key = m_keyChain.createKey(id);
            } catch (Pib.Error e){
                PibIdentity id = m_keyChain.createIdentityV2(identityName);
                state.m_key = id.getDefaultKey();
            }
        } catch (Exception e){
            log.info(e.getMessage());
        }
        state.m_ca = ca;
        state.m_isInstalled = false;

        CertificateV2 certRequest = new CertificateV2();
        certRequest.setName(
                new Name(state.m_key.getName())
                .append("cert-request")
                .appendVersion(2)
        );
        // TODO: I think you can use KeyChain.selfSign(PibKey) here.
        // Let's discuss this later and focus on the functionality now.
        certRequest.setContent(state.m_key.getPublicKey());
        MetaInfo metaInfo = new MetaInfo();
        metaInfo.setFreshnessPeriod((double) TimeUnit.DAYS.toMillis(1));
        certRequest.setMetaInfo(metaInfo);
        SigningInfo signInfo = new SigningInfo(state.m_key);
        signInfo.setValidityPeriod(
                new ValidityPeriod(
                        System.currentTimeMillis()
                                + TimeUnit.DAYS.toMillis(0),
                        System.currentTimeMillis()
                                + TimeUnit.DAYS.toMillis(10)));
        try {
            m_keyChain.sign(certRequest, signInfo);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        Interest interest = new Interest(
                new Name(ca.m_caName)
                        .append(new Name("_NEW"))
                        .append(certRequest.wireEncode())
        );
        try{
            m_keyChain.sign(interest, new SigningInfo(state.m_key));
        } catch (Exception e){
            log.info(e.getMessage());
        }
        OnData onData = ((i, d) -> handleNewResponse(i, d, state, requestCb, errorCb));
        try{
            m_face.expressInterest(
                    interest,
                    onData,
                    getTimeoutCallbackFunc(m_retryTimes, onData, errorCb),
                    getNetworkNackCallbackFunc(errorCb));
        } catch (IOException e){
            log.warning(e.getMessage());
        }
        // TODO: NDN_LOG_INFO needed.

    }

    public void handleNewResponse(
            Interest request, Data reply, RequestState state,
            RequestCallback requestCb, ErrorCallback errorCb
    ){
        // Verification failed.
        if (!VerificationHelpers.verifyDataSignature(
                reply, state.m_ca.m_anchor)){
            errorCb.onError(
                    "Cannot verify data from " +
                    state.m_ca.m_caName.toUri()
            );
            return;
        }

        JsonObject obj = getJsonFromData(reply);
        state.m_status = obj.getString(JsonHelper.JSON_STATUS, "");
        state.m_requestId = obj.getString(JsonHelper.JSON_REQUEST_ID, "");

        // Status failed.
        if (!checkStatus(state, obj, errorCb)){
            return;
        }

        JsonArray challenges = obj.getJsonArray(JsonHelper.JSON_CHALLENGES);
        ArrayList<String> challengeList = new ArrayList<>();
        for (int idx = 0; idx < challenges.size(); idx++){
            JsonObject o = challenges.getJsonObject(idx);
            challengeList.add(o.getString(JsonHelper.JSON_CHALLENGE_TYPE));
        }
        state.m_challengeList = challengeList;
        requestCb.onRequest(state);
        // TODO: NDN_LOG_INFO needed.
    }

    public void sendSelect(
            RequestState state, String challengeType, JsonObject selectParam,
            RequestCallback requestCb, ErrorCallback errorCb
    ){
        ;
    }

    public void handleSelectResponse(
            Interest request, Data reply, RequestState state,
            RequestCallback requestCb, ErrorCallback errorCb
    ){
        ;
    }

    public void sendValidate(
            RequestState state, JsonObject validateParam,
            RequestCallback requestCb, ErrorCallback errorCb
    ){
        ;
    }

    public void handleStatusResponse(
            Interest request, Data reply, RequestState state,
            RequestCallback requestCb, ErrorCallback errorCb
    ){
        ;
    }

    public void requestDownload(
            RequestState state,
            RequestCallback requestCb, ErrorCallback errorCb
    ){
        ;
    }

    public void handleDownloadResponse(
            Interest request, Data reply, RequestState state,
            RequestCallback requestCb, ErrorCallback errorCb
    ){
        ;
    }

    // Helper functions
    public ClientConfig getClientConfig() { return m_config; }

    // TODO: Consult Zhiyi how to do Unit Test on it.
    // This is an important function. I don't want to get it wrong.
    public JsonObject getJsonFromData(Data data) {
        String jsonString = data.getContent().toString();
        InputStream inputStrStream
                = new ByteArrayInputStream(jsonString.getBytes());
        JsonReader reader = Json.createReader(inputStrStream);
        return reader.readObject();
    }

    // TODO: Also have to consult Zhiyi about Unit Test.
    public Blob nameBlockFromJson(JsonObject obj){
        String str = obj.toString();
        return new Blob(str);
    }

    // TODO: To be tested.
    public final Boolean checkStatus(
            RequestState state, JsonObject json,
            ErrorCallback errorCb
    ){
        // TODO: Change "failure" To ChallengeModule.FAILURE later.
        // There is not ChallengeModule yet.
        if (state.m_status.equals("failure")){
            errorCb.onError(
                    json.getString(JsonHelper.JSON_FAILURE_INFO,
                    ""));
            return false;
        }
        if (state.m_requestId.isEmpty() || state.m_status.isEmpty()){
            errorCb.onError(
                    "The response does not carry required fields." +
                            " requestID: " + state.m_requestId +
                            " status: " + state.m_status
            );
            return false;
        }
        return true;
    }

    protected void timeoutDetected(
            Interest interest, int retryTimesLeft,
            OnData dataCb, ErrorCallback errorCb
    ){
        if (retryTimesLeft > 0){
            try {
                m_face.expressInterest(
                        interest,
                        dataCb,
                        getTimeoutCallbackFunc(retryTimesLeft, dataCb, errorCb),
                        getNetworkNackCallbackFunc(errorCb));
            } catch (IOException e){
                log.warning(e.getMessage());
            }
        } else {
            errorCb.onError("Run out retries: still timeout");
        }
    }
    private OnTimeout getTimeoutCallbackFunc(
            int retryTimesLeft,
            OnData dataCb, ErrorCallback errorCb
    ){
        return (i -> timeoutDetected(
                i, m_retryTimes - 1,
                dataCb, errorCb));
    }

    protected void nackDetected(
            Interest interest, NetworkNack nack,
            ErrorCallback errorCb){
        ;
    }
    private OnNetworkNack getNetworkNackCallbackFunc(
            ErrorCallback errorCb
    ){
        return ((i, networkNack) -> nackDetected(i, networkNack, errorCb));
    }
}
