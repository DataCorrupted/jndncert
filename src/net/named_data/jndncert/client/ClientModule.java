package net.named_data.jndncert.client;

import net.named_data.jndn.*;
import net.named_data.jndn.security.KeyChain;
import net.named_data.jndn.util.Blob;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.ArrayList;


public class ClientModule {
    // TODO: Fill this class.
    protected ClientConfig m_config;
    protected Face m_face;
    protected KeyChain m_keyChain;
    protected int m_retryTimes;

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
        void localhostListCb(ClientConfig clientConfig);
    }
    public interface ListCallback{
        void listCb(ArrayList<Name> caList, Name assignedName, Name schema);
    }
    public interface RequestCallback{
        void requestCb(RequestState state);
    }
    public interface ErrorCallback{
        void errCb(String errInfo);
    }

    public void reuqestCaTrustAnchor(
            Name caName,
            OnData trustAnchorCb, ErrorCallback errorCb
    ){
        ;
    }

    public void requestLocalhostList(
            LocalhostListCallback listCb, ErrorCallback errorCb){
        ;
    }

    public void handleLocalhostListResponse(
            Interest request, Data reply,
            LocalhostListCallback listCb
    ){
        ;
    }

    public void requestList(
            ClientCaItem ca, String additionalInfo,
            ListCallback listCb, ErrorCallback errorCb
    ){
        ;
    }

    public void handleListResponse(
            Interest request, Data reply, ClientCaItem ca,
            ListCallback listCb, ErrorCallback errorCb
    ){
        ;
    }

    public void sendProbe(
            ClientCaItem ca, String probInfo,
            RequestCallback requestCb, ErrorCallback errorCb
    ){
        ;
    }

    public void handleProbeResponse(
            Interest request, Data reply, RequestState state,
            RequestCallback requestCb, ErrorCallback errorCb){
        ;
    }

    public void sendNew(
            ClientCaItem ca, Name identityName,
            RequestCallback requestCb, ErrorCallback errorCb){
        ;
    }

    public void handleNewRequest(
            Interest request, Data reply, RequestState state,
            RequestCallback requestCb, ErrorCallback errorCb
    ){
        ;
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

    public JsonObject getJsonFromData(Data data) {
        return Json.createObjectBuilder().build();
    }

    public Blob nameBlockFromJson(JsonObject obj){
        return new Blob();
    }

    public final Boolean checkStatus(
            RequestState state, JsonObject json,
            ErrorCallback errorCb
    ){
        return true;
    }

    protected void onTimeout(
            Interest interest, int nRetriesLeft,
            OnData dataCb, ErrorCallback errorCb
    ){
        ;
    }

    protected void onNack(
            Interest interest, NetworkNack nack,
            ErrorCallback errorCb
    ){
        ;
    }
}
