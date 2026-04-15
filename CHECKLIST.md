# Project Checklist

Hlavny priebezne aktualizovany checklist pre cely projekt.

Aktualny stav bol naposledy skontrolovany proti repozitaru dna `2026-04-15`.

## M2 povinne body

### 1. Build a testy

- [x] `mvn clean install` prechadza na celom projekte.
- [x] `customer-service` prejde testami.
- [x] `harvest-service` prejde testami.
- [x] `production-service` prejde testami.
- [x] `inventory-service` prejde testami.

### 2. Persistence

- [x] `customer-service` pouziva Spring Data JPA a H2.
- [x] `harvest-service` pouziva Spring Data JPA a H2.
- [x] `production-service` pouziva Spring Data JPA a H2.
- [x] `inventory-service` pouziva Spring Data JPA a H2.
- [x] Repository interfaces existuju vo vsetkych 4 domenovych services.
- [x] `customer-service` ma aspon 2 custom repository metody napriec repository vrstvou.
- [x] `harvest-service` ma aspon 2 custom repository metody.
- [x] `production-service` ma aspon 2 custom repository metody.
- [x] `inventory-service` ma aspon 2 custom repository metody napriec repository vrstvou.

### 3. Repository testy

- [x] `customer-service` ma `@DataJpaTest` repository testy.
- [x] `harvest-service` ma `@DataJpaTest` repository testy.
- [x] `production-service` ma `@DataJpaTest` repository testy.
- [x] `inventory-service` ma `@DataJpaTest` repository testy.

### 4. Integracne testy

- [x] `customer-service` ma aspon jeden integracny test.
- [x] `harvest-service` ma aspon jeden integracny test.
- [x] `production-service` ma aspon jeden integracny test.
- [ ] `inventory-service` ma aspon jeden integracny test typu `@SpringBootTest` / `*IT`.

### 5. End-to-end prepojenie

- [x] Aspon jeden REST flow je napojeny na service a persistence vrstvu.
- [x] Realne je persistence napojena vo viacerych moduloch, nie len v jednom.

### 6. Async komunikacia

- [ ] Existuje komunikacia medzi 2 services cez Apache ActiveMQ Artemis.
- [ ] Producer zapisuje JSON spravy do queue.
- [ ] Consumer spravy asynchronne spracovava.
- [ ] Spravy sa ukladaju do databazy.
- [ ] Producer/consumer konfiguracia je v `application.yml`.
- [ ] Lokalne transakcie su osetrene na producer aj consumer strane.

### 7. Docker / Compose / README

- [x] Projekt ma `docker-compose.yml`.
- [x] Projekt ma Dockerfile aspon pre cast services.
- [ ] `docker-compose.yml` pokryva cely system alebo aspon vsetky planovane runtime komponenty pre M2.
- [ ] README obsahuje kompletne Docker build/run instrukcie pre `customer-service`.
- [x] README obsahuje Docker build/run instrukcie pre `harvest-service`.
- [ ] README obsahuje kompletne Docker build/run instrukcie pre `inventory-service`.
- [x] README obsahuje Docker build/run instrukcie pre `production-service`.
- [x] README obsahuje Swagger UI a OpenAPI odkazy pre vsetky 4 domenove services.
- [ ] README popisuje aj broker / supporting infrastructure, ak bude pridany Artemis.
- [ ] Po zavedeni Artemis je broker infrastructure doplnena do compose.
- [ ] Kratky architektonicky popis JMS flow je doplneny v README alebo `docs`.

### 8. OpenAPI

- [x] Projekt ma pridanu `springdoc-openapi` dependency.
- [x] `production-service` uz pouziva OpenAPI anotacie na controlleroch.
- [x] `harvest-service` ma konzistentne OpenAPI anotacie na vsetkych controlleroch a endpointoch.
- [x] `harvest-service` ma doplnene `@ApiResponses` na endpointoch.
- [x] `harvest-service` ma doplnene `@Schema` anotacie na hlavnych DTO triedach.
- [x] `harvest-service` ma zmysluplne summary/description texty bez placeholderov.
- [ ] `customer-service` ma konzistentne OpenAPI anotacie na vsetkych controlleroch a endpointoch.
- [ ] `inventory-service` ma konzistentne OpenAPI anotacie na vsetkych controlleroch a endpointoch.
- [ ] `production-service` ma skontrolovane a zjednotene OpenAPI popisy na vsetkych endpointoch.
- [ ] `production-service` ma doplnene `@ApiResponses` tam, kde este chybaju.
- [ ] `production-service` ma doplnene `@Schema` anotacie na hlavnych DTO triedach.
- [ ] `customer-service` ma doplnene `@ApiResponses` na endpointoch.
- [ ] `customer-service` ma doplnene `@Schema` anotacie na hlavnych DTO triedach.
- [ ] `inventory-service` ma doplnene `@ApiResponses` na endpointoch.
- [ ] `inventory-service` ma doplnene `@Schema` anotacie na hlavnych DTO triedach.
- [ ] Vsetky 4 domenove services maju pouzite aspon `@Tag` na controlleroch, `@Operation` na endpointoch, `@ApiResponses` na hlavnych endpointoch, `@Schema` na hlavnych DTO a zmysluplne summary/description texty bez placeholderov.
- [ ] OpenAPI ma byt dorobene "na vybornu", nie len minimalne: konzistentne nazvy endpointov, jasne popisane business operacie, rozumne popisane request/response DTO a bez modulov, kde by dokumentacia chybala alebo bola len ciastocna.

