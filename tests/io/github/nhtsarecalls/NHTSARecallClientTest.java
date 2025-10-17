package io.github.nhtsarecalls;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NHTSARecallClientTest {

    @Test
    void parseRecordsExtractsFields() throws IOException {
        String payload = "{" +
                "\"results\":[{" +
                "\"ReportReceivedDate\":\"2021-02-18\"," +
                "\"Manufacturer\":\"Honda\"," +
                "\"Component\":\"AIR BAGS\"," +
                "\"Summary\":\"Example summary\"," +
                "\"Consequence\":\"Example consequence\"," +
                "\"Remedy\":\"Example remedy\"," +
                "\"Notes\":\"Example notes\"," +
                "\"NHTSACampaignNumber\":\"21V123000\"," +
                "\"ModelYear\":\"2018\"," +
                "\"Make\":\"Honda\"," +
                "\"Model\":\"Civic\"," +
                "\"VIN\":\"19XFC2F59JE000000\"" +
                "}]" +
                "}";

        NHTSARecallClient client = new NHTSARecallClient();
        List<RecallRecord> records = client.parseRecords(payload);

        assertEquals(1, records.size());
        RecallRecord record = records.get(0);
        assertEquals("Honda", record.getMake());
        assertEquals("Civic", record.getModel());
        assertEquals("21V123000", record.getNhtsaCampaignNumber());
        assertNotNull(record.getRaw());
    }
}
