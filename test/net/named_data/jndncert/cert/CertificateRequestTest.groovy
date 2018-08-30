package net.named_data.jndncert.cert

import net.named_data.jndn.Name
import net.named_data.jndn.security.KeyChain
import net.named_data.jndn.security.pib.PibIdentity
import net.named_data.jndn.security.pib.PibKey
import net.named_data.jndn.security.v2.CertificateV2
import org.json.JSONObject

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
        assert request.m_challengeSecrets.isNull()
        assert request.m_cert == cert;

        JSONObject secret = new JSONObject().put("code", "P.R.");
        request = new CertificateRequest(
                new Name("/ndn/shanghaitech"), "69850764",
                "need-verify", "EMAIL",
                secret.toString(), cert);
        assert request.getCaName().toUri() == "/ndn/shanghaitech"
        assert request.getRequestId() == "69850764"
        assert request.m_status == "need-verify"
        assert request.m_challengeType == "EMAIL"
        assert request.m_challengeSecrets.toString() == secret.toString()
        assert request.m_cert == cert
    }
    // TODO: finish test toString() when you have indent for m_cert done.
    void testToString() {
        assert 1 == 1
    }
}
