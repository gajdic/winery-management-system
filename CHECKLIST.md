# Project Checklist

Hlavny priebezne aktualizovany checklist pre cely projekt.

Aktualny stav bol naposledy skontrolovany proti repozitaru dna `2026-04-29`.

## M3 povinne body

Termin projektu: `2026-05-10 23:59`.
Termin odovzdania review: `2026-05-15 23:59`.

### 1. End-to-end prepojenie REST -> service -> persistence

- [x] `customer-service` ma REST endpointy napojene na facade/service vrstvu a JPA persistence.
- [x] `harvest-service` ma REST endpointy napojene na facade/service vrstvu a JPA persistence.
- [x] `production-service` ma REST endpointy napojene na facade/service vrstvu a JPA persistence.
- [x] `inventory-service` ma REST endpointy napojene na facade/service vrstvu a JPA persistence.
- [ ] Rucne overit hlavne REST flow cez Swagger alebo curl pre vsetky 4 services po starte cez Docker Compose.
- [ ] Doplnit alebo aktualizovat integracne testy tak, aby pokryvali najdolezitejsie pouzivatelske flow z REST API az do databazy.

### 2. Database seeding a clear/reset

- [ ] `customer-service` ma seed dat cez migracny nastroj alebo jednoznacny startup seeder.
- [ ] `harvest-service` ma seed dat cez migracny nastroj alebo jednoznacny startup seeder.
- [ ] `production-service` ma seed dat cez migracny nastroj alebo jednoznacny startup seeder.
- [ ] `inventory-service` ma seed dat cez migracny nastroj alebo jednoznacny startup seeder.
- [ ] Seed data posobia realisticky a nepouzivaju placeholder hodnoty typu `AAAAAA`.
- [ ] Existuje sposob, ako databazy vycistit alebo resetnut pred novym demo behom.
- [ ] README popisuje, ako sa seedovanie a pripadny clear/reset databaz spusta.

### 3. Security - OAuth 2 Resource Server

- [ ] Do vsetkych API services pridat Spring Security OAuth 2 Resource Server konfiguraciu.
- [ ] Pouzit seminarovy `client_id` a `client_secret` pre client aj resource servery.
- [ ] Nakonfigurovat issuer/JWK introspection podla seminaroveho OAuth provideru.
- [ ] Pouzit aspon dva rozne `test_*` scopes.
- [ ] Rozdelit scopes medzi metody tak, aby davali business zmysel, napr. citanie vs. zapis/admin operacie.
- [ ] `customer-service` endpointy maju nastavene scope pravidla.
- [ ] `harvest-service` endpointy maju nastavene scope pravidla.
- [ ] `production-service` endpointy maju nastavene scope pravidla.
- [ ] `inventory-service` endpointy maju nastavene scope pravidla.
- [ ] Verejne ponechat iba endpointy, ktore maju byt verejne, napr. health, OpenAPI docs alebo Swagger redirect podla dohody.
- [ ] Swagger/OpenAPI vie pracovat s OAuth tokenom, alebo README jasne popisuje ziskanie tokenu zo seminaroveho clienta.
- [ ] Rucne overit, ze request bez tokenu zlyha na chranenych endpointoch.
- [ ] Rucne overit, ze token bez potrebneho scope zlyha na endpointoch s vyssim opravnenim.
- [ ] Rucne overit, ze token so spravnym scope prejde.

### 4. Observability - Actuator, Prometheus, Grafana

- [ ] Do vsetkych services pridat `spring-boot-starter-actuator`.
- [ ] Do vsetkych services pridat Prometheus/Micrometer registry.
- [ ] `customer-service` exposeuje `/actuator/health` a `/actuator/prometheus`.
- [ ] `harvest-service` exposeuje `/actuator/health` a `/actuator/prometheus`.
- [ ] `production-service` exposeuje `/actuator/health` a `/actuator/prometheus`.
- [ ] `inventory-service` exposeuje `/actuator/health` a `/actuator/prometheus`.
- [ ] `docker-compose.yml` obsahuje Prometheus.
- [ ] Prometheus scrapeuje vsetky 4 services.
- [ ] `docker-compose.yml` obsahuje Grafanu.
- [ ] Grafana ma provisioning pre datasource, aby sa nemusela konfigurovat rucne po starte.
- [ ] Grafana ma preloaded dashboard zobrazujuci stav systemu.
- [ ] Dashboard ukazuje aspon health/up stav, request rate/latenciu a JVM alebo HTTP metriky pre services.
- [ ] README obsahuje URL pre actuator, Prometheus a Grafanu.

### 5. Deployment a CI

