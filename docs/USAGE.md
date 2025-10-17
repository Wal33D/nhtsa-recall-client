# Usage Guide

## Python

```bash
pip install nhtsa-recall-client
```

```python
from python.recall_client import NHTSARecallClient

client = NHTSARecallClient()
recalls = client.recalls_by_make_model_year("Honda", "Civic", "2018")
```

## Java (Gradle)

```groovy
dependencies {
    implementation 'io.github.nhtsarecalls:nhtsa-recall-client:0.1.0'
}
```

```java
NHTSARecallClient client = new NHTSARecallClient();
client.getRecallsByCampaign("21V123000");
```

## Android

Include the Android module once it is published, then use the async helper to keep work off the UI thread.
