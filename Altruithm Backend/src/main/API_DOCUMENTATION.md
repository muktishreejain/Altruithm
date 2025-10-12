# Altruithm Fraud Detection API

## Endpoint: POST /api/fraud/check

### Request:
{
"charityName": "string",
"amount": number
}

### Response:
{
"riskLevel": "LOW | MEDIUM | HIGH",
"riskScore": 0.0-1.0,
"warnings": ["list of red flags"]
}

### Examples:
- Safe: Red Cross, $500 → MEDIUM
- Risky: URGENT EMERGENCY, $50000 → HIGH

// {
"charityName": "Red Cross",
"amount": 500
}

//{
"charityName": "URGENT EMERGENCY HELP",
"amount": 50000
}