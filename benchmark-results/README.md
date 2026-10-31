# Reflection vs. KSP – Android Benchmark Results

> **Status:** R8 OFF (5 runs) and R8 ON (5 comparison runs + 1 additional run)  
> **R8 OFF measurement date:** 2026-10-21  
> **Device:** Google Pixel 9a, Android 17 (API 37)

## Summary

For the model creation and mapping tasks evaluated here, the KSP-generated implementation outperformed the reflection-based implementation in all three comparisons, both with and without R8. Across five runs, the median full-pipeline execution time was **20,223.04 ns** for Reflection and **265.99 ns** for KSP (**76.03×**) with R8 OFF, versus **19,461.63 ns** and **235.12 ns** (**82.77×**) with R8 ON. The configurations used different Android OS builds, so the difference between them cannot be attributed to R8 alone.

This result applies **only to the specific benchmark and configuration tested**; it is not a general claim about all reflection-based or KSP-generated code.

## Test Environment and Configuration (R8 OFF)

| Property | Value |
|---|---|
| Device | Google Pixel 9a (`tegu`) |
| Operating system | Android 17 / API 37 |
| Android build ID | `CP3A.260905.009` |
| CPU cores | 8 |
| CPU frequency locked | No (`cpuLocked=false`) |
| Sustained performance mode | Disabled |
| AndroidX Benchmark compilation mode | `verify` |
| R8 optimization | Disabled in the benchmarked build |
| Number of benchmark runs | 5 |
| Benchmarks per run | 7 |

The measurements come from JSON exports produced by AndroidX Benchmark. The `thermalThrottleSleepSeconds` value is 0 for the benchmark entries examined; this **does not establish** that device temperature or CPU frequency remained constant throughout the runs.

## Methodology (R8 OFF)

- Five successive benchmark runs were executed and saved separately on the same physical device.
- Each run included seven benchmarks: one baseline and three Reflection/KSP comparison pairs.
- The **value for an individual run** is the JSON field `metrics.timeNs.median`.
- The **aggregate execution time** is the median of those five run-level medians (median of medians), **not** the pooled median of all internal repetitions.
- The **speedup** is calculated by dividing the aggregate Reflection execution time by the aggregate KSP execution time.
- Allocation figures come from the JSON field `metrics.allocationCount.median`; they represent **allocations per operation, not bytes**.
- This benchmark does not compare the compile-time processing overhead of KSP.

## Aggregate Results (R8 OFF)

| Task | Reflection (ns) | KSP (ns) | KSP speedup |
|---|---:|---:|---:|
| Model creation | 6,839.23 | 94.06 | **72.72×** |
| Mapping | 11,659.02 | 161.76 | **72.08×** |
| Full pipeline | 20,223.04 | 265.99 | **76.03×** |

The aggregate median for the baseline benchmark was **1.382 ns**. Baseline overhead was not subtracted from the results above.

## Execution Times by Run (R8 OFF)

All values below are in nanoseconds (ns) and represent benchmark medians for the corresponding run.

| Run | Reflection model | KSP model | Reflection mapping | KSP mapping | Reflection pipeline | KSP pipeline |
|---|---:|---:|---:|---:|---:|---:|
| 1 | 6,741.09 | 94.06 | 11,658.01 | 169.45 | 20,223.04 | 266.45 |
| 2 | 6,932.95 | 94.53 | 11,589.78 | 158.16 | 19,628.51 | 265.99 |
| 3 | 6,794.73 | 92.19 | 11,659.02 | 161.50 | 20,090.37 | 247.45 |
| 4 | 6,862.33 | 94.04 | 11,829.39 | 161.76 | 20,399.41 | 267.19 |
| 5 | 6,839.23 | 95.40 | 11,686.31 | 183.84 | 20,326.39 | 264.35 |

## Allocations (R8 OFF)

