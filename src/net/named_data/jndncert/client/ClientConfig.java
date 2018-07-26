package net.named_data.jndncert.client;

import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.security.v2.CertificateV2;
import net.named_data.jndn.util.Common;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class ClientConfig {

    public ArrayList<ClientCaItem> m_caItems = new ArrayList<>();
    public String m_localNdncertAnchor = "";
    private final Logger log = Logger.getLogger("ClientConfig");

    public void load(String fileName){
        try {
            File f = new File(fileName);
            InputStream inputFileStream = new FileInputStream(f);
            JsonReader reader = Json.createReader(inputFileStream);
            load(reader);
            reader.close();
        } catch (FileNotFoundException e) {
            log.warning(e.getMessage());
        };
    }

    public void load(JsonReader jsonReader){
        m_caItems.clear();
        JsonObject main_obj = jsonReader.readObject();
        JsonArray arr = main_obj.getJsonArray("ca-list");
        for (int idx = 0; idx < arr.size(); idx++){
            JsonObject temp_obj = arr.getJsonObject(idx);
            m_caItems.add(extractCaItem(temp_obj));
        }
        m_localNdncertAnchor =
                main_obj.getString("local-ndncert-anchor", "");
    }

    // TODO: This function appeared in the ndncert, yet it's never implemented.
    // Please implement it after ndncert gets updated.
    public void addNewCaItem(ClientCaItem item){ ; }

    public void removeCaItem(Name caName){
        Predicate<ClientCaItem> caPredicate = p -> p.m_caName.toUri().equals(caName.toUri());
        m_caItems.removeIf(caPredicate);
    }

    private ClientCaItem extractCaItem(JsonObject jsonObj){
        ClientCaItem ca_item = new ClientCaItem();

        // If these items are not there, Json parser will throw an NullPointerException
        // because the library is converting a nullptr to a String, which is meaningless.
        ca_item.m_caName = new Name(jsonObj.getString("ca-prefix"));
        ca_item.m_caInfo = jsonObj.getString("ca-info", "");
        ca_item.m_probe = jsonObj.getString("probe", "");
        ca_item.m_isListEnabled =
                jsonObj.getString("is-list-enabled", "").equals("true");
        ca_item.m_targetedList = jsonObj.getString("target-list", "");

        final byte[] certBytes = Common.base64Decode(jsonObj.getString("certificate"));
        try{
            ca_item.m_anchor = new CertificateV2();
            ca_item.m_anchor.wireDecode(ByteBuffer.wrap(certBytes));
        } catch (EncodingException e){
            log.warning(e.getMessage());
        }
        return ca_item;
    }

}