## Opravy po M1 review

### Hotove

- [x] `ApiError` je vytiahnuty do shared modulu.
- [x] Shared exception handling je centralizovany v `common-service`.
- [x] `.gitignore` ignoruje vnorene `target/`.
- [x] `production-service` nema DTO vo `WineRecipeService`.
- [x] `production-service` ma mapovanie vo facade vrstve.
- [x] `production-service` pouziva `server.servlet.context-path`.
- [x] `QualityEvaluationDto` uz pouziva enum odporucanie.
- [x] `harvest-service` ma upratane `HarvestRecordViewDto` na nested basic DTO pristup.
- [x] `inventory-service` ma exception mapping.

### Este otvorene

- [ ] `customer-service`: odstranit alebo prehodnotit `RepositoryConfig`, ak este existuje alebo je zbytocny.
- [ ] `customer-service`: zvazit vztah `Review -> Customer` namiesto samotneho `customerId`.
- [ ] `customer-service`: zvazit vztah `PurchaseRecord -> Customer` namiesto samotneho `customerId`.
- [ ] `customer-service`: zvazit bidirectional mapovanie cez `Customer -> reviews`.
- [ ] `customer-service`: zvazit bidirectional mapovanie cez `Customer -> purchaseRecords`.
- [ ] `customer-service`: doplnit reviews do `CustomerDto`, ak to chcete dorobit podla feedbacku.
- [ ] `customer-service`: doplnit purchase history do `CustomerDto`, ak to chcete dorobit podla feedbacku.
- [x] `harvest-service`: zjednotit delete existence checks vo vsetkych service triedach.
- [ ] `inventory-service`: rozhodnut, ci `ReservationStatus` ponechat alebo zjednodusit.
- [ ] `inventory-service`: ak `ReservationStatus` ostane, neumoznit klientovi nastavovat ho lubovolne.
- [ ] Skontrolovat `equals()` / `hashCode()` strategiu tam, kde dava zmysel business key.

## Najblizsie odporucane kroky

1. Doriesit integracny test pre `inventory-service`.
2. Rozhodnut dvojicu services pre Artemis a navrhnut queue flow.
3. Doinplementovat Artemis producer, consumer, DB ulozenie sprav a konfiguraciu v `application.yml`.
4. Rozsirit `docker-compose.yml` a README tak, aby pokryvali kompletne M2 spustenie.
5. Dorobit OpenAPI v `customer-service`, `inventory-service` a zjednotit `production-service` na rovnaky standard ako `harvest-service`.
6. Az potom riesit volitelne customer model/DTO refaktory z feedbacku po M1.

## Nice to have

Tieto body nie su kriticke minimum pre M2, ale vedia zlepsit kvalitu projektu a dojem z odovzdania.

### OpenAPI navyse

- [ ] Doplnenie `description` textov aj ku controllerom a jednotlivym operaciam, nie len `summary`.
- [ ] Zvazit `@Parameter` len tam, kde parameter naozaj nie je z nazvu endpointu dostatocne jasny.
- [ ] Doplnenie prikladov request/response DTO, ak sa rozhodnete ist viac do detailu.
- [ ] Zjednotenie stylu nazvov a textov medzi vsetkymi services, aby Swagger neposobil ako zmes 4 roznych stylov.

### Customer service refaktory

- [ ] Prejst z `customerId` na realny vztah na `Customer` v `Review`.
- [ ] Prejst z `customerId` na realny vztah na `Customer` v `PurchaseRecord`.
- [ ] Rozsirit `CustomerDto` o review historiu.
- [ ] Rozsirit `CustomerDto` o purchase history.

### Domain model a konzistencia

- [ ] Skontrolovat `equals()` / `hashCode()` strategiu v entitach a modeloch.
- [ ] Upratat pripadne zbytocne duplicitne validacie medzi REST a nizsimi vrstvami.
- [ ] Zjednotit delete/update existence checks napriec service vrstvami.

### Dokumentacia a infra

- [ ] Zjednotenie Docker navodov v README tak, aby stylovo a obsahovo sedeli napriec vsetkymi services.
