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
