# Reflection vs. KSP – Android Benchmark Results

> **Status:** 5 benchmark runs, comparison without R8 optimization  
> **Measurement date:** 2026-10-21  
> **Device:** Google Pixel 9a, Android 17 (API 37)

## Summary

For the model creation and mapping tasks evaluated here, the KSP-generated implementation outperformed the reflection-based implementation in all three comparisons. Across five benchmark runs, the median full-pipeline execution time was **20,223.04 ns** for Reflection and **265.99 ns** for KSP, representing a **76.03× speedup**.

This result applies **only to the specific benchmark and configuration tested**; it is not a general claim about all reflection-based or KSP-generated code.

## Test Environment and Configuration

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

## Methodology

- Five successive benchmark runs were executed and saved separately on the same physical device.
- Each run included seven benchmarks: one baseline and three Reflection/KSP comparison pairs.
- The **value for an individual run** is the JSON field `metrics.timeNs.median`.
- The **aggregate execution time** is the median of those five run-level medians (median of medians), **not** the pooled median of all internal repetitions.
- The **speedup** is calculated by dividing the aggregate Reflection execution time by the aggregate KSP execution time.
- Allocation figures come from the JSON field `metrics.allocationCount.median`; they represent **allocations per operation, not bytes**.
- This benchmark does not compare the compile-time processing overhead of KSP.

## Aggregate Results

| Task | Reflection (ns) | KSP (ns) | KSP speedup |
|---|---:|---:|---:|
| Model creation | 6,839.23 | 94.06 | **72.72×** |
| Mapping | 11,659.02 | 161.76 | **72.08×** |
| Full pipeline | 20,223.04 | 265.99 | **76.03×** |

The aggregate median for the baseline benchmark was **1.382 ns**. Baseline overhead was not subtracted from the results above.

## Execution Times by Run

All values below are in nanoseconds (ns) and represent benchmark medians for the corresponding run.

| Run | Reflection model | KSP model | Reflection mapping | KSP mapping | Reflection pipeline | KSP pipeline |
|---|---:|---:|---:|---:|---:|---:|
| 1 | 6,741.09 | 94.06 | 11,658.01 | 169.45 | 20,223.04 | 266.45 |
| 2 | 6,932.95 | 94.53 | 11,589.78 | 158.16 | 19,628.51 | 265.99 |
| 3 | 6,794.73 | 92.19 | 11,659.02 | 161.50 | 20,090.37 | 247.45 |
| 4 | 6,862.33 | 94.04 | 11,829.39 | 161.76 | 20,399.41 | 267.19 |
| 5 | 6,839.23 | 95.40 | 11,686.31 | 183.84 | 20,326.39 | 264.35 |

## Allocations

| Task | Reflection (allocations/operation) | KSP (allocations/operation) |
|---|---:|---:|
| Model creation | 125 | 1 |
| Mapping | 97 | 10 |
| Full pipeline | 222 | 11 |

For the full pipeline, KSP performed **95.05% fewer allocations** (222 → 11 allocations per operation).

## Stability and Interpretation

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

1. **No R8 optimization.** These results cannot automatically be extrapolated to an R8-optimized release build.
2. **CPU frequency was not locked.** Frequency scaling and temperature may affect execution times.
3. **Compilation mode was `verify`.** Performance may differ under other ART compilation modes.
4. **Microbenchmark scope.** The results apply to the tested implementations, inputs, and test code; they do not necessarily reflect end-user performance in a complete application.
5. **Build time and APK size were not measured.** KSP's benefits and costs in these areas would require separate measurements.
6. **Semantic equivalence is not established by JSON exports alone.** Source-code review and correctness tests are needed to confirm that both implementations perform equivalent work.

## Raw Data

The original AndroidX Benchmark exports accompanying this document are:

- `run-01.json`
- `run-02.json`
- `run-03.json`
- `run-04.json`
- `run-05.json`
