# NHTSA Recall API Overview

NHTSA exposes multiple REST endpoints under `https://api.nhtsa.gov/recalls/`. The most common entry points are:

- `recallsByVIN?vin=<VIN>` – specific vehicle campaigns
- `recallsByVehicle?make=<MAKE>&model=<MODEL>&modelYear=<YEAR>` – make/model/year aggregation
- `recallsByCampaign?campaignNumber=<NHTSA Campaign>` – canonical campaign data

All responses return JSON with `results`/`Results` arrays matching the schema used in this repository. Refer to this file as we expand language bindings.
