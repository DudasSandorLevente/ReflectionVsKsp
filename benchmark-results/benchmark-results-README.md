# Reflection vs. KSP – Android benchmarkeredmények

> **Állapot:** 5 mérési sorozat, R8 nélküli összehasonlítás  
>**Mérés dátuma:** 2026-10-21  
>**Eszköz:** Google Pixel 9a, Android 17 (API 37)

## Összefoglaló

A vizsgált modell-létrehozási és mapping feladatoknál a KSP-vel generált implementáció mindhárom összehasonlításban gyorsabbnak bizonyult a reflection-alapú implementációnál. A teljes pipeline öt sorozatból számolt medián futási ideje **20 223,04 ns** (Reflection), illetve **265,99 ns** (KSP), ami **76,03×** arányt jelent.

Ez **konkrét benchmarkra és konfigurációra vonatkozó** eredmény, nem általános állítás minden reflection- vagy KSP-alapú kódról.

## Mérési környezet és konfiguráció

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

## Módszertan

- Öt egymást követő, külön mentett benchmarkfuttatás készült ugyanazon a fizikai készüléken.
- Minden sorozatban hét benchmark szerepel: egy baseline, három reflection/KSP összehasonlítási pár.
- Egy benchmark **sorozatértéke** a JSON `metrics.timeNs.median` mezője.
- Az **összesített futási idő** az öt sorozatérték mediánja (mediánok mediánja), nem az összes belső ismétlés közös mediánja.
- A **gyorsulási arány** a reflection összesített ideje osztva a KSP összesített idejével.
- Az allokációs értékek a JSON `metrics.allocationCount.median` mezőiből származnak; ezek műveletenkénti allokációszámok, **nem bájtok**.
- A benchmark nem tartalmazza a KSP fordításkori feldolgozási idejének összehasonlítását.

## Összesített eredmények

| Feladat | Reflection (ns) | KSP (ns) | KSP gyorsulás |
|---|---:|---:|---:|
| Modell létrehozása | 6 839,23 | 94,06 | **72,72×** |
| Mapping | 11 659,02 | 161,76 | **72,08×** |
| Teljes pipeline | 20 223,04 | 265,99 | **76,03×** |

A baseline benchmark összesített mediánja: **1,382 ns**. A baseline nem lett levonva a fenti értékekből.

## Futási idők sorozatonként

Az alábbi értékek nanomásodpercben (ns), az adott sorozat benchmark-mediánjai.

| Sorozat | Reflection modell | KSP modell | Reflection mapping | KSP mapping | Reflection pipeline | KSP pipeline |
|---|---:|---:|---:|---:|---:|---:|
| 1 | 6 741,09 | 94,06 | 11 658,01 | 169,45 | 20 223,04 | 266,45 |
| 2 | 6 932,95 | 94,53 | 11 589,78 | 158,16 | 19 628,51 | 265,99 |
| 3 | 6 794,73 | 92,19 | 11 659,02 | 161,50 | 20 090,37 | 247,45 |
| 4 | 6 862,33 | 94,04 | 11 829,39 | 161,76 | 20 399,41 | 267,19 |
| 5 | 6 839,23 | 95,40 | 11 686,31 | 183,84 | 20 326,39 | 264,35 |

## Allokációk

| Feladat | Reflection (allokáció/művelet) | KSP (allokáció/művelet) |
|---|---:|---:|
| Modell létrehozása | 125 | 1 |
| Mapping | 97 | 10 |
| Teljes pipeline | 222 | 11 |

A teljes pipeline allokációszáma **95,05%-kal alacsonyabb** a KSP esetében (222 → 11 allokáció/művelet).

## Stabilitás és értelmezés

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

1. **Nincs R8-optimalizáció.** Az eredmény nem vetíthető automatikusan R8-cal optimalizált release buildre.
2. **A CPU-órajel nincs rögzítve.** A frekvenciaváltás és a hőmérséklet befolyásolhatja az időket.
3. **A fordítási mód `verify`.** A teljesítmény más ART compilation mode mellett eltérhet.
4. **Mikrobenchmark.** Az eredmények a vizsgált implementációkra, bemenetekre és tesztkódra vonatkoznak; nem feltétlenül tükrözik egy teljes alkalmazás végfelhasználói teljesítményét.
5. **Nem tartalmaz fordítási időt és APK-méretet.** A KSP előnyeit és költségeit ezek mentén külön kellene mérni.
6. **Az implementációk szemantikai egyenértékűségét a JSON-export önmagában nem igazolja.** Ehhez a forráskód és a helyességi tesztek vizsgálata szükséges.

## Nyers adatok

A dokumentációhoz tartozó, eredeti AndroidX Benchmark exportok:

- `run-01.json`
- `run-02.json`
- `run-03.json`
- `run-04.json`
- `run-05.json`
