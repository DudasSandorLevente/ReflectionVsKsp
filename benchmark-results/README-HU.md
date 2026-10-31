# Reflection vs. KSP – Android benchmarkeredmények

> **Állapot:** R8 OFF (5 futás) és R8 ON (5 összehasonlító + 1 további futás)  
>**R8 OFF mérés dátuma:** 2026-10-21  
>**Eszköz:** Google Pixel 9a, Android 17 (API 37)

## Összefoglaló

A vizsgált modell-létrehozási és mapping feladatoknál a KSP-vel generált implementáció mindhárom összehasonlításban gyorsabbnak bizonyult a reflection-alapú implementációnál, R8 OFF és R8 ON konfigurációban is. A teljes pipeline öt sorozatból számolt medián futási ideje R8 OFF esetén **20 223,04 ns** (Reflection), illetve **265,99 ns** (KSP), azaz **76,03×** különbség; R8 ON esetén **19 461,63 ns**, illetve **235,12 ns**, azaz **82,77×** különbség. A két konfiguráció eltérő Android builden készült, így az R8 önálló hatása ebből nem határozható meg.

Ez **konkrét benchmarkra és konfigurációra vonatkozó** eredmény, nem általános állítás minden reflection- vagy KSP-alapú kódról.

## Mérési környezet és konfiguráció (R8 OFF)

| Tulajdonság | Érték |
|---|---|
| Készülék | Google Pixel 9a (`tegu`) |
| Operációs rendszer | Android 17 / API 37 |
| Android build azonosító | `CP3A.260905.009` |
| CPU-magok száma | 8 |
| CPU frekvencia rögzítése | Nem (`cpuLocked=false`) |
| Sustained performance mode | Kikapcsolva |
| AndroidX Benchmark compilation mode | `verify` |
| R8 optimalizáció | Nem engedélyezett a vizsgált benchmark buildben |
| Mérési sorozatok száma | 5 |
| Benchmarkok száma sorozatonként | 7 |

A mérések az AndroidX Benchmark által előállított JSON-exportokból származnak. A `thermalThrottleSleepSeconds` értéke a vizsgált benchmarkbejegyzésekben 0; ez **nem bizonyítja**, hogy a készülék hőmérséklete vagy órajele a futások alatt végig állandó volt.

## Módszertan (R8 OFF)

- Öt egymást követő, külön mentett benchmarkfuttatás készült ugyanazon a fizikai készüléken.
- Minden sorozatban hét benchmark szerepel: egy baseline, három reflection/KSP összehasonlítási pár.
- Egy benchmark **sorozatértéke** a JSON `metrics.timeNs.median` mezője.
- Az **összesített futási idő** az öt sorozatérték mediánja (mediánok mediánja), nem az összes belső ismétlés közös mediánja.
- A **gyorsulási arány** a reflection összesített ideje osztva a KSP összesített idejével.
- Az allokációs értékek a JSON `metrics.allocationCount.median` mezőiből származnak; ezek műveletenkénti allokációszámok, **nem bájtok**.
- A benchmark nem tartalmazza a KSP fordításkori feldolgozási idejének összehasonlítását.

## Összesített eredmények (R8 OFF)

| Feladat | Reflection (ns) | KSP (ns) | KSP gyorsulás |
|---|---:|---:|---:|
| Modell létrehozása | 6 839,23 | 94,06 | **72,72×** |
| Mapping | 11 659,02 | 161,76 | **72,08×** |
| Teljes pipeline | 20 223,04 | 265,99 | **76,03×** |

A baseline benchmark összesített mediánja: **1,382 ns**. A baseline nem lett levonva a fenti értékekből.

## Futási idők sorozatonként (R8 OFF)

Az alábbi értékek nanomásodpercben (ns), az adott sorozat benchmark-mediánjai.

| Sorozat | Reflection modell | KSP modell | Reflection mapping | KSP mapping | Reflection pipeline | KSP pipeline |
|---|---:|---:|---:|---:|---:|---:|
| 1 | 6 741,09 | 94,06 | 11 658,01 | 169,45 | 20 223,04 | 266,45 |
| 2 | 6 932,95 | 94,53 | 11 589,78 | 158,16 | 19 628,51 | 265,99 |
| 3 | 6 794,73 | 92,19 | 11 659,02 | 161,50 | 20 090,37 | 247,45 |
| 4 | 6 862,33 | 94,04 | 11 829,39 | 161,76 | 20 399,41 | 267,19 |
| 5 | 6 839,23 | 95,40 | 11 686,31 | 183,84 | 20 326,39 | 264,35 |

## Allokációk (R8 OFF)

| Feladat | Reflection (allokáció/művelet) | KSP (allokáció/művelet) |
|---|---:|---:|
| Modell létrehozása | 125 | 1 |
| Mapping | 97 | 10 |
| Teljes pipeline | 222 | 11 |

