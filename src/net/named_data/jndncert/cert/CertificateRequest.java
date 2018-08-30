package net.named_data.jndncert.cert;

import net.named_data.jndn.Name;
import net.named_data.jndn.security.v2.CertificateV2;
import net.named_data.jndncert.common.JsonHelper;
import org.json.JSONObject;

public class CertificateRequest {
    private Name m_caName;
    private String m_requestId;
    // In cpp, there are bunch of getters and setters at the same time.
    // This doesn't make sense since you might as well set them as public.
    public String m_status;
    public String m_challengeType;
    public JSONObject m_challengeSecrets;
    public  CertificateV2 m_cert;

    public CertificateRequest(){
        this(new Name(), "", "", "", "{}", new CertificateV2());
    }
    public CertificateRequest(
            Name caName, String requestId, CertificateV2 cert){
        this(caName, requestId, "", "", "{}", cert);
    }
    public CertificateRequest(
            Name caName, String requestId, String status,
            String challengeType, String challengeSecrets,
            CertificateV2 cert) {
        m_caName = caName;
        m_requestId = requestId;
        m_status = status;
        m_challengeType = challengeType;
        m_cert = cert;
        if (challengeSecrets != null) {
            m_challengeSecrets = JsonHelper.string2Json(challengeSecrets);
        }
    }
    public Name getCaName() { return m_caName; }
    public String getRequestId() { return m_requestId; }

    // No way to overload a stream function like cpp,
    // but java calls toStirng() when you attempt print an object
    // toString() is a base function defined in Object. Just override it.
    // TODO: add indent to m_cert when you have time. This is not important now.
    public String toString(){
        return "Request CA name: \n " + m_caName + "\n" +
                "Request ID: \n " + m_requestId + "\n" +
                (m_status.equals("") ? "" :
                        "Request status: \n " + m_status + "\n") +
                (m_challengeType.equals("") ? "" :
                        "Request challenge type: \n " + m_challengeType + "\n") +
                "Certificate: \n" + m_cert;
    }
}
