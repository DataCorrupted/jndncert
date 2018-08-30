package net.named_data.jndncert.client;

import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.security.v2.CertificateV2;
import net.named_data.jndn.util.Common;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientConfig {

    public ArrayList<ClientCaItem> m_caItems = new ArrayList<>();
    public String m_localNdncertAnchor = "";
    private final Logger log = Logger.getLogger("ClientConfig");

    public void load(String fileName){
        File f = new File(fileName);
        String content = "";
        try {
            byte[] bytes = Files.readAllBytes(f.toPath());
            content = new String(bytes,"UTF-8");
        } catch (IOException e){
            log.warning(e.getMessage());
        }
        load(new JSONObject(content));
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