A teljes pipeline allokációszáma **95,05%-kal alacsonyabb** a KSP esetében (222 → 11 allokáció/művelet).

## Stabilitás és értelmezés (R8 OFF)

| Benchmark | 5 sorozat mediánjának minimuma (ns) | Maximuma (ns) |
|---|---:|---:|
| Modell létrehozása – Reflection | 6 741,09 | 6 932,95 |
| Modell létrehozása – KSP | 92,19 | 95,40 |
| Mapping – Reflection | 11 589,78 | 11 829,39 |
| Mapping – KSP | 158,16 | 183,84 |
| Teljes pipeline – Reflection | 19 628,51 | 20 399,41 |
| Teljes pipeline – KSP | 247,45 | 267,19 |

A KSP mapping benchmark belső ismétlései között egyes sorozatokban jelentős ingadozás figyelhető meg. A JSON-ok `coefficientOfVariation` értékei a belső ismétlésekre vonatkoznak, ezért **nem azonosak** az öt független sorozat közötti szóródással. A fő megállapítás a sorozatmediánok alapján mind az öt futásban megmarad.

## Korlátok és reprodukálhatóság

1. **Az R8 OFF és R8 ON külön konfiguráció.** A közvetlen összevetést az eltérő Android build is befolyásolhatja; az R8 önálló hatása nem izolált.
2. **A CPU-órajel nincs rögzítve.** A frekvenciaváltás és a hőmérséklet befolyásolhatja az időket.
3. **A fordítási mód `verify`.** A teljesítmény más ART compilation mode mellett eltérhet.
4. **Mikrobenchmark.** Az eredmények a vizsgált implementációkra, bemenetekre és tesztkódra vonatkoznak; nem feltétlenül tükrözik egy teljes alkalmazás végfelhasználói teljesítményét.
5. **Nem tartalmaz fordítási időt és APK-méretet.** A KSP előnyeit és költségeit ezek mentén külön kellene mérni.
6. **Az implementációk szemantikai egyenértékűségét a JSON-export önmagában nem igazolja.** Ehhez a forráskód és a helyességi tesztek vizsgálata szükséges.

## Nyers adatok (R8 OFF)

A dokumentációhoz tartozó, eredeti AndroidX Benchmark exportok:

- `run-01.json`
- `run-02.json`
- `run-03.json`
- `run-04.json`
- `run-05.json`

## R8 ON – mérési környezet és módszertan

Az R8 ON benchmark ugyanazon a Pixel 9a készüléken, Android 17 / API 37 alatt futott, de **más rendszerbuilden** (`CP3A.261005.005`), mint az R8 OFF mérés (`CP3A.260905.009`). Mind a hat R8 ON JSON-ban azonos a rendszerbuild. A CPU nem volt rögzített órajelen (`cpuLocked=false`), a sustained performance mode kikapcsolt, a Benchmark compilation mode `verify`.

Az első öt R8 ON futásból a `metrics.timeNs.median` értékek mediánját számítjuk (mediánok mediánja), az R8 OFF dokumentációval azonos módon. A **hatodik futás további ellenőrző mérés**, külön szerepel a táblázatban, és nem része az ötfutásos összesítésnek. Ez nem statisztikai kizárás vagy hibás mérésként történő elvetés. A gyorsulás a Reflection/KSP összesített idő hányadosa; a baseline értékét nem vonjuk le.

## R8 OFF és R8 ON – összehasonlítás

| Feladat | R8 OFF Reflection (ns) | R8 OFF KSP (ns) | R8 OFF KSP-előny | R8 ON Reflection (ns) | R8 ON KSP (ns) | R8 ON KSP-előny |
|---|---:|---:|---:|---:|---:|---:|
| Modell létrehozása | 6 839,23 | 94,06 | 72,72× | 6 710,33 | 95,09 | 70,57× |
| Mapping | 11 659,02 | 161,76 | 72,08× | 11 468,30 | 171,17 | 67,00× |
| Teljes pipeline | 20 223,04 | 265,99 | 76,03× | 19 461,63 | 235,12 | 82,77× |

**Értelmezés:** mindkét konfiguráción belül a KSP jelentős előnye látható. Az R8 ON/OFF közötti változás nem értelmezhető tisztán R8-hatásként, mert a rendszerbuild eltér, és a CPU-órajel sem volt rögzítve. A különböző futások között is van ingadozás.

## R8 ON – futási idők sorozatonként

Az értékek nanomásodpercben (ns), a JSON-ok `metrics.timeNs.median` mezőjéből, két tizedesre kerekítve.

