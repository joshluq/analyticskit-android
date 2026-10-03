---
name: analyticskit-quality-check
description: >-
  Runbook to verify code quality, formatting, unit tests, 100% Kover coverage,
  and showcase build in Analyticskit Android. Use this skill before submitting PRs,
  closing tasks, or verifying repository health.
---

# Analyticskit Quality Check Runbook

This skill defines the mandatory pre-flight and quality verification pipeline for the **Analyticskit Android** project, enforcing the standards established in `AGENTS.md`.

## Prerequisites & Environment

The project requires **JDK 21 or higher** (Gradle 9.8+ and build-logic plugins require Java 21+ runtime).

If running on Windows in PowerShell and `JAVA_HOME` points to Java 17, temporarily set:
```powershell
$env:JAVA_HOME = "C:\Users\josh_\AppData\Local\Programs\Android Studio\jbr"
```

---

## Verification Pipeline Steps

Execute the checks in this sequence:

### 1. Code Formatting (Spotless)
Check if all Kotlin files and Gradle scripts follow official style conventions:

```bash
# Check formatting
./gradlew spotlessCheck
```

If formatting violations are reported:
```bash
# Automatically format code
./gradlew spotlessApply
```

### 2. Static Analysis (Detekt)
Run Detekt to catch code smells, complexity issues, and style rule breaks:

```bash
./gradlew detekt
```
- Configuration file: `config/detekt/detekt.yml`
- Ensure no new suppressions are added without proper justification.

### 3. Unit Tests & Logic Coverage (Kover)
Per `AGENTS.md`, Analyticskit requires **100% logic coverage** across all layers.

```bash
# Run unit tests on library
./gradlew :analyticskit:testDebugUnitTest

# Verify coverage against thresholds
./gradlew :analyticskit:koverVerifyDebug

# Output coverage log summary in console
./gradlew :analyticskit:koverLogDebug
```

To inspect uncovered lines if verification fails:
- HTML Report: `library/build/reports/kover/htmlDebug/index.html`
- Binary Reports: `library/build/kover/bin-reports/`

### 4. Verify Showcase App Build
Ensure that library changes do not break integration with consumer applications:

```bash
./gradlew :showcase:assembleDebug
```

---

## One-Line Pre-Flight Command

To run the complete suite in one command:

```powershell
$env:JAVA_HOME = "C:\Users\josh_\AppData\Local\Programs\Android Studio\jbr"
.\gradlew.bat spotlessCheck detekt :analyticskit:testDebugUnitTest :analyticskit:koverVerifyDebug :showcase:assembleDebug
```

---

## Troubleshooting Guide

| Issue | Cause | Resolution |
| :--- | :--- | :--- |
| `Dependency requires at least JVM runtime version 21` | JVM version < 21 | Ensure `JAVA_HOME` points to JDK 21+ or Android Studio JBR. |
| `Spotless failed: ...` | Unformatted Kotlin or Gradle files | Run `./gradlew spotlessApply` and re-run check. |
| `Kover verification failed: coverage is below 100%` | Missing test cases for new branches or methods | Add unit tests in `library/src/test/java` covering all branches, edge cases, and exception paths. |
| `:showcase:assembleDebug failed` | Breaking change in public API | Update showcase references or ensure backward compatibility in `AnalyticskitManager`. |
