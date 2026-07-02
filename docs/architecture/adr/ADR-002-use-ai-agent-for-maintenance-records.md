# ADR-002: Use AI-agent for maintenance records


**Status:** Accepted

**Context:** The product must provide the ability to car owners to communicate with AI assistant in chat. The AI assistant must understand the certain context of the car owner and helps to thier get stored data or make a record about maintaince, expense, repair, inspection in a more natural way. The backend also need to control when AI-agent returns invalid extracted data from user request.

**Decision:** For solving the problem backend team integrated Deepseek 4 model. Access to the model was provided my the customer through the configured AI provider. The AI-agent was set up to understand the context of user records and their requirements. The backend calls the model through the OpenAI compatible API, passes the current car context and user message, and asks the model to return the main data for record in JSON.

**Consequences and tradeoffs**: As a consequense the user can make new records or get information about car not only through of manually filling form but also with help if AI assistant. Tradeof is that the product depends from the extarnal AI Provider and must validate extracted data from messages and invalid values

**Quality requirements addressed where applicable** 
- QR-002: Controlled AI provider failure handling
- QR-003: Invalid extracted maintenance data rejection