- [x] Projekt obsahuje `.gitlab-ci.yml`.
- [x] GitLab CI ma test jobs pre `customer-service`, `harvest-service`, `inventory-service` a `production-service`.
- [x] GitLab CI ma build jobs pre vsetky 4 domenove services.
- [x] GitLab CI ma Docker build kontrolu cez Kaniko pre vsetky 4 domenove services.
- [x] Overit v GitLabe, ze pipeline realne bezi na `main`/merge requestoch.
- [x] Overit, ze zlyhavajuce testy alebo build rozbiju pipeline.
- [x] README popisuje CI pipeline.
- [x] [OPTIONAL] Ak sa robi OpenShift/Kubernetes deployment, doplnit manifesty a README instrukcie. Inak v README jasne oznacit ako not implemented.

### 6. Showcase scenar

- [ ] Definovat minimalne jeden runnable scenar, ktory ukazuje realne pouzitie systemu.
- [ ] Scenar pokryva viac ako jeden service, ak to dava v domene zmysel.
- [ ] Scenar pracuje s autentifikaciou/tokenmi, ak vola chranene endpointy.
- [ ] Scenar je spustitelny skriptom alebo nastrojom ako Locust/JMeter/Postman/curl script.
- [ ] Scenar ma jasny ciel, napr. produkcny batch zvysi skladove zasoby cez messaging, alebo zakaznik vytvori objednavkovu/purchase historiu nad seed datami.
- [ ] Scenar ma popis ocakavanych vysledkov.
- [ ] README obsahuje navod na spustenie scenara.
- [ ] Rucne overit, ze scenar funguje po cistom `docker compose up --build`.

### 7. README a dokumentacia pre M3

- [ ] README obsahuje kompletny M3 runbook: build, start, seed/reset, token, Swagger, scenario, monitoring.
- [ ] README obsahuje OAuth scopes a priklady requestov s tokenom.
- [ ] README obsahuje zoznam service URL a portov.
- [ ] README obsahuje observability URL: actuator endpoints, Prometheus, Grafana.
- [ ] README obsahuje popis GitLab CI.
- [ ] README obsahuje informaciu, ci a ako bol pouzity GenAI pri vyvoji.
- [ ] README jasne rozlisuje povinne funkcionality a optional deployment.

### 8. Teamwork a GitLab proces

- [ ] V GitLabe je nastavene, ze merge requesty vyzaduju aspon 2 approvals.
- [ ] M3 praca prebieha cez merge requesty, nie priamymi pushmi do `main`.
- [ ] Kazdy clen commituje iba zmeny, ktore realne spravil.
- [ ] Pred deadline skontrolovat, ze hlavna vetva `main`/`master` obsahuje vsetko, lebo GitLab repo je jediny source of truth.
- [ ] V README alebo projektovej dokumentacii je uvedene pouzitie GenAI zdrojov a rozsah pouzitia.

### 9. Predodovzdavacia kontrola M3

- [ ] `mvn clean install` prechadza nad celym multi-module projektom.
- [ ] `docker compose up --build` nastartuje vsetky services a infrastructure komponenty.
- [ ] Vsetky services maju dostupny Swagger/OpenAPI.
- [ ] Chranene endpointy su bez tokenu odmietnute.
- [ ] Endpointy so spravnym tokenom a scope funguju.
- [ ] Seed/reset funguje po cistom starte.
- [ ] Prometheus vidi vsetky services ako `up`.
- [ ] Grafana dashboard po starte ukazuje data bez rucnej konfiguracie.
- [ ] Showcase scenar je spustitelny a ma ocakavany vysledok.
- [ ] GitLab pipeline na aktualnom `main`/`master` prechadza.

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
- [x] `inventory-service` ma aspon jeden integracny test typu `@SpringBootTest` / `*IT`.

### 5. End-to-end prepojenie

- [x] Aspon jeden REST flow je napojeny na service a persistence vrstvu.
- [x] Realne je persistence napojena vo viacerych moduloch, nie len v jednom.

### 6. Async komunikacia

- [x] Existuje komunikacia medzi 2 services cez Apache ActiveMQ Artemis.
- [x] Producer zapisuje JSON spravy do queue.
- [x] Consumer spravy asynchronne spracovava.
- [x] Spravy sa ukladaju do databazy.
- [x] Producer/consumer konfiguracia je v `application.yml`.
- [x] Lokalne transakcie su osetrene na producer aj consumer strane.

### 7. Docker / Compose / README

- [x] Projekt ma `docker-compose.yml`.
- [x] Projekt ma Dockerfile pre vsetky 4 domenove services.
- [x] `docker-compose.yml` pokryva cely system alebo aspon vsetky planovane runtime komponenty pre M2.
- [x] README obsahuje kompletne Docker build/run instrukcie pre `customer-service`.
- [x] README obsahuje Docker build/run instrukcie pre `harvest-service`.
- [x] README obsahuje kompletne Docker build/run instrukcie pre `inventory-service`.
- [x] README obsahuje Docker build/run instrukcie pre `production-service`.
- [x] README obsahuje Swagger UI a OpenAPI odkazy pre vsetky 4 domenove services.
- [x] README popisuje aj broker / supporting infrastructure, ak bude pridany Artemis.
- [x] Po zavedeni Artemis je broker infrastructure doplnena do compose.
- [x] Kratky architektonicky popis JMS flow je doplneny v README alebo `docs`.

