package net.named_data.jndncert.cert

import net.named_data.jndn.Name
import net.named_data.jndn.security.KeyChain
import net.named_data.jndn.security.pib.PibIdentity
import net.named_data.jndn.security.pib.PibKey
import net.named_data.jndn.security.v2.CertificateV2

import javax.json.Json
import javax.json.JsonObject

class CertificateRequestTest extends GroovyTestCase {
    KeyChain keyChain = new KeyChain("pib-memory:", "tpm-memory:")
    PibIdentity identity = keyChain.createIdentityV2(new Name("/ndn/shanghaitech"))
    PibKey key = identity.getDefaultKey()
    CertificateV2 cert = key.getDefaultCertificate()

    void testInit(){
        CertificateRequest request
        request = new CertificateRequest(
                new Name("/ndn/shanghaitech"), "69850764", cert);
        assert request.getCaName().toUri() == "/ndn/shanghaitech"
        assert request.getRequestId() == "69850764"
        assert request.m_status == ""
        assert request.m_challengeSecrets.isEmpty()
        assert request.m_cert == cert;

        JsonObject secret = Json.createObjectBuilder()
                .add("code", "P.R.")
                .build()
        request = new CertificateRequest(
                new Name("/ndn/shanghaitech"), "69850764",
                "need-verify", "EMAIL",
                secret.toString(), cert);
        assert request.getCaName().toUri() == "/ndn/shanghaitech"
        assert request.getRequestId() == "69850764"
        assert request.m_status == "need-verify"
        assert request.m_challengeType == "EMAIL"
        assert request.m_challengeSecrets == secret
        assert request.m_cert == cert
    }
    // TODO: finish test toString() when you have indent for m_cert done.
    void testToString() {
        assert 1 == 1
    }
}
