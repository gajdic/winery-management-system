Winery Management System — Demo Video
======================================

Disclaimer: during recording, the screen capture and audio recording
failed to sync. We are attaching this short written description of
what the video shows.

The video walks through an end-to-end scenario across four microservices
(customer, harvest, production, inventory) with an Apache Artemis JMS
broker, Prometheus, and Grafana. The complexity of the scenario lies in
the asynchronous communication between production-service and
inventory-service.

What happens in the video (5 acts, ~5 minutes):

1. Architecture and running stack
   docker ps shows 8 containers; Prometheus targets confirms that all
   5 scrape targets are UP (4 services + Prometheus self-scrape).

2. Harvest domain
   Through the central Swagger UI we call GET /vineyards, /grape-types,
   and /harvest-records/summary/by-year. We see 3 vineyards in the Palava
   region, 4 grape varieties, and harvest aggregation by year.

3. Production planning
   GET /production/recipes returns 4 wine recipes. In /production/batches
   we locate the Sauvignon Blanc Late Harvest batch in READY status,
   planned at 1000 bottles, with 970 actually feasible.

4. JMS messaging (the key moment)
   At the start of Act 4, the terminal shows the logs of both services
   side by side — production-service (green) and inventory-service
   (yellow) — both ending with "Completed initialization...", confirming
   that the services are up and waiting.
   PATCH on the completed status triggers the asynchronous flow:
   production publishes a BatchCompletedMessage to Artemis, inventory
   consumes the message, increases the stock by 970 bottles (from 90 to
   1060), and sends an ACK back via the second queue. Production
   synchronously validates the ACK against the original batch and wine
   ID (validate-then-save pattern). All four log lines — producer to
   consumer to ACK — are visible in the terminals.
   In the Artemis console, the "Total Messages added" counter jumps
   from 0 to 1.

5. Customer perspective + monitoring
   GET /customers shows 3 customers; the Mary Harrison detail returns a
   bidirectional mapping with reviews and purchases in a single DTO.
   Wine IDs in the reviews match recipe IDs in production-service
   (cross-service data consistency). The Grafana dashboard shows a spike
   in HTTP Requests per Minute from this demo run.