| Task | Reflection (allocations/operation) | KSP (allocations/operation) |
|---|---:|---:|
| Model creation | 125 | 1 |
| Mapping | 97 | 10 |
| Full pipeline | 222 | 11 |

For the full pipeline, KSP performed **95.05% fewer allocations** (222 → 11 allocations per operation).

## Stability and Interpretation (R8 OFF)

| Benchmark | Minimum of five run medians (ns) | Maximum of five run medians (ns) |
|---|---:|---:|
| Model creation – Reflection | 6,741.09 | 6,932.95 |
| Model creation – KSP | 92.19 | 95.40 |
| Mapping – Reflection | 11,589.78 | 11,829.39 |
| Mapping – KSP | 158.16 | 183.84 |
| Full pipeline – Reflection | 19,628.51 | 20,399.41 |
| Full pipeline – KSP | 247.45 | 267.19 |

Some runs show substantial variation among the internal repetitions of the KSP mapping benchmark. The JSON `coefficientOfVariation` values describe **variation within each benchmark run**, and should not be confused with variation **between the five separate runs**. The main finding holds across the run-level medians in all five runs.

## Limitations and Reproducibility

1. **R8 OFF and R8 ON are separate configurations.** Different Android OS builds may influence their comparison; the isolated effect of R8 was not measured.
2. **CPU frequency was not locked.** Frequency scaling and temperature may affect execution times.
3. **Compilation mode was `verify`.** Performance may differ under other ART compilation modes.
4. **Microbenchmark scope.** The results apply to the tested implementations, inputs, and test code; they do not necessarily reflect end-user performance in a complete application.
5. **Build time and APK size were not measured.** KSP's benefits and costs in these areas would require separate measurements.
6. **Semantic equivalence is not established by JSON exports alone.** Source-code review and correctness tests are needed to confirm that both implementations perform equivalent work.

## Raw Data (R8 OFF)

The original AndroidX Benchmark exports accompanying this document are:

- `run-01.json`
- `run-02.json`
- `run-03.json`
- `run-04.json`
- `run-05.json`

## R8 ON – Environment and Methodology

The R8 ON benchmarks ran on the same Pixel 9a with Android 17 / API 37, but on a **different OS build** (`CP3A.261005.005`) from R8 OFF (`CP3A.260905.009`). All six R8 ON JSON files report the same build. CPU frequency was not locked (`cpuLocked=false`), sustained performance mode was disabled, and AndroidX Benchmark compilation mode was `verify`.

For the R8 ON aggregate, we use the median of the `metrics.timeNs.median` values from the **first five runs** (median of medians), consistent with the R8 OFF methodology. **Run 6 is an additional check**, reported separately and excluded from the five-run aggregate; it is not discarded as an invalid measurement. Speedup is the aggregate Reflection time divided by the aggregate KSP time. Baseline overhead is not subtracted.

## R8 OFF vs. R8 ON – Aggregate Comparison

| Task | R8 OFF Reflection (ns) | R8 OFF KSP (ns) | R8 OFF KSP speedup | R8 ON Reflection (ns) | R8 ON KSP (ns) | R8 ON KSP speedup |
|---|---:|---:|---:|---:|---:|---:|
| Model creation | 6,839.23 | 94.06 | 72.72× | 6,710.33 | 95.09 | 70.57× |
| Mapping | 11,659.02 | 161.76 | 72.08× | 11,468.30 | 171.17 | 67.00× |
| Full pipeline | 20,223.04 | 265.99 | 76.03× | 19,461.63 | 235.12 | 82.77× |

**Interpretation:** KSP retains a substantial advantage within each configuration. Differences between R8 OFF and R8 ON cannot be interpreted as the isolated effect of R8, because the OS build changed and CPU frequency was not locked. Run-to-run variation is also present.

## R8 ON – Execution Times by Run

Values are in nanoseconds (ns), taken from the JSON `metrics.timeNs.median` field and rounded to two decimals.

