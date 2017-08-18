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
