package net.named_data.jndncert.client

import net.named_data.jndn.Name

import javax.json.Json
import javax.json.JsonObject

class ClientConfigTest extends groovy.util.GroovyTestCase {
    void testLoad() {
        ClientConfig config = new ClientConfig();
        config.load("res/test/client.conf.test");
        assert config.m_caItems.size() == 2

        final ClientCaItem item = config.m_caItems.get(0);
        assert item.m_caName.toUri() == "/ndn/edu/ShanghaiTech/CA"
        assert item.m_caInfo == "ShanghaiTech's certificate authority."
        assert item.m_probe == "Please use your email address to apply a namespace first."
        assert item.m_targetedList == "Use your email address (edu preferred) as input"
        assert item.m_isListEnabled
        // TODO: Test anchor here.
//        assert item.m_anchor.getName().toUri() ==
//                "/ndn/site1/KEY/%11%BC%22%F4c%15%FF%17/self/%FD%00%00%01Y%C8%14%D9%A5"

        System.err.println("ClientConfig.load(): Passed.");
    }

    void testRemoveCaItem() {
        ClientConfig config = new ClientConfig();
        config.load("res/test/client.conf.test");

        ClientCaItem item = new ClientCaItem();
        item.m_caName = new Name("/test");
        item.m_caInfo = "test";
        item.m_probe = "test";
        item.m_isListEnabled = false;

        // Add
        config.m_caItems.add(item);
        assert config.m_caItems.size() == 3
        assert config.m_caItems.get(2).m_caName.toUri() == "/test"

        // Remove
        config.removeCaItem(new Name("/test"));
        assert config.m_caItems.size() == 2
        assert config.m_caItems.get(1).m_caName.toUri() == "/ndn/edu/ShanghaiTech/peter/CA"

        System.err.println("ClientConfig.removeCaItem(): Passed.");
    }

    void testExtractCaItem() {
        JsonObject test_obj = Json.createObjectBuilder()
            .add("ca-prefix", "prefix")
            .add("ca-info", "info")
            .add("target-list", "list")
            .add("probe", "probe")
            .add("is-list-enabled", "true")
            .add("certificate", "cert")
        .build();

        ClientConfig config = new ClientConfig();
        ClientCaItem ca_item = config.extractCaItem(test_obj);

        assert ca_item.m_caName.toUri() == "/prefix"
        assert ca_item.m_caInfo == "info"
        assert ca_item.m_targetedList == "list"
        assert ca_item.m_probe == "probe"
        assert ca_item.m_isListEnabled
        // TODO: Please also think of a way to do test on CertV2
        // assert ca_item.m_anchor == "something"

        System.err.println("ClientConfig.extractItem(): Passed.");

    }
}