### 8. OpenAPI

- [x] Projekt ma pridanu `springdoc-openapi` dependency.
- [x] `production-service` uz pouziva OpenAPI anotacie na controlleroch.
- [x] `harvest-service` ma konzistentne OpenAPI anotacie na vsetkych controlleroch a endpointoch.
- [x] `harvest-service` ma doplnene `@ApiResponses` na endpointoch.
- [x] `harvest-service` ma doplnene `@Schema` anotacie na hlavnych DTO triedach.
- [x] `harvest-service` ma zmysluplne summary/description texty bez placeholderov.
- [x] `customer-service` ma konzistentne OpenAPI anotacie na vsetkych controlleroch a endpointoch.
- [x] `inventory-service` ma konzistentne OpenAPI anotacie na vsetkych controlleroch a endpointoch.
- [x] `production-service` ma skontrolovane a zjednotene OpenAPI popisy na vsetkych endpointoch.
- [x] `production-service` ma doplnene `@ApiResponses` tam, kde este chybaju.
- [x] `production-service` ma doplnene `@Schema` anotacie na hlavnych DTO triedach.
- [x] `customer-service` ma doplnene `@ApiResponses` na endpointoch.
- [x] `customer-service` ma doplnene `@Schema` anotacie na hlavnych DTO triedach.
- [x] `inventory-service` ma doplnene `@ApiResponses` na endpointoch.
- [x] `inventory-service` ma doplnene `@Schema` anotacie na hlavnych DTO triedach.
- [x] Vsetky 4 domenove services maju pouzite aspon `@Tag` na controlleroch, `@Operation` na endpointoch, `@ApiResponses` na hlavnych endpointoch, `@Schema` na hlavnych DTO a zmysluplne summary/description texty bez placeholderov.
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

- [x] `customer-service`: odstranit alebo prehodnotit `RepositoryConfig`, ak este existuje alebo je zbytocny.
- [ ] `customer-service`: zvazit vztah `Review -> Customer` namiesto samotneho `customerId`.
- [ ] `customer-service`: zvazit vztah `PurchaseRecord -> Customer` namiesto samotneho `customerId`.
- [ ] `customer-service`: zvazit bidirectional mapovanie cez `Customer -> reviews`.
- [ ] `customer-service`: zvazit bidirectional mapovanie cez `Customer -> purchaseRecords`.
- [ ] `customer-service`: doplnit reviews do `CustomerDto`, ak to chcete dorobit podla feedbacku.
- [ ] `customer-service`: doplnit purchase history do `CustomerDto`, ak to chcete dorobit podla feedbacku.
- [x] `harvest-service`: zjednotit delete existence checks vo vsetkych service triedach.
- [x] `inventory-service`: rozhodnut, ci `ReservationStatus` ponechat alebo zjednodusit. (odstraneny 2026-04-24)
- [x] `inventory-service`: ak `ReservationStatus` ostane, neumoznit klientovi nastavovat ho lubovolne. (N/A — odstraneny)
- [ ] Skontrolovat `equals()` / `hashCode()` strategiu tam, kde dava zmysel business key.

## Najblizsie odporucane kroky

1. Pridat Spring Security OAuth 2 Resource Server do vsetkych 4 services a rozdelit endpointy medzi aspon 2 `test_*` scopes.
2. Pridat actuator + Prometheus metriky do services a rozsirit `docker-compose.yml` o Prometheus a Grafanu s provisioningom dashboardu.
3. Zjednotit database seeding/reset pre vsetky services a zdokumentovat ho v README.
4. Pripravit runnable showcase scenar a overit ho po cistom `docker compose up --build`.
5. Overit v GitLabe, ze merge requesty realne vyzaduju aspon 2 approvals, kedze to sa z lokalneho repozitara neda potvrdit.
6. Ak ostane cas, vratit sa k volitelnym customer model/DTO refaktorom z feedbacku po M1.

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

- [x] Zjednotenie Docker navodov v README tak, aby stylovo a obsahovo sedeli napriec vsetkymi services.

## Poznamky k finalnemu odovzdaniu

- Povinne M2 body su v repozitari momentalne pokryte.
- M3 checklist bol doplneny dna `2026-04-29`; vacsina M3 infrastruktury este nie je oznacena ako hotova.
- `mvn install` prechadza nad celym multi-module projektom.
- Messaging je implementovany, ale oplati sa ho este raz rucne overit v Docker Compose prostredi pred odovzdanim.
- GitLab workflow body (`2 approvals`, individual contribution) treba skontrolovat priamo v GitLabe, nie len lokalne.