| Run | Reflection model | KSP model | Reflection mapping | KSP mapping | Reflection pipeline | KSP pipeline | Baseline |
|---|---:|---:|---:|---:|---:|---:|---:|
| 1 | 6,718.88 | 95.09 | 11,512.38 | 327.60 | 18,759.95 | 233.39 | 1.33 |
| 2 | 6,860.79 | 95.86 | 11,042.76 | 147.42 | 19,323.91 | 231.52 | 1.29 |
| 3 | 6,606.76 | 95.77 | 11,476.22 | 171.17 | 19,834.81 | 235.12 | 1.39 |
| 4 | 6,710.33 | 94.68 | 11,116.55 | 145.63 | 19,461.63 | 249.75 | 1.35 |
| 5 | 6,676.93 | 94.30 | 11,468.30 | 175.34 | 19,822.94 | 258.50 | 1.49 |
| 6 (additional) | 6,587.67 | 92.62 | 9,699.31 | 148.43 | 16,806.89 | 263.92 | 1.38 |

The first R8 ON run had a KSP mapping median of **327.60 ns**; the next five runs ranged from **145.63 to 175.34 ns**. The high first-run value is retained, and the median of the first five runs is **171.17 ns**. Run 6 showed lower Reflection mapping and full-pipeline times, illustrating between-run variation.

## R8 ON – Allocations

| Task | Reflection (allocations/operation) | KSP (allocations/operation) |
|---|---:|---:|
| Model creation | 125 | 1 |
| Mapping | 97 | 10 |
| Full pipeline | 222 | 11 |

All six R8 ON runs reported these values when rounded to two decimal places, matching the documented R8 OFF figures. For the full pipeline, KSP made **95.05% fewer allocations** (222 → 11). These are allocation counts, not bytes; equality after rounding does not imply that all raw values were identical.

## R8 Optimization: Mapping and Keep-Rule Findings

Inspection of `app/build/outputs/mapping/benchmarkR8/mapping.txt` showed that `KspSettingsMapper` was renamed (`tg0`), and its `map()` mapping indicated inlining from `GeneratedSettingsModelRegistry.map()` and generated KSP mapper methods. Several generated mapper/factory classes were marked `R8$$REMOVED$$CLASS`: they no longer exist as standalone classes, **not** necessarily that their functionality was removed.

`ReflectionSettingsMapper` retained its original name. Its mapping indicated inlining of Kotlin collection operations (`sortedBy`, `mapNotNull`, `forEach`, `firstOrNull`) and `findAnnotation`, but this does **not** eliminate the runtime reflection required by the implementation. A mapping file provides evidence of transformations, not proof of a specific cause of performance improvement.

In a separate experiment, removing the broad `-keep class kotlin.** { *; }` rule caused benchmark runtime failures involving missing Kotlin runtime classes and methods (`TuplesKt`, `MapsKt`, `CollectionsKt`, `IntrinsicsKt`, etc.). The separately compiled benchmark APK can reference classes that app-side R8 processing removes or renames. The experimental narrowing was **reverted**, and all seven benchmarks passed again with the original keep configuration.

Broad Kotlin and AndroidX Benchmark keep rules **restrict optimization opportunities**. Accordingly, R8 ON should not be interpreted as the maximum performance achievable with fully minimized keep rules. Nevertheless, R8 transformations were observed in the application’s KSP and Reflection code.

## Additional Limitations

- R8 OFF and R8 ON were measured on different Android OS builds, so this is not a controlled A/B experiment varying only R8.
- The sixth R8 ON run is shown but excluded from the first-five-run aggregate.
- The failed keep-rule narrowing experiment is **not** part of the reported performance measurements.
- Build time, APK size, energy consumption, and all possible optimization configurations were not measured.

## Raw Data (R8 ON)

- `r8-run-01.json`
- `r8-run-02.json`
- `r8-run-03.json`
- `r8-run-04.json`
- `r8-run-05.json`
- `r8-run-06.json` (additional check run)
