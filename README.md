# NHTSA Recall Client

Sister project to the `nhtsa-vin-decoder` toolkit that focuses on the National Highway Traffic Safety Administration (NHTSA) recall APIs. Provides a unified Java, Android, and Python interface for retrieving manufacturer recall campaigns by VIN, make/model/year, or component.

> ⚠️ Repository is under active construction. Initial scaffolding mirrors the VIN decoder project and will grow to feature full API parity, offline caching, and sample apps.

## Project Layout

```
nhtsa-recall-client/
├── android/                     # Android wrapper (async callbacks)
├── java/                        # Core Java implementation
├── python/                      # Python client library
├── docs/                        # Documentation and API guides
├── examples/                    # Usage samples
└── tests/                       # Automated tests
```

## Quick Start

### Python

```python
from python.recall_client import NHTSARecallClient

client = NHTSARecallClient()
recalls = client.recalls_by_vin("1HGCM82633A004352")

for recall in recalls:
    print(recall.nhtsa_campaign_number, recall.summary)
```

### Java

```java
import io.github.nhtsarecalls.NHTSARecallClient;

NHTSARecallClient client = new NHTSARecallClient();
client.getRecallsByVin("1HGCM82633A004352")
      .forEach(recall -> System.out.println(recall.getNhtsaCampaignNumber()));
```

### Android (async)

```java
NHTSARecallClientAndroid client = new NHTSARecallClientAndroid();
client.getRecallsByVin("1HGCM82633A004352", new RecallCallback() {
    @Override public void onSuccess(List<RecallRecord> recalls) { /* ui update */ }
    @Override public void onError(Throwable throwable) { /* log */ }
});
```

## Roadmap

- Provide typed recall data models for VIN, campaign, and component queries
- Implement shared HTTP client with retry/caching similar to VIN decoder
- Deliver Python package (PyPI), Java library (Maven/Gradle), and Android artifact
- Add docs, code samples, and CI mirroring the VIN decoder project

## License

MIT – see `LICENSE`.
