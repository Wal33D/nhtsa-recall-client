"""NHTSA Recall API client mirroring the VIN decoder architecture."""

from __future__ import annotations

import json
from dataclasses import dataclass
from functools import lru_cache
from typing import Dict, List, Optional, Tuple
from urllib import error, parse, request

BASE_URL = "https://api.nhtsa.gov/recalls"


@dataclass
class RecallRecord:
    """Structured recall record returned by NHTSA."""

    report_received_date: Optional[str] = None
    manufacturer: Optional[str] = None
    component: Optional[str] = None
    summary: Optional[str] = None
    consequence: Optional[str] = None
    remedy: Optional[str] = None
    notes: Optional[str] = None
    nhtsa_campaign_number: Optional[str] = None
    recall_number: Optional[str] = None
    model_year: Optional[str] = None
    make: Optional[str] = None
    model: Optional[str] = None
    vin: Optional[str] = None
    raw: Dict[str, str] | None = None


class NHTSARecallClient:
    """Simple wrapper around the public NHTSA recall endpoints."""

    def __init__(self, base_url: str = BASE_URL, timeout: int = 10):
        self.base_url = base_url.rstrip("/")
        self.timeout = timeout

    def recalls_by_vin(self, vin: str) -> List[RecallRecord]:
        """Fetch recall campaigns by VIN."""
        path = "/recallsByVIN"
        params = {"vin": vin.upper().strip()}
        return self._fetch(path, tuple(sorted(params.items())))

    def recalls_by_make_model_year(self, make: str, model: str, year: str) -> List[RecallRecord]:
        """Fetch recalls by make/model/year combination."""
        path = "/recallsByVehicle"
        params = {"make": make, "model": model, "modelYear": year}
        return self._fetch(path, tuple(sorted(params.items())))

    def recalls_by_campaign(self, campaign: str) -> List[RecallRecord]:
        """Fetch details for a specific NHTSA campaign number."""
        path = "/recallsByCampaign"
        params = {"campaignNumber": campaign}
        return self._fetch(path, tuple(sorted(params.items())))

    @lru_cache(maxsize=128)
    def _fetch(self, path: str, params_key: Tuple[Tuple[str, str], ...]) -> List[RecallRecord]:
        """Perform HTTP GET and parse recall records."""
        params = dict(params_key)
        url = f"{self.base_url}{path}?{parse.urlencode(params)}&format=json"
        req = request.Request(url, headers={"User-Agent": "nhtsa-recall-client/0.1"})

        try:
            with request.urlopen(req, timeout=self.timeout) as resp:
                payload = resp.read().decode("utf-8")
        except error.URLError as exc:
            raise RuntimeError(f"NHTSA recall API request failed: {exc}") from exc

        try:
            data = json.loads(payload)
        except json.JSONDecodeError as exc:
            raise RuntimeError("Failed to decode NHTSA response as JSON") from exc

        results = data.get("results") or data.get("Results") or []
        return [self._to_record(entry) for entry in results if isinstance(entry, dict)]

    @staticmethod
    def _to_record(entry: Dict[str, str]) -> RecallRecord:
        """Convert raw dict into RecallRecord."""
        return RecallRecord(
            report_received_date=entry.get("ReportReceivedDate"),
            manufacturer=entry.get("Manufacturer"),
            component=entry.get("Component"),
            summary=entry.get("Summary"),
            consequence=entry.get("Consequence") or entry.get("Conequence"),
            remedy=entry.get("Remedy"),
            notes=entry.get("Notes"),
            nhtsa_campaign_number=entry.get("NHTSACampaignNumber"),
            recall_number=entry.get("RecallNumber"),
            model_year=entry.get("ModelYear"),
            make=entry.get("Make"),
            model=entry.get("Model"),
            vin=entry.get("VIN"),
            raw=entry,
        )