| Sorozat | Reflection modell | KSP modell | Reflection mapping | KSP mapping | Reflection pipeline | KSP pipeline | Baseline |
|---|---:|---:|---:|---:|---:|---:|---:|
| 1 | 6 718,88 | 95,09 | 11 512,38 | 327,60 | 18 759,95 | 233,39 | 1,33 |
| 2 | 6 860,79 | 95,86 | 11 042,76 | 147,42 | 19 323,91 | 231,52 | 1,29 |
| 3 | 6 606,76 | 95,77 | 11 476,22 | 171,17 | 19 834,81 | 235,12 | 1,39 |
| 4 | 6 710,33 | 94,68 | 11 116,55 | 145,63 | 19 461,63 | 249,75 | 1,35 |
| 5 | 6 676,93 | 94,30 | 11 468,30 | 175,34 | 19 822,94 | 258,50 | 1,49 |
| 6 (további) | 6 587,67 | 92,62 | 9 699,31 | 148,43 | 16 806,89 | 263,92 | 1,38 |

Az első R8 ON futás KSP mapping mediánja **327,60 ns**, míg a következő öt futásban **145,63–175,34 ns** közé esik. A kiugró első értéket megtartottuk; az első öt futásból számított medián **171,17 ns**. A hatodik futásban a Reflection mapping és a teljes pipeline alacsonyabb értéket mutatott, ami a futások közötti változékonyságra figyelmeztet.

## R8 ON – allokációk

| Feladat | Reflection (allokáció/művelet) | KSP (allokáció/művelet) |
|---|---:|---:|
| Modell létrehozása | 125 | 1 |
| Mapping | 97 | 10 |
| Teljes pipeline | 222 | 11 |

Mind a hat R8 ON futásban ezek az értékek jelentek meg két tizedesre kerekítve, megegyezve az R8 OFF dokumentált értékeivel. A teljes pipeline-ban a KSP **95,05%-kal kevesebb allokációt** végzett (222 → 11). Ezek allokációszámok, nem allokált bájtok; a kerekített egyezés nem jelenti az összes futás nyers értékének teljes azonosságát.

## R8-optimalizáció: mapping és keep szabályok

Az `app/build/outputs/mapping/benchmarkR8/mapping.txt` vizsgálatakor a `KspSettingsMapper` osztály átnevezve (`tg0`) szerepelt, és a `map()` metódus mappingje a `GeneratedSettingsModelRegistry.map()` és a generált KSP mapper metódusainak beépítésére utalt. Több generált mapper/factory osztály `R8$$REMOVED$$CLASS` jelölést kapott: ez azt mutatja, hogy nem maradtak önálló osztályként, **nem** azt, hogy a hozzájuk tartozó funkcionalitás eltűnt.

A `ReflectionSettingsMapper` neve megmaradt. A mappingje a Kotlin collection műveletek (`sortedBy`, `mapNotNull`, `forEach`, `firstOrNull`) és a `findAnnotation` beépítésére utalt, de ez **nem** szünteti meg a szükséges futásidejű reflexiót. A mapping az R8 átalakításairól ad támpontot, önmagában nem bizonyít konkrét gyorsulási okot.

A széles `-keep class kotlin.** { *; }` szabály eltávolításával végzett külön kísérletben a benchmark futtatása hiányzó Kotlin runtime-osztályok és metódusok miatt hibázott (`TuplesKt`, `MapsKt`, `CollectionsKt`, `IntrinsicsKt` stb.). A külön fordított benchmark APK olyan osztályokra is hivatkozhat, amelyeket az alkalmazás R8-feldolgozása eltávolít vagy átnevez. A kísérleti szűkítést **visszavontuk**, az eredeti keep konfigurációval mind a hét benchmark sikeresen futott.

A széles Kotlin és AndroidX Benchmark keep szabályok **korlátozzák az optimalizálás szabadságát**; ezért az R8 ON eredmény nem tekinthető a maximálisan szűkített keep szabályokkal elérhető teljesítmény mérésének. Ugyanakkor a saját KSP- és Reflection-kódon megfigyelhető R8-átalakítások történtek.

## További korlátok

- Az R8 OFF és ON mérések eltérő Android rendszerbuilden készültek, így nem kontrollált, kizárólag R8-at változtató A/B kísérletet alkotnak.
- A hatodik R8 ON futás nem része az első öt futásra épülő összesített értékeknek; eredménye külön közölt.
- A keep szabályok szűkítésével végzett sikertelen kísérlet **nem** része a közölt teljesítményméréseknek.
- A mérés nem tartalmaz fordítási időt, APK-méretet, energiafelhasználást vagy minden lehetséges optimalizációs konfigurációt.

## Nyers adatok (R8 ON)

- `r8-run-01.json`
- `r8-run-02.json`
- `r8-run-03.json`
- `r8-run-04.json`
- `r8-run-05.json`
- `r8-run-06.json` (további ellenőrző futás)
