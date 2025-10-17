import json
import unittest
from unittest import mock
from urllib import request

from python.recall_client import NHTSARecallClient


class RecallClientTestCase(unittest.TestCase):
    def test_recalls_by_vin_parses_results(self):
        sample = {
            "results": [
                {
                    "ReportReceivedDate": "2021-02-18",
                    "Manufacturer": "Honda",
                    "Component": "AIR BAGS",
                    "Summary": "Example summary",
                    "Consequence": "Example consequence",
                    "Remedy": "Example remedy",
                    "Notes": "Example notes",
                    "NHTSACampaignNumber": "21V123000",
                    "ModelYear": "2018",
                    "Make": "Honda",
                    "Model": "Civic",
                    "VIN": "19XFC2F59JE000000",
                }
            ]
        }

        client = NHTSARecallClient()
        payload = json.dumps(sample).encode("utf-8")

        mock_response = mock.MagicMock()
        mock_response.read.return_value = payload
        mock_response.__enter__.return_value = mock_response

        with mock.patch.object(request, "urlopen", return_value=mock_response):
            recalls = client.recalls_by_vin("19XFC2F59JE123456")

        self.assertEqual(len(recalls), 1)
        recall = recalls[0]
        self.assertEqual(recall.make, "Honda")
        self.assertEqual(recall.model, "Civic")
        self.assertEqual(recall.nhtsa_campaign_number, "21V123000")


if __name__ == "__main__":
    unittest.main()
