package net.named_data.jndncert.client

import net.named_data.jndn.Data
import net.named_data.jndn.Interest
import net.named_data.jndn.Name
import net.named_data.jndn.security.KeyChain
import net.named_data.jndn.security.SigningInfo
import net.named_data.jndn.security.pib.PibIdentity
import net.named_data.jndn.security.pib.PibKey
import net.named_data.jndn.security.v2.CertificateV2
import net.named_data.jndn.util.Blob
import net.named_data.jndncert.common.JsonHelper

import javax.json.Json
import javax.json.JsonObject

class ClientModuleTest extends GroovyTestCase {
    class ErrorCallback implements ClientModule.ErrorCallback{
        private String expectedErrInfo = ""
        void onError(String errInfo){
            assert errInfo == expectedErrInfo
        }
        String getExpected() { return expectedErrInfo }
        void setExpected(String expected){ expectedErrInfo = expected }
    }
    ErrorCallback errCb = new ErrorCallback()
    DummyFace face = new DummyFace()
    KeyChain keyChain = new KeyChain("pib-memory", "tpm-memory")
    ClientModule client = new ClientModule(face, keyChain)
    RequestState state_;

    void testInit() {
        client.getClientConfig().load(
                "res/test/client.conf.test")
        assert client.getClientConfig().m_caItems.size() == 2
    }

    void testSendNew(){
        client.getClientConfig().load(
                "res/test/client.conf.test"
        )
        PibIdentity id = keyChain.createIdentityV2(new Name("/site"))
        PibKey key = id.getDefaultKey()
        CertificateV2 cert = key.getDefaultCertificate()

        ClientCaItem item = new ClientCaItem()
        item.m_caName = new Name("/site/CA")
        item.m_anchor = cert
        client.getClientConfig().m_caItems.add(item)
        ClientModule.RequestCallback requestCb = new ClientModule.RequestCallback() {
            @Override
            void onRequest(RequestState state) {
                assert state.m_status == "Status"
                assert state.m_requestId == "69850764"
                assert state.m_challengeList.size() == 2
                state_ = state;
            }
        }
        client.sendNew(item, new Name("/site"), requestCb, errCb)
        Interest interest = face.getInterest();
        // The Interest sent was correct.
        assert interest.getName().size() == 6
        assert interest.getName().getPrefix(3)
                .toUri() == "/site/CA/_NEW"

        // Generate a fake response.
        Data data = new Data()
        data.setName(face.getInterest().getName())
        ArrayList<String> challenges = new ArrayList<>()
        challenges.add("EMAIL")
        challenges.add("PIN")
        JsonObject responseJson =
                JsonHelper.genNewResponseJson(
                        "69850764", "Status", challenges)
        Blob blob = client.nameBlockFromJson(responseJson)
        data.setContent(blob)
        keyChain.sign(data, new SigningInfo(key))
        face.feedData(data)
    }

    void testSelect(){
        // pretend that we have send _NEW
        testSendNew()
        JsonObject param = Json.createObjectBuilder()
                .add("email-address", "rongyy@shanghaitech.edu.cn")
                .add("emergency-contact", Json.createArrayBuilder()
                    .add(Json.createObjectBuilder().add("name", "Ling Jiawei"))
                    .add(Json.createObjectBuilder().add("name", "Gao Xin"))
                    )
                .build()
        ClientModule.RequestCallback requestCb = new ClientModule.RequestCallback() {
            @Override
            void onRequest(RequestState state) {
                assert state.m_status == "Select"
                state_ = state
            }
        }
        client.sendSelect(state_, "EMAIL", param, requestCb, errCb);
        Interest interest = face.getInterest()
        assert interest.getName().size() == 8
        Data data = new Data()
        data.setName(interest.getName());
        JsonObject status = Json.createObjectBuilder().add("status", "Select").build()
        Blob blob = client.nameBlockFromJson(status)
        data.setContent(blob)
        // TODO: How do I sign this data?
        keyChain.sign(data, keyChain.getDefaultIdentity())
        face.feedData(data)
    }

    void testGetJsonFromData() {
        String jsonStr = "{" +
                    "\"Name\":\"Peter Rong\"," +
                    "\"School\":\"ShanghaiTech University\"" +
                "}"
        Data data = new Data(new Name("testData"))
                .setContent(new Blob(jsonStr))
        JsonObject object = client.getJsonFromData(data)
        assert object.getString("Name") == "Peter Rong"
        assert object.getString("School") == "ShanghaiTech University"
    }

    void testNameBlockFromJson() {
        JsonObject object = Json.createObjectBuilder()
                .add("Name", "Peter Rong")
                .add("School", "ShanghaiTech University")
                .build()
        Blob blob = client.nameBlockFromJson(object)
        assert blob.toString() == "{" +
                    "\"Name\":\"Peter Rong\"," +
                    "\"School\":\"ShanghaiTech University\"" +
                "}"
    }

    void testCheckStatus() {
        RequestState state = new RequestState()
        Boolean checkResult

        // failure
        state.m_status = "failure"
        errCb.setExpected("Peter: This is an error info.")
        JsonObject object = Json.createObjectBuilder()
                .add(JsonHelper.JSON_FAILURE_INFO, errCb.getExpected())
                .build()
        checkResult = client.checkStatus(state, object, errCb)
        assert !checkResult

        // invalid response - no status
        state.m_status = ""
        state.m_requestId = "69850764"
        errCb.setExpected(
                "The response does not carry required fields." +
                " requestID: " + state.m_requestId +
                " status: " + state.m_status)
        checkResult = client.checkStatus(state, object, errCb)
        assert !checkResult

        // invalid response - no request id
        state.m_status = "success"
        state.m_requestId = ""
        errCb.setExpected(
                "The response does not carry required fields." +
                " requestID: " + state.m_requestId +
                " status: " + state.m_status)
        checkResult = client.checkStatus(state, object, errCb)
        assert !checkResult

        // success
        state.m_status = "success"
        state.m_requestId = "69850764"
        errCb.setExpected("")
        checkResult = client.checkStatus(state, object, errCb)
        assert checkResult
    }

}
