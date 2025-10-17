from python.recall_client import NHTSARecallClient

client = NHTSARecallClient()

recalls = client.recalls_by_vin("1HGCM82633A004352")
for recall in recalls:
    print(f"{recall.nhtsa_campaign_number} - {recall.summary}")
