<a href="https://travis-ci.org/mscaldas2012/CDS_PHTriggers"><img src="https://travis-ci.org/mscaldas2012/CDS_PHTriggers.svg?branch=master"></img></a>
# Concept

**Hook:** PHTriggerS Hook

**Purpose:** Provide initial decision support if a EHR event qualifiers as a possible reportable event.

**Description:** The PHTriggers hook will provide a response back based on the RCTC implementation. If a set of code
matches are found the service will send back a "reportable" indicator. Otherwise, a "not reportable" determination will 
be returned.

**Disclaimer:** This is a coarse grained first line decision support that determines that this event may be
of interest and possibly reportable. Further adjudication would be required to determine actual reportability.

# Implementation

This project is being implemented in java with spring framework. Refer to the swagger yaml file for documentation on
endpoints.

Swagger:<BR>
http://editor.swagger.io/?url=https://raw.githubusercontent.com/mscaldas2012/CDS_PHTriggers/master/src/docs/apidoc.yaml


Travis:<BR>
https://travis-ci.org/mscaldas2012/CDS_PHTriggers

#Sample Request
```
{
   "hookInstance" : "<d1577c69-dfbe-44ad-ba6d-3e05e953b2ea>",
   "fhirServer" : "<EHR FHIR URL>",
   "hook" : "lab-create",
   "redirect" : "<http://hooks2.smarthealthit.org/service-done.html>",
   "user" : "Practitioner/example",
   "context" : [
                  {
                    "context": {
                      "resourceType": "Observation",
                      "status": "preliminary",
                      "code": {
                        "coding": [
                          {
                            "system": "http://loinc.org",
                            "code": "<43890-3>",
                            "display": "<Bordetella pertussis [Presence] in Sputum by Organism specific culture>"
                          }
                        ]
                      }
                    }
                  }
   ],
   "patient" : "<1288992>",
   "prefetch" : {   
   	"patient": "Patient/{{Patient.id}}",
        "observations": "Observation?patient={{Patient.id}}"
      }
   }
}
```


#Sample Response
```
{
  "cards": [
	{
      "summary": "This case is Possibly Reportable. It should be sent as an eICR to Public Health Intermediary for final Adjudication",
      "detail" : "This card shows the action to be taken" ,
      "indicator": "info",
      "suggestions":
      [
      	{
      		"label": "Send Case Report",
      		"uuid" : "<generateduuid>",
      		actions: 
      		[
      			{
   				"type": "create",
   				"description" : "Send Case Report for Adjudication to Public Health Intermediary", 
   				"resources" : "<new_order>"
      			}
      		]
      	}
      ],
      "links"
      "source": {
        "label": "PHTriggers RCTC Service",
        "url": "https://cds-services.ng-atl.com/cds-services/code_reportability",
        "type": "smart"
      }
    ],

    "decisions" : 
    [
    	{
    	"create" : ["<new_order_id>","<new_order_id>"],
    	"delete" : []
    	}
	]
}

```

DSTU2 FHIR Observation

```
{
  "resource": {
    "resourceType": "Observation",
    "id": "M5851276",
    "meta": {
      "versionId": "1-5851275",
      "lastUpdated": "2016-01-26T19:54:14.000Z"
    },
    "text": {
      "status": "generated",
      "div": "<div><p><b>Observation</b></p><p><b>Patient</b>: 3998008</p><p><b>Status</b>: Auth (Verified)</p><p><b>Category</b>: Laboratory</p><p><b>Code</b>: RBC</p><p><b>Result</b>: 5 x10(3)/mcL</p><p><b>Risk Level</b>: Normal</p><p><b>Date</b>: 2016-01-26T19:41:00.000Z</p><p><b>Risk Level Detail</b>: Low: 4 x10(3)/mcL High: 6 x10(3)/mcL</p></div>"
    },
    "status": "final",
    "category": {
      "coding": [
        {
          "system": "http://hl7.org/fhir/observation-category",
          "code": "laboratory",
          "display": "Laboratory"
        }
      ],
      "text": "Laboratory"
    },
    "code": {
      "coding": [
        {
          "system": "http://loinc.org",
          "code": "789-8",
          "display": "ERYTHROCYTES:NCNC:PT:BLD:QN:AUTOMATED COUNT"
        }
      ],
      "text": "RBC"
    },
    "subject": {
      "reference": "Patient/3998008"
    },
    "encounter": {
      "reference": "Encounter/3651924"
    },
    "effectiveDateTime": "2016-01-26T19:41:00.000Z",
    "issued": "2016-01-26T19:54:07.000Z",
    "valueQuantity": {
      "value": 5,
      "unit": "x10(3)/mcL",
      "system": "http://unitsofmeasure.org",
      "code": "10*3/uL"
    },
    "interpretation": {
      "coding": [
        {
          "system": "http://hl7.org/fhir/v2/0078",
          "code": "N",
          "display": "Normal"
        }
      ]
    },
    "comments": "{\\rtf1\\ansi\\ansicpg1252\\uc0\\deff0{\\fonttbl\r\n{\\f0\\fswiss\\fcharset0\\fprq2 Arial;}\r\n{\\f1\\froman\\fcharset2\\fprq2 Symbol;}}\r\n{\\colortbl;\\red0\\green0\\blue0;\\red255\\green255\\blue255;}\r\n{\\*\\generator TX_RTF32 10.1.323.501;}\r\n\\deftab1134\\pard\\plain\\f0\\fs24\\cb2\\chshdng0\\chcfpat0\\chcbpat2 This is interpretative data for RBC.  This should apply to all routed to service resources.\\par\\pard\\par }",
    "referenceRange": [
      {
        "low": {
          "value": 4,
          "unit": "x10(3)/mcL",
          "system": "http://unitsofmeasure.org",
          "code": "10*3/uL"
        },
        "high": {
          "value": 6,
          "unit": "x10(3)/mcL",
          "system": "http://unitsofmeasure.org",
          "code": "10*3/uL"
        }
      }
    ]
  }
}
```
