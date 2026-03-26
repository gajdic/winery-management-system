# Winery Management System

Winery Management System is a microservice-based Spring Boot application for managing grape harvests, wine production,
cellar inventory, and customer sales in a winery.

## Project description

The system helps a winery owner keep track of annual grape harvests, define wine recipes, estimate how much wine can be
produced from available grapes, manage bottle stock, and process customer purchases. Buyers can browse wines, buy
bottles, and leave feedback about the wine quality and their experience.

## Microservices

The project is split into four executable Maven modules:

- `customer-service` - customer profiles, purchase history, and feedback.
- `harvest-service` - harvest records, vineyard locations, quantities, and grape quality.
- `production-service` - wine recipes, production planning, batch tracking, and blend quality evaluation.
- `inventory-service` - bottle stock, reservations, and sold bottle tracking.

Each module can be run as a separate Spring Boot application.

### Production service

The `production-service` implements two non-trivial business functions beyond CRUD:

1. **Production planning** - computes how many bottles can be produced from available harvest lots for a selected
   recipe, determines whether the request is fully feasible, and identifies the limiting grape variety.
2. **Blend quality evaluation** - computes a weighted quality score for the available harvest lots against a recipe and
   recommends whether the blend is suitable for standard or premium production.

## Use case diagram

TODO

## DTO class diagram

TODO
