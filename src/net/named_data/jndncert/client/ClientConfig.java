package net.named_data.jndncert.client;

import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.security.v2.CertificateV2;
import net.named_data.jndn.util.Common;
import net.named_data.jndncert.common.JsonHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class ClientConfig {

    public ArrayList<ClientCaItem> m_caItems = new ArrayList<>();
    public String m_localNdncertAnchor = "";
    private final Logger log = Logger.getLogger("ClientConfig");

    private static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public void load(String fileName){
        File file = new File(fileName);
        FileInputStream fileIptStrm = null;
        String content = "";
        try{
            fileIptStrm = new FileInputStream(file);
            content = convertStreamToString(fileIptStrm);
        } catch (IOException e){
            log.warning(e.getMessage());
        }
        load(JsonHelper.string2Json(content));
    }

    public void load(JSONObject jsonObject){
        m_caItems.clear();
        JSONArray arr = jsonObject.getJSONArray("ca-list");
        for (int idx = 0; idx < arr.length(); idx++){
            JSONObject temp_obj = arr.getJSONObject(idx);
            m_caItems.add(extractCaItem(temp_obj));
        }
        m_localNdncertAnchor =
                jsonObject.optString("local-ndncert-anchor");
    }

    public void addNewCaItem(ClientCaItem item){
        m_caItems.add(item);
    }

    public void removeCaItem(Name caName){
        Predicate<ClientCaItem> caPredicate = p -> p.m_caName.toUri().equals(caName.toUri());
        m_caItems.removeIf(caPredicate);
    }

    private ClientCaItem extractCaItem(JSONObject jsonObj){
        ClientCaItem ca_item = new ClientCaItem();

        // If these items are not there, Json parser will throw an NullPointerException
        // because the library is converting a nullptr to a String, which is meaningless.
        ca_item.m_caName = new Name(jsonObj.optString("ca-prefix"));
        ca_item.m_caInfo = jsonObj.optString("ca-info", "");
        ca_item.m_probe = jsonObj.optString("probe", "");
        ca_item.m_isListEnabled =
                jsonObj.optString("is-list-enabled", "").equals("true");
        ca_item.m_targetedList = jsonObj.optString("target-list", "");

        final byte[] certBytes = Common.base64Decode(jsonObj.optString("certificate"));
        try{
            ca_item.m_anchor = new CertificateV2();
            ca_item.m_anchor.wireDecode(ByteBuffer.wrap(certBytes));
        } catch (EncodingException e){
            log.warning(e.getMessage());
        }
        return ca_item;
    }

}
