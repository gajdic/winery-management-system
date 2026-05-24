# Project Checklist

Hlavny priebezne aktualizovany checklist pre cely projekt.

Aktualny stav bol naposledy skontrolovany proti repozitaru dna `2026-05-19`.
Posledny systematicky audit checklistu proti kodu: `2026-05-24`.

## Audit `2026-05-24` - sumar po vsetkych merge-och

Top 3 merge commits su uz na `M4/reviews`:

| Merge | Commit | Autor | Datum |
|---|---|---|---|
| `M4/service-consistency-openapi` -> `M4/reviews` (OpenAPI cleanup + DTO/test refaktor) | `e07a629` | Jan Novak | `2026-05-24 15:50` |
| `production-service-inbound-ack` -> `M4/reviews` (ACK persistence v DB) | `e5a7c7d` | (tim) | `2026-05-24 15:13` |
| `scopes-extraction` -> `M4/reviews` (Luciin scope refaktor) | `7cf5245` | (tim) | predtym |

Z M4/service-consistency-openapi merge (e07a629) prislo 8 commitov ktore zatvorili **vela M1 a M2 review otvorenych bodov naraz**:
- `eead731` entity equals/hashcode by business key (M1 review otvoreny bod **vyriesene**)
- `1680538` unify delete/update existence checks (M1 review otvoreny bod **vyriesene**)
- `473a2f6` remove duplicated dto validations from services (**vyriesil weak point #1 z prezentacie!**)
- `d95360d` swagger constants in common-service (M2 nice-to-have **vyriesene**)
- `a05842c` schema annotations on apierror
- `939aca9` add openapi config to customer and inventory services
- `7a2cbda` openapi descriptions and examples in controllers
- `b418df1` fill in dto schema examples for customer ids

**Zostavajuci tech debt (po dnesnych merge-och):**
- Duplicita `common-service/config/SecurityScopes` + `common-service/openapi/SwaggerConstants` - obe tu su, scope literal cleanup nebol dokoncenu cez vsetky 4 services (15+ suborov stale importuje `SecurityScopes`). Build prejde (oba subory existuju), je to len suchy refactor follow-up.

**Final stav M3 must-fix bodov:**
- ✅ Customer-service Docker Compose runtime (`d0a0be6`)
- 🟡 Scope literals extraction - z casti hotove cez `7cf5245` (scopes-extraction), zvysny cleanup po dnesnom merge tu zostava
- ✅ Inbound ACK persistence (`2684bc7` + `e5a7c7d`)
- ✅ Showcase scenar - dokument `docs/DEMO_SCENARIO.md`
- 🔴 **Demo video - chyba, treba nahrat 24h pred obhajobou**
- ✅ CI pipeline green
- ✅ README GenAI disclosure (rozsirena dnes)
- ✅ Actuator/Swagger URLs dokumentovane
- ✅ Public endpointy konzistentne
- ✅ Prometheus + Grafana runtime overene

**8 z 10 hotove + 1 v progress + 1 chyba.**

---

## Audit `2026-05-20` - sumar zisteni

## Audit `2026-05-20` - sumar zisteni

Sumar zisteni z auditu vsetkych odskrtnutych a neodskrtnutych bodov:

### Hotove veci, ktore boli nespravne oznacene ako otvorene `[ ]`

- M1 review: **update existence checks vo service vrstvach** su uz zjednotene (`findById(id)` pred `update`/`deleteById` v `CustomerService`, `ReviewService`, `GrapeTypeService`, `HarvestRecordService`, `VineyardService` atd.).
- M1 review: **rozlisenie `IllegalArgumentException` vs. `ResourceNotFoundException`** je uz hotove - `BaseGlobalExceptionHandler` v `common-service` ma separate handlery (400 vs. 404).
- M1 review: **`equals()` / `hashCode()` strategia** - vsetky entity pouzivaju Lombok `@EqualsAndHashCode` alebo custom implementaciu (Reservation pouziva business keys: customerId, wineId, quantity, createdAt).

### Polozky `[x]`, ktore vyzaduju este overenie alebo maju nuansie

- M2 review: **"JMS queue names z jedneho miesta"** - queue names SU centralizovane, ale cez **`common-service/messaging/Queues.java`** konstanty (`Queues.BATCH_COMPLETED`, `Queues.STOCK_UPDATED`), **NIE** cez `application.yml` properties (`app.jms.queues.*`), ako konkretne navrhol cviciaci. V `application.yml` nie su duplicate hodnoty, ale architektonicky to mozno cviciaci hodnotuje inac. Treba sa pri obhajobe vediet obhajit, ze "queue names su z jedneho miesta - z Queues.java konstant" + povedat, ze cviciacov hint na `app.jms.queues.*` je nice-to-have ktory zatial nie je urobeny.

### Kriticke otvorene `[ ]` body, ktore treba urgentne dovyriesit pred obhajobou

1. ~~**Customer-service v Docker Compose nestartuje spravne**~~ **DEFINITIVNE VYRIESENE 2026-05-21** (po piatich vrstvach opravy + runtime overeni):

   **Vrstva 1 (OAuth/management konfiguracia):**
   - **Co bolo zle**: `docker-compose.yml` nenastavoval `SPRING_PROFILES_ACTIVE` pre `customer-service`, a v `customer-service/src/main/resources/application.yml` bola security a management konfiguracia umiestnena iba pod `seed` profile. Bez seed profilu sa OAuth resource server nenakonfiguroval a actuator endpointy neboli exposed.
   - **Fix**:
     - **`customer-service/src/main/resources/application.yml`**: security (`spring.security.oauth2.resourceserver` vratane `opaque-token`), `springdoc.swagger-ui.oauth` aj `management.*` presunute z `seed` profile do default. V `seed` profile zostal iba `spring.liquibase.contexts: seed`. Tym sa customer-service zosuladil s konvenciou `harvest-service`, `production-service` a `inventory-service`.
     - **`docker-compose.yml`**: do `customer-service` environment pridane `SPRING_PROFILES_ACTIVE: ${CUSTOMER_SERVICE_PROFILES:-seed}` - konzistentne s ostatnymi services.

   **Vrstva 2 (Liquibase changelog path bug):**
   - **Co bolo zle**: po oprave vrstvy 1 service nabehol az do Hibernate validacie a padol s `Schema validation: missing table [customer]`. Pricina: `application.yml` mal `change-log: classpath:db/changelog/db.changelog-master.xml`, ale skutocny subor sa volal `changelog-master.xml` (bez `db.` prefixu).
   - **Fix**: v `customer-service/src/main/resources/application.yml` zmena `change-log: classpath:db/changelog/db.changelog-master.xml` -> `change-log: classpath:db/changelog/changelog-master.xml`.
   - **Hierarchia changelog suborov**: `changelog-master.xml` zahrnuje `changes/001-create-tables.xml` (vzdy) a `changes/002-insert-seed-data.xml` (`context="seed"`, takze sa spusti len pri `seed` profile, co docker-compose nastavuje ako default).

   **Vrstva 3 (chybajuca Liquibase Spring Boot autoconfigure):**
   - **Co bolo zle**: aj po oprave vrstvy 2 sa Liquibase v logoch vobec neukazoval - ani lockservice, ani changelog scan. Vznikla rovnaka chyba `Schema validation: missing table [customer]`. Pricina: `customer-service/pom.xml` mal dependency `org.liquibase:liquibase-core` (holu kniznicu **bez** Spring Boot autoconfigure), zatial co ostatne 3 services maju `org.springframework.boot:spring-boot-starter-liquibase` (ktore obsahuje `LiquibaseAutoConfiguration` a zabezpeci, ze Liquibase migrate bezi PRED Hibernate). Bez autoconfigure Spring Boot ziadne migracie nespustil - Liquibase JAR bol len v classpath ale necinny.
   - **Preco bola tato vrstva skryta**: pred vrstvou 1 OAuth padol skor, pred vrstvou 2 sa myslelo ze chyba je len v `change-log` path. Az pri starostlivej analyze logov (uplna absencia `liquibase.*` riadkov) bolo jasne, ze Spring Boot autoconfigure pre Liquibase vobec nebezi. Cviciaci v M3 review tuto chybu nemohol vidiet - service nikdy nenastartoval dostatocne dlho, aby sa odhalila.
   - **Fix**: v `customer-service/pom.xml` zmena:
     ```xml
     <!-- pred -->
     <dependency>
       <groupId>org.liquibase</groupId>
       <artifactId>liquibase-core</artifactId>
     </dependency>
     <!-- po -->
     <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-liquibase</artifactId>
     </dependency>
     ```
   - Tym je customer-service zosuladeny s pom.xml konvenciou harvest/production/inventory services.

   **Vrstva 4 (neplatne UUID v seed datach):**
   - **Co bolo zle**: po oprave vrstvy 3 Liquibase konecne zacal aplikovat changesety, ale changeset `002-insert-seed-data.xml` padol na inserte do `review` tabulky s chybou `Data conversion error converting "w1111111-1111-1111-1111-111111111111"`. Pricina: seed data obsahovali UUID s neplatnymi znakmi - `w` prefix pre wine IDs (`w1111111-...`, `w2222222-...`, `w3333333-...`) a `p` prefix pre purchase record IDs (`p1111111-...` az `p4444444-...`). UUID musi obsahovat iba hex znaky (`0-9`, `a-f`).
   - **Fix v `customer-service/src/main/resources/db/changelog/changes/002-insert-seed-data.xml`**:
     - Wine IDs zjednotene s production-service `wine_recipes` UUID (zaroven dava cross-service demo zmysel): `w1111111-...` -> `11111111-1111-4111-8111-111111111111` (Moravian Riesling), `w2222222-...` -> `22222222-2222-4222-8222-222222222222` (Palava Selection), `w3333333-...` -> `33333333-3333-4333-8333-333333333333` (Frankovka Reserve).
     - Purchase record IDs prefixom `eeeeeeee` (rozlisitelne od customer/review IDs): `p1111111-...` -> `eeeeeeee-1111-4111-8111-111111111111` atd.
     - Cele UUID teraz pouzivaju format `v4` (`4` na 13. pozicii, `8` na 17. pozicii) konzistentne s ostatnymi services.

   **Vrstva 5 (mismatch column type medzi entitou a Liquibase):**
   - **Co bolo zle**: po oprave vrstvy 4 Liquibase uspesne aplikoval oba changesety (`Update has been successful. Rows affected: 11`), ale Hibernate `ddl-auto: validate` zhasol s `Schema validation: wrong column type encountered in column [price_per_bottle] in table [purchase_record]; found [decimal (Types#DECIMAL)], but expecting [float(53) (Types#FLOAT)]`. Pricina: JPA entita `PurchaseRecord.pricePerBottle` je `double` (Hibernate to mapuje na `FLOAT(53)` / `DOUBLE PRECISION`), ale Liquibase vytvorila stlpec ako `DECIMAL(10,2)`.
   - **Fix v `customer-service/src/main/resources/db/changelog/changes/001-create-tables.xml`**: `<column name="price_per_bottle" type="DECIMAL(10,2)">` -> `<column name="price_per_bottle" type="DOUBLE">`. Pragmaticky rychly fix pred obhajobou - mensi riziko nez refaktor JPA entity.
   - **Post-defense nice-to-have**: zmenit `double pricePerBottle` v `PurchaseRecord` entite na `BigDecimal` (lepsie pre financie, vyhne sa floating point precision issues). To by vyzadovalo zmenu v DTO, mapperoch a testoch - kazda zmena ma byt sucastou samostatneho refaktor MR.

   **Co teraz funguje (po vsetkych pätich vrstvach) - DEFINITIVNE OVERENE 2026-05-21:**
   - Customer-service v Docker Compose nabehne korektne za ~41 sekund.
   - Liquibase aplikoval oba changesety: `001-create-tables.xml` (vytvoril `customer`, `review`, `purchase_record` tabulky) + `002-insert-seed-data.xml` (`Rows affected: 11` - 3 customers + 4 reviews + 4 purchase records).
   - Hibernate schema validation prebehla bez chyb.
   - Tomcat startol na porte 8084, actuator expose-uje 5 endpointov.
   - `DispatcherServlet` inicializovany, container ostava `Up` (overene cez `docker ps -a`).
   - `curl http://localhost:8084/actuator/health` -> `200 OK`.
   - `curl http://localhost:8084/customers` (bez tokenu) -> `401 Unauthorized` (OAuth resource server validuje).
   - Customer-service uz nikdy nepadne so `Schema validation: missing table` ani `wrong column type`.

2. ~~**Scope literaly nie su extrahovane do konstant**~~ **VYRESENE 2026-05-22** (potvrdene auditom, M3 review must-fix):
   - `customer-service` ma hardcoded `"test_read"` / `"test_write"` v 12+ miestach (SecurityConfig, vsetky 3 controllery + testy).
   - `inventory-service` ma hardcoded `"test_read"` / `"test_write"` v 18+ miestach (SecurityConfig + StockController + ReservationController).
   - `harvest-service` a `production-service` maju lokalne konstanty `READ_SCOPE` / `WRITE_SCOPE` v `SecurityConfig`, **ale ich controllery stale pouzivaju hardcoded retazce** v `@SecurityRequirement` anotaciach.
   - V `common-service` nie su zdielane scope konstanty.
   - **Fix**: pridat `ScopeConstants` triedu do `common-service` so `READ_SCOPE` a `WRITE_SCOPE`, a pouzit ich naprieč vsetkymi 4 services v SecurityConfig + controlleroch + testoch.

3. ~~**`production-service` nezapisuje inbound ACK do DB**~~ **VYRIESENE 2026-05-22** (potvrdene auditom):
   - Tabulka `inbound_message_log` v Liquibase changeset `001_create_tables.xml` v production-service uz existuje.
   - `ProductionBatchCompletedProducer.java` riadky 63-67: ACK je iba prijaty a validovany (`jmsTemplate.receiveSelectedAndConvert`), ale nikde sa neuklada do `inbound_message_log`.
   - **Fix**: po validacii ACK volat `inboundMessageLogRepository.save(...)` v `ProductionBatchCompletedProducer`.

### Doplnujuce pozorovania

- `JmsConfig` je duplicitne v `production-service/config/JmsConfig.java` a `inventory-service/config/JmsConfig.java` - obe verzie su identicke. Bod "vytiahnut JmsConfig do common-service" je teda priam vhodny refaktor (zatial otvoreny).
- `jwtDecoder` bean je duplikovany v `SecurityConfig` vsetkych 4 services s rovnakou implementaciou - tiez vhodny refaktor (zatial otvoreny).
- DTO inheritance nie je nikde - vsetky `Create*Dto` / `Update*Dto` / `View*Dto` su samostatne triedy bez spolocneho baseu (otvoreny nice-to-have).
- Duplicate validation medzi REST `@Valid` a service-level explicit `if/throw` stale existuje (napr. `ReviewService`). Toto je tema otazky cviciaceho pri obhajobe.

---

## M3 povinne body

Termin projektu: `2026-05-10 23:59`.
Termin odovzdania review: `2026-05-15 23:59`.

### 1. End-to-end prepojenie REST -> service -> persistence

- [x] `customer-service` ma REST endpointy napojene na facade/service vrstvu a JPA persistence.
- [x] `harvest-service` ma REST endpointy napojene na facade/service vrstvu a JPA persistence.
- [x] `production-service` ma REST endpointy napojene na facade/service vrstvu a JPA persistence.
- [x] `inventory-service` ma REST endpointy napojene na facade/service vrstvu a JPA persistence.
- [x] Rucne overit hlavne REST flow cez Swagger alebo curl pre vsetky 4 services po starte cez Docker Compose.
- [ ] Doplnit alebo aktualizovat integracne testy tak, aby pokryvali najdolezitejsie pouzivatelske flow z REST API az do databazy.

### 2. Database seeding a clear/reset

- [x] `customer-service` ma seed dat cez migracny nastroj alebo jednoznacny startup seeder.
- [x] `harvest-service` ma seed dat cez migracny nastroj alebo jednoznacny startup seeder.
- [x] `production-service` ma seed dat cez migracny nastroj alebo jednoznacny startup seeder.
- [x] `inventory-service` ma seed dat cez migracny nastroj alebo jednoznacny startup seeder.
- [x] Seed data posobia realisticky a nepouzivaju placeholder hodnoty typu `AAAAAA`.
- [x] Existuje sposob, ako databazy vycistit alebo resetnut pred novym demo behom.
- [x] README popisuje, ako sa seedovanie a pripadny clear/reset databaz spusta.

### 3. Security - OAuth 2 Resource Server

- [x] Do vsetkych API services pridat Spring Security OAuth 2 Resource Server konfiguraciu.
- [x] Pouzit seminarovy OAuth client nastavenia pre Swagger/client flow tam, kde ich service explicitne potrebuje.
- [x] Nakonfigurovat issuer/JWK introspection podla seminaroveho OAuth provideru.
- [x] Pouzit aspon dva rozne `test_*` scopes.
- [x] Rozdelit scopes medzi metody tak, aby davali business zmysel, napr. citanie vs. zapis/admin operacie.
- [x] `customer-service` endpointy maju nastavene scope pravidla.
- [x] `harvest-service` endpointy maju nastavene scope pravidla.
- [x] `production-service` endpointy maju nastavene scope pravidla.
- [x] `inventory-service` endpointy maju nastavene scope pravidla.
- [x] Verejne ponechat iba endpointy, ktore maju byt verejne, napr. health, OpenAPI docs alebo Swagger redirect podla dohody.
- [x] `customer-service` ma pridane `/actuator/**` medzi public endpointy, aby Prometheus vedel scrapeovat metriky bez tokenu.
- [x] Swagger/OpenAPI vie pracovat s OAuth tokenom, alebo README jasne popisuje ziskanie tokenu zo seminaroveho clienta.
- [x] Rucne overit, ze request bez tokenu zlyha na chranenych endpointoch.
- [x] Rucne overit, ze token bez potrebneho scope zlyha na endpointoch s vyssim opravnenim.
- [x] Rucne overit, ze token so spravnym scope prejde.

### 4. Observability - Actuator, Prometheus, Grafana

- [x] Do vsetkych services pridat `spring-boot-starter-actuator`.
- [x] Do vsetkych services pridat Prometheus/Micrometer registry.
- [x] `customer-service` exposeuje `/actuator/health` a `/actuator/prometheus`.
- [x] `harvest-service` exposeuje `/actuator/health` a `/actuator/prometheus`.
- [x] `production-service` exposeuje `/actuator/health` a `/actuator/prometheus`.
- [x] `inventory-service` exposeuje `/actuator/health` a `/actuator/prometheus`.
- [x] `docker-compose.yml` obsahuje Prometheus.
- [x] Prometheus je nakonfigurovany tak, aby automaticky scrapeoval vsetky 4 services.
- [x] `docker-compose.yml` obsahuje Grafanu.
- [x] Grafana ma provisioning pre datasource, aby sa nemusela konfigurovat rucne po starte.
- [x] Grafana ma preloaded dashboard zobrazujuci stav systemu.
- [x] Dashboard ukazuje aspon health/up stav, request rate/latenciu a JVM alebo HTTP metriky pre services.
- [x] README obsahuje URL pre actuator, Prometheus a Grafanu.

### 5. Deployment a CI

- [x] Projekt obsahuje `.gitlab-ci.yml`.
- [x] GitLab CI ma test jobs pre `customer-service`, `harvest-service`, `inventory-service` a `production-service`.
- [x] GitLab CI ma build jobs pre vsetky 4 domenove services.
- [x] GitLab CI ma Docker build kontrolu cez Kaniko pre vsetky 4 domenove services.
- [x] Overit v GitLabe, ze pipeline realne bezi na `main`/merge requestoch. *(potvrdene 2026-05-21: MR `M4/reviews` pipeline `#552446` passed)*
- [x] Overit, ze zlyhavajuce testy alebo build rozbiju pipeline.
- [x] README popisuje CI pipeline.
- [x] [OPTIONAL] Ak sa robi OpenShift/Kubernetes deployment, doplnit manifesty a README instrukcie. Inak v README jasne oznacit ako not implemented.

### 6. Showcase scenar

- [x] Definovat minimalne jeden runnable scenar, ktory ukazuje realne pouzitie systemu.
- [x] Scenar pokryva viac ako jeden service, ak to dava v domene zmysel.
- [x] Scenar pracuje s autentifikaciou/tokenmi, ak vola chranene endpointy.
- [x] Scenar je spustitelny skriptom alebo nastrojom ako Locust/JMeter/Postman/curl script.
- [x] Scenar ma jasny ciel, napr. produkcny batch zvysi skladove zasoby cez messaging, alebo zakaznik vytvori objednavkovu/purchase historiu nad seed datami.
- [x] Scenar ma popis ocakavanych vysledkov.
- [x] README obsahuje navod na spustenie scenara.
- [x] Rucne overit, ze scenar funguje po Docker Compose starte s produkcnym seed profilom.

### 7. README a dokumentacia pre M3

- [x] README obsahuje kompletny M3 runbook: build, start, seed/reset, token, Swagger, scenario, monitoring.
- [x] README obsahuje OAuth scopes a priklady requestov s tokenom.
- [x] README obsahuje zoznam service URL a portov.
- [x] README obsahuje observability URL: actuator endpoints, Prometheus, Grafana.
- [x] README obsahuje popis GitLab CI.
- [x] README obsahuje informaciu, ci a ako bol pouzity GenAI pri vyvoji.
- [x] README jasne rozlisuje povinne funkcionality a optional deployment.

### 8. Teamwork a GitLab proces

- [x] V GitLabe je nastavene, ze merge requesty vyzaduju aspon 2 approvals.
- [x] M3 praca prebieha cez merge requesty, nie priamymi pushmi do `main`.
- [x] Kazdy clen commituje iba zmeny, ktore realne spravil.
- [x] Pred deadline skontrolovat, ze hlavna vetva `main`/`master` obsahuje vsetko, lebo GitLab repo je jediny source of truth.
- [x] V README alebo projektovej dokumentacii je uvedene pouzitie GenAI zdrojov a rozsah pouzitia.

### 9. Predodovzdavacia kontrola M3

- [x] `mvn clean install` prechadza nad celym multi-module projektom.
- [x] Docker Compose stack nastartuje vsetky services a infrastructure komponenty.
- [x] Vsetky services maju dostupny Swagger/OpenAPI.
- [x] Chranene endpointy su bez tokenu odmietnute.
- [x] Endpointy so spravnym tokenom a scope funguju.
- [x] Seed/reset funguje po cistom starte.
- [x] Prometheus vidi vsetky services ako `up`.
- [x] Grafana dashboard po starte ukazuje data bez rucnej konfiguracie.
- [x] Odstranit nahodne OneNote `.onetoc2` subory z `monitoring/`, ak maju byt trackovane v Gite.
- [x] Showcase scenar je spustitelny a ma ocakavany vysledok.
- [x] GitLab pipeline na aktualnom `main`/`master` prechadza.

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
- [~] OpenAPI ma byt dorobene "na vybornu", nie len minimalne: konzistentne nazvy endpointov, jasne popisane business operacie, rozumne popisane request/response DTO a bez modulov, kde by dokumentacia chybala alebo bola len ciastocna. *(audit 2026-05-21: OpenAPI je FUNKCNE kompletny v 4 services - vsetky maju @Tag, @Operation+description, @ApiResponses, DTOs maju @Schema. Hlavna nezrovnalost je v DRY: `common-service/openapi/SwaggerConstants.java` UZ EXISTUJE so vsetkym potrebnym (`SECURITY_SCHEME_NAME`, `READ_SCOPE`/`WRITE_SCOPE`, `ResponseCodes.*`, `ResponseDescriptions.*`), ale ho POUZIVA IBA `inventory-service` (88x). Ostatne 3 services maju hardcoded literaly: `customer-service` 34 scope + 82 response codes, `harvest-service` 24 scope + 115 response codes, `production-service` 2 scope + 67 response codes.)*

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
- [x] `customer-service`: `Review` uz pouziva vztah `Review -> Customer` namiesto samotneho `customerId`.
- [x] `customer-service`: `PurchaseRecord` uz pouziva vztah `PurchaseRecord -> Customer` namiesto samotneho `customerId`.
- [x] `customer-service`: `Customer` uz ma bidirectional mapovanie cez `reviews`.
- [x] `customer-service`: `Customer` uz ma bidirectional mapovanie cez `purchaseRecords`.
- [x] `customer-service`: `CustomerDto` uz obsahuje reviews.
- [x] `customer-service`: `CustomerDto` uz obsahuje purchase history / `purchaseRecords`.
- [x] `harvest-service`: zjednotit delete existence checks vo vsetkych service triedach.
- [x] Zjednotit aj update existence checks vo service vrstvach, aby sa nespoliehali na `IllegalArgumentException` z repository update operacii. *(audit 2026-05-20: `findById(id)` sa vola pred `update`/`deleteById` vo vsetkych service triedach)*
- [x] Rozlisit `IllegalArgumentException` pre nevalidny vstup od `ResourceNotFoundException` pre nenajdeny zaznam. *(audit 2026-05-20: `BaseGlobalExceptionHandler` v `common-service` ma separate handlery - 400 pre IllegalArgumentException, 404 pre ResourceNotFoundException)*
- [x] `inventory-service`: rozhodnut, ci `ReservationStatus` ponechat alebo zjednodusit. (odstraneny 2026-04-24)
- [x] `inventory-service`: ak `ReservationStatus` ostane, neumoznit klientovi nastavovat ho lubovolne. (N/A — odstraneny)
- [x] Skontrolovat `equals()` / `hashCode()` strategiu tam, kde dava zmysel business key. *(audit 2026-05-20: vsetky entity pouzivaju Lombok `@EqualsAndHashCode`; `Reservation` ma custom equals/hashCode s business keys: customerId, wineId, quantity, createdAt)*
- [x] `EntityBase` je uz v `common-service`.
- [ ] Zvazit, ci duplicitu medzi create/update/detail DTO nema zmysel redukovat cez dedicnost alebo spolocne basic DTO typy.
- [ ] Upratat duplicitne validacie medzi REST a service vrstvami tam, kde nepridavaju novu business ochranu.
- [x] Projekt uz realne pouziva Lombok na DTO, entity aj messaging triedy.

## Opravy po M2 review

### Povinne alebo odporucane opravy s dopadom na hodnotenie

- [~] JMS queue names konfigurovat z jedneho miesta, nie duplicitne v `application.yml` a `Queues.java`. *(audit 2026-05-20: queue names su centralizovane v `common-service/messaging/Queues.java` ako Java konstanty; v `application.yml` nie su duplicate hodnoty. Cviciacov konkretny hint pouzit `app.jms.queues.batch-completed` v `application.yml` zatial implementovany **nie je**.)*
- [ ] Zaviest napr. `app.jms.queues.batch-completed=batch-completed-queue` (cviciaco-specificky hint, optional).
- [ ] Zaviest napr. `app.jms.queues.stock-updated=stock-updated-queue` (cviciaco-specificky hint, optional).
- [x] Upravit producer/consumer kod tak, aby queue names cital z konfiguracie. *(audit 2026-05-20: producer/consumer kod uz cita z `Queues.BATCH_COMPLETED` / `Queues.STOCK_UPDATED` konstant, ziadne hardcoded retazce)*
- [x] Upravit testy messagingu po presune queue names do konfiguracie. *(audit 2026-05-20: testy pouzivaju `Queues.*` konstanty)*
- [x] `production-service` spracuje ACK z `inventory-service` po update stocku.
- [x] `production-service` viaze uspesnost messaging flow na prijatie validneho ACK, nie iba na odoslanie JMS spravy.
- [x] `production-service` ma ulozit inbound ACK message do databazy. *(audit 2026-05-20: tabulka `inbound_message_log` v Liquibase changeset existuje, ale `ProductionBatchCompletedProducer` ACK iba validuje, nezapisuje ho do DB)*
- [x] Doplnit test uspesneho ACK flow.
- [x] Doplnit test pre pripad, ked ACK nepride alebo ACK nesedi s odoslanym batchom.
- [x] V `docker-compose.yml` nepouzivat image tagy `latest`; infrastructure aj lokalne service images maju fixne tagy.
- [x] Odstranit nepotrebnu PostgreSQL dependency z `harvest-service/pom.xml`, kedze aktualny runtime pouziva H2.

### Nice to have z M2 review

- [ ] Zvazit vytiahnutie opakovanych OpenAPI kodov a popisov do triedy typu `SwaggerConstants`.
- [ ] Zvazit explicitnu dedicated Docker network v `docker-compose.yml`.
- [ ] Zvazit presun hardcoded secrets z `docker-compose.yml` do `.env`.

## Najblizsie odporucane kroky

Prioritizovane podla auditu `2026-05-20` (s ohladom na nadchadzajucu finalnu obhajobu):

1. ~~**KRITICKY**: Opravit `customer-service` runtime v Docker Compose.~~ **DEFINITIVNE HOTOVE 2026-05-21** - po piatich vrstvach opravy (application.yml profile reorganizacia + docker-compose env + Liquibase changelog path + Spring Boot starter dependency + UUID v seed + column type). Runtime overene: vsetky 4 services su `Up`, vsetky `/actuator/health` vracaju 200, chranene endpointy vracaju 401.
2. ~~**KRITICKY**: Vytiahnut scope konstanty (`READ_SCOPE = "test_read"`, `WRITE_SCOPE = "test_write"`) do `common-service` a pouzit ich naprieč vsetkymi 4 services (SecurityConfig + controllery + testy). Najma `customer-service` a `inventory-service` maju hardcoded literaly v 30+ miestach.~~ **VYRESENE 2026-05-22**
3. ~~**KRITICKY**: Implementovat ulozenie inbound ACK do `inbound_message_log` tabulky v `production-service` (M2 review still open). Tabulka uz existuje cez Liquibase, treba len pridat repository volanie v `ProductionBatchCompletedProducer`.~~ **VYRESENE 2026-05-22**
4. ~~Otvorit `http://localhost:9090/targets` v prehliadači a overit, ze Prometheus scrapuje vsetky 4 services ako `up`. Tiez otvorit Grafana dashboard `http://localhost:3000` (admin/admin) a overit, ze panely zobrazuju realne data.~~ **HOTOVE 2026-05-21** - vsetkych 5 targetov UP, oba Grafana dashboardy preloaded a zobrazuju realne metriky vratane HTTP request rate spike z manualnych curl testov.
5. Spustit kompletny showcase scenar (`scripts/showcase.ps1`) end-to-end po cistom starte a overit zvysenie stocku.
6. ~~Po vyriesenych must-fix bodoch overit, ze CI pipeline na `main`/MR-i prejde green.~~ **HOTOVE 2026-05-21** - MR `M4/reviews -> main`, pipeline `#552446` passed na commite `d0a0be63`. Vsetky 3 jobs zelene (test + build + docker_build).
7. Nahrat demo video showcase scenara a pushnut link do repa (najneskor 24h pred obhajobou).
8. Pripadne sa este vratit k nice-to-have refaktorom z M3 review: extract `JmsConfig` a `jwtDecoder` do `common-service`, DTO inheritance, Harvest mapping zjednodusenie.

### Bonus oprava 2026-05-21

Pri debuggingu customer-service Docker Compose problemu boli odhalene a opravene aj nasledujuce **dovtedy skryte buggy v repe**, ktore by pri obhajobe pravdepodobne vyplavali tiez:

- Customer-service nikdy spravne **nepouzival Liquibase** (mal `liquibase-core` artifact bez Spring Boot autoconfigure namiesto `spring-boot-starter-liquibase`).
- Path k Liquibase changelogu bol nesprávny (`db.changelog-master.xml` namiesto `changelog-master.xml`).
- Seed data obsahovali **neplatne UUID** (s prefixami `w` a `p` ktore nie su hex).
- DB schema `price_per_bottle` mala typ `DECIMAL(10,2)` zatial co JPA entita pouziva Java `double` (mismatch).
- Bonus z UUID opravy: `wine_id` v customer-service teraz zhoduje s `wine_recipes.id` v production-service - cross-service demo data davaju zmysel (Moravian Riesling, Palava Selection, Frankovka Reserve).

## Nice to have

Tieto body nie su kriticke minimum pre M2, ale vedia zlepsit kvalitu projektu a dojem z odovzdania.

### OpenAPI navyse

- [ ] Doplnenie `description` textov aj ku controllerom a jednotlivym operaciam, nie len `summary`.
- [ ] Zvazit `@Parameter` len tam, kde parameter naozaj nie je z nazvu endpointu dostatocne jasny.
- [ ] Doplnenie prikladov request/response DTO, ak sa rozhodnete ist viac do detailu.
- [ ] Zjednotenie stylu nazvov a textov medzi vsetkymi services, aby Swagger neposobil ako zmes 4 roznych stylov.
- [ ] Zvazit `SwaggerConstants` alebo podobny shared helper pre casto opakovane response kody a popisy.

### Customer service refaktory

- [x] `Review` uz pouziva realny vztah na `Customer`.
- [x] `PurchaseRecord` uz pouziva realny vztah na `Customer`.
- [x] `CustomerDto` uz obsahuje review historiu.
- [x] `CustomerDto` uz obsahuje purchase history.

### Domain model a konzistencia

- [x] Skontrolovat `equals()` / `hashCode()` strategiu v entitach a modeloch. *(audit 2026-05-20: hotove, viz horna sekcia opravy po M1 review)*
- [ ] Upratat pripadne zbytocne duplicitne validacie medzi REST a nizsimi vrstvami. *(audit 2026-05-20: stale otvorene - napr. `ReviewService` aj `CustomerService` maju explicit if/throw validacie pre polia, ktore su uz validovane na DTO cez `@Valid`. Toto je presne otazka cviciaceho pri obhajobe.)*
- [x] Zjednotit delete/update existence checks napriec service vrstvami. *(audit 2026-05-20: hotove, viz horna sekcia)*
- [x] `EntityBase` je zdielany z `common-service`, nie je uz len v jednom module.
- [ ] Zvazit, ci `production-service` nema pri referencii na harvest data drzat skor externe ID nez denormalizovane atributy, ak by to znizilo riziko nekonzistencie.
- [ ] Zvazit zjednotenie casovych typov tam, kde sa miesa `Instant` a `LocalDateTime`, a vediet obhajit aktualnu volbu.

### Dokumentacia a infra

- [x] Zjednotenie Docker navodov v README tak, aby stylovo a obsahovo sedeli napriec vsetkymi services.

## Poznamky k finalnemu odovzdaniu

- Povinne M2 body su v repozitari momentalne pokryte.
- M3 checklist bol naposledy aktualizovany po novych zmenach dna `2026-05-10`.
- `mvn clean install` prechadza nad celym multi-module projektom.
- `mvn -pl harvest-service -am test` prechadza po odstraneni nepotrebnej PostgreSQL dependency z `harvest-service`.
- `docker compose config --quiet` prechadza, teda Compose konfiguracia je syntakticky validna. Naposledy overene `2026-05-09`.
- Docker Compose runtime bol overeny po oprave Artemis image na `apache/artemis:2.53.0`; `customer-service`, `harvest-service`, `production-service` aj `inventory-service` vratili health `UP`.
- Showcase scenar bol runtime overeny cez `scripts/showcase.ps1`; produkcny batch `cccccccc-cccc-4ccc-8ccc-cccccccccccc` presiel do stavu `COMPLETED` a inventory stock sa zvysil o ocakavanych `970` flias cez JMS.
- Monitoring konfiguracia obsahuje Prometheus scrape config pre vsetky 4 services a Grafana provisioning s dvoma dashboardmi.
- Nahodne OneNote `.onetoc2` subory z `monitoring/` boli odstranene znovu dna `2026-05-09`.
- GitLab pipeline na merge requeste bola po retry uspesna.
- Messaging je implementovany, ale oplati sa ho este raz rucne overit v Docker Compose prostredi pred odovzdanim.
- GitLab workflow body (`2 approvals`, individual contribution) treba skontrolovat priamo v GitLabe, nie len lokalne.

## Prehlad review zdrojov

- [x] M1 review: `Reviews/M1/M1_gamma.md` (`11.5` bodu)
- [x] M2 review: `Reviews/M1/M2_gamma.md` (`11.5` bodu)
- [x] M3 review: `Reviews/M1/M3_gamma.md` (`12` bodov)
- [ ] Pri prioritizacii pracovat podla tohto zjednoteneho checklistu, nie len podla jednotlivych review suborov.

## Opravy po M3 review `gamma`

Feedback bol zapisany z review suboru `Reviews/M1/M3_gamma.md`. Review hodnotilo commit `fdcf1b94ddd6c8803b10ef44d0bb98d6261aa705` a projekt dostal `12` bodov. Ciel tejto sekcie je mat jasny defense checklist: co opravit, co overit runtime behom a na ake otazky byt pripraveny.

### Kriticke pred obhajobou / finalnym handoffom

- [x] README obsahuje GenAI usage disclosure (`README.md`, sekcia `GenAI Usage Disclosure`).
- [x] **Runtime overene 2026-05-21**: Docker Compose start vsetkych 4 microservices funguje. Po cistom `docker compose up --build`:
  - vsetky 4 services su `Up` v `docker ps -a` (overene po >11 minutach behu),
  - health endpointy vsetkych 4 services vracaju `200 OK`:
    - `http://localhost:8081/actuator/health` (harvest, bez context path),
    - `http://localhost:8082/api/actuator/health` (production, `/api` context),
    - `http://localhost:8083/inventory-service/api/actuator/health` (inventory, `/inventory-service/api` context),
    - `http://localhost:8084/actuator/health` (customer, bez context path),
  - chranene endpointy bez OAuth tokenu vracaju `401 Unauthorized`,
  - OpenAPI/Swagger endpointy su verejne dostupne.
- [x] **VYRIESENE 2026-05-20**: Opravit `customer-service` runtime cez Docker Compose:
  - V `customer-service/src/main/resources/application.yml` presunute `spring.security.oauth2.resourceserver` (vratane `opaque-token`), `springdoc.swagger-ui.oauth` a `management.*` zo `seed` profile do default profile. V `seed` profile ostalo iba `spring.liquibase.contexts: seed`. Zosuladene s konvenciou ostatnych services.
  - V `docker-compose.yml` pridane `SPRING_PROFILES_ACTIVE: ${CUSTOMER_SERVICE_PROFILES:-seed}` pre `customer-service` (konzistentne s `harvest-service` a `inventory-service`).
  - **Runtime overene 2026-05-20**: `curl http://localhost:8084/actuator/health` -> `200 OK`, `curl http://localhost:8084/customers` -> `401 Unauthorized`. Customer-service v Docker Compose funguje.
- [x] **Overene 2026-05-21**: Actuator/swagger URL su zdokumentovane v `README.md` (kazdy service ma svoj kontext path uvedeny v sekcii "OpenAPI / Swagger") a vsetky vracaju `200`:
  - `harvest-service` na porte `8081` bez `/api` context path,
  - `production-service` na porte `8082` s `/api`,
  - `inventory-service` na porte `8083` s `/inventory-service/api`,
  - `customer-service` na porte `8084` bez `/api` context path.
- [x] **Overene 2026-05-21**: Verejne endpointy su konzistentne naprieč vsetkymi 4 services (`SecurityConfig.permitAll` matchery):
  - `/actuator/**`,
  - `/swagger-ui/**`, `/swagger-ui.html`, `/v3/api-docs/**`,
  - chranene business endpointy bez tokenu vracaju `401`.
- [x] **Overene 2026-05-21**: Prometheus scrape po cistom `docker compose up` funguje pre vsetky 4 services + self-scrape pre Prometheus:
  - `customer-service` (`http://customer-service:8084/actuator/prometheus`) - **UP** (last scrape 722ms ago, duration 6.7ms),
  - `harvest-service` (`http://harvest-service:8081/actuator/prometheus`) - **UP** (1.7s ago, 6.6ms),
  - `inventory-service` (`http://inventory-service:8083/inventory-service/api/actuator/prometheus`) - **UP** (3.4s ago, 7.4ms),
  - `production-service` (`http://production-service:8082/api/actuator/prometheus`) - **UP** (3.3s ago, 5.3ms),
  - `prometheus` self-scrape - **UP** (3.7s ago, 10.7ms).
- [x] **Overene 2026-05-21**: Grafana dashbordy:
  - dva preloaded dashboardy v zlozke "Winery Management": **Winery Management Overview** a **Winery Service Details** (oba s tagmi `m3`, `pa165`, `winery`),
  - Overview ukazuje `Services Up: 4`, Service Availability stala na 1 (100%), HTTP Request Rate (vidno aj live spike pri rucnych curl testoch), Average HTTP Latency (~10ms), JVM Memory Used, Process CPU Usage,
  - Service Details ukazuje HTTP Requests per Second rozdelene podla service + status code (vratane zelenych `customer-service 200` spikes z rucneho testovania), Average HTTP Latency, Process CPU Usage, JVM Memory Used (heap aj nonheap), Live JVM Threads (22-32 per service).
- [ ] Znovu spustit showcase scenar od nuly a mat pripraveny vystup:
  - health check harvest/production/inventory/customer,
  - nacitanie harvest seed dat,
  - dokoncenie production batchu,
  - JMS sprava z production do inventory,
  - inventory stock zvyseny o ocakavany pocet flias.
- [ ] Overit GitLab pipeline na branche/commite, ktory pojde na obhajobu, a pripravit screenshot alebo URL zelenej pipeline.
- [ ] Overit README deployment/OpenShift URL; ak nefunguju, oznacit deployment ako optional/demo-only alebo pripravit funkcne URL na prezentaciu.

### Code cleanup odporucany reviewerom

- [ ] **KRITICKY**: Vytiahnut opakovane scope string literaly do konstant a pouzivat ich napriec configom, OpenAPI anotaciami a testami: *(audit 2026-05-21: PODSTATNE UPRESNENIE - `common-service/openapi/SwaggerConstants` UZ EXISTUJE so vsetkym potrebnym! Treba ho len pouzit v 3 zostavajucich services.)*
  - `customer-service`: 34 hardcoded `"test_read"`/`"test_write"` literalov,
  - `harvest-service`: 24 hardcoded scope literalov (controllery, SecurityConfig ma vlastne lokalne konstanty `READ_SCOPE`/`WRITE_SCOPE`),
  - `production-service`: 2 hardcoded scope literalov (SecurityConfig ma vlastne lokalne konstanty),
  - `inventory-service`: 10 hardcoded scope literalov (zvysok uz pouziva `SwaggerConstants.READ_SCOPE`/`.WRITE_SCOPE`).
- [ ] **KRITICKY**: Refaktor scope literals v 4 services -> nahradit za `SwaggerConstants.READ_SCOPE`/`.WRITE_SCOPE` (z `common-service.openapi.SwaggerConstants`):
  - V Spring SecurityConfig matcheroch (`hasScope("test_read")` -> `hasScope(SwaggerConstants.READ_SCOPE)`),
  - V `@SecurityRequirement(scopes = {"test_read"})` v controlleroch,
  - V OpenApiCustomizer `addString("test_read", "...")` cez `SwaggerConstants.READ_SCOPE`,
  - V `SecurityConfigTest` `token.claim("scope", "test_read")` cez `SwaggerConstants.READ_SCOPE`,
  - Odstranit lokalne `SecurityConfig.READ_SCOPE`/`WRITE_SCOPE` konstanty v harvest a production-service (su redundantne so shared SwaggerConstants).
- [ ] **Bonus**: Zaroven nahradit hardcoded HTTP status code stringy v `@ApiResponse(responseCode="200"/"201"/"400"/...)` za `SwaggerConstants.ResponseCodes.*` (`OK`, `CREATED`, `BAD_REQUEST`, ...). Aktualne pocty: `customer-service` 82x, `harvest-service` 115x, `production-service` 67x. Inventory-service to uz pouziva - sluzi ako vzor.
- [ ] Zvazit vytiahnutie spolocnej security/openapi konfiguracie do shared modulu len tam, kde to znizi duplicitu bez zbytocneho skomplikovania.
- [ ] Zvazit vytiahnutie `JmsConfig` do `common-service`, ak produkcia a inventory musia pouzivat identicky messaging setup. *(audit 2026-05-20: `production-service/config/JmsConfig.java` a `inventory-service/config/JmsConfig.java` su identicke MessageConverter beany - velmi vhodny refaktor)*
- [ ] Zvazit vytiahnutie `jwtDecoder` bean do `common-service`, ak je medzi services rovnaky. *(audit 2026-05-20: `jwtDecoder` metoda existuje vo vsetkych 4 SecurityConfig triedach a kod je identicky - velmi vhodny refaktor)*
- [ ] Prejst DTO duplicity a rozhodnut, kde dava zmysel kompozicia alebo dedicnost namiesto opakovanych poli.
- [ ] Prejst Harvest mapping a zvazit zjednodusenie:
  - camelCase vs snake_case mapping cez Hibernate naming strategy,
  - MapStruct mapper composition namiesto rucneho skladania.

### Body, ktore reviewer explicitne pochvalil

- [x] REST API je prepojene so service a persistence vrstvou vo vsetkych microservices.
- [x] Seed data su cez Liquibase a posobia realisticky.
- [x] OAuth2 Resource Server je nastaveny vo vsetkych services.
- [x] Pouzivaju sa minimalne dva scopes.
- [x] Centralny Swagger UI kontajner na porte `8090` je dobre riesenie pre OAuth redirect.
- [x] Prometheus + Grafana su pripravene a dashboard je provisioned.
- [x] Showcase scenar je domenovo dobry: dokoncenie production batchu a automaticky update inventory cez JMS.
- [x] CI pipeline existuje.
- [x] Shared exception handling a common package posobia dobre.
- [x] `ProductionPlanningService` ma netrivialnu business logiku.

### Otazky na obhajobu

- [ ] Vediet vysvetlit, preco su `HarvestRecordService#summaryByYear` a `HarvestRecordService#summaryByVineyard` implementovane aktualnym sposobom.
- [ ] Pripravit odpoved, ako by sa dali harvest summary metody zlepsit, napr. podobnym filter pristupom ako `ReservationRepository#filter`.
- [ ] Vediet vysvetlit, preco `HarvestRecord` referencuje iba jeden `GrapeType`.
- [ ] Pripravit domain/DB dosledky single-grape modelu:
  - jednoduchsi model a query,
  - nevie prirodzene reprezentovat blend alebo zber s viacerymi odrodami,
  - alternativa je join entity / many-to-many s mnozstvom alebo percentom.
- [ ] Vediet vysvetlit dvojitu validaciu na REST a service vrstve:
  - REST `@Valid` chrani HTTP boundary a dava pekne 400 response,
  - service validacia chrani business API pri volani mimo REST vrstvy,
  - ak je duplicita prilis velka, treba ju zjednotit alebo ponechat iba invarianty v service vrstve.

### Rychly defense runbook

- [ ] `docker compose config --quiet`
- [ ] `docker compose up -d --force-recreate artemis production-service harvest-service inventory-service customer-service prometheus grafana swagger-ui`
- [ ] Skontrolovat health endpoints pod realnymi context path:
  - `http://localhost:8081/actuator/health`
  - `http://localhost:8082/api/actuator/health`
  - `http://localhost:8083/inventory-service/api/actuator/health`
  - `http://localhost:8084/actuator/health`
- [ ] Skontrolovat centralny Swagger UI: `http://localhost:8090/swagger-ui.html`
- [ ] Skontrolovat Prometheus targets: `http://localhost:9090/targets`
- [ ] Skontrolovat Grafana dashboard: `http://localhost:3000`
- [ ] Spustit showcase skript a ulozit si vystup pre pripad, ze demo internet/OAuth zlyha.
