---
name: analyticskit-release-workflow
description: >-
  Step-by-step procedure to prepare, bump version, verify locally, and release
  Analyticskit Android (Snapshots and Production Releases to GitHub Packages).
  Use when cutting a new version, preparing release PRs, or publishing to MavenLocal.
---

# Analyticskit Release & Publishing Workflow

This skill outlines the process for releasing snapshots and production versions of `analyticskit-android` to GitHub Packages and Maven Local.

---

## Release Strategy Overview

| Target | Branch | CI Workflow | Version Type Flag | Destination |
| :--- | :--- | :--- | :--- | :--- |
| **Snapshot** | `develop` | `publish-snapshot.yml` | `-PversionType="-SNAPSHOT"` | GitHub Packages (`joshluq/analyticskit-android`) |
| **Production Release** | `main` | `publish-release.yml` | `-PversionType=""` | GitHub Packages + GitHub Release Tag `vX.Y.Z` |

---

## Step 1: Version Configuration Checklist

1. **`gradle.properties`**:
   - Update `libraryVersion` to the target version (e.g. `1.3.1` or `1.4.0`):
     ```properties
     libraryVersion=1.4.0
     ```
   - Verify that `catalogVersion` points to the stable release if cutting a production release (avoid `-SNAPSHOT` in production releases when possible).
2. **`README.md`**:
   - Update the installation dependency block:
     ```kotlin
     implementation("es.joshluq.kit:analyticskit:1.4.0")
     ```

---

## Step 2: Local Pre-Flight Verification

Before creating a release pull request or pushing to `develop`/`main`:

```powershell
$env:JAVA_HOME = "C:\Users\josh_\AppData\Local\Programs\Android Studio\jbr"

# 1. Format and static checks
.\gradlew.bat spotlessCheck detekt

# 2. Run all unit tests
.\gradlew.bat test

# 3. Verify showcase app compiles
.\gradlew.bat :showcase:assembleDebug

# 4. Verify local Maven publication
.\gradlew.bat publishReleasePublicationToMavenLocal
```

---

## Step 3: Git Workflow & Tagging

### For Snapshots (Iterative Development)
1. Merge feature branch into `develop`.
2. The GitHub Action `Publish Kit Snapshot` runs automatically, appending `-SNAPSHOT` to the version and publishing artifacts.

### For Production Releases
1. Create a release branch (e.g., `release/v1.4.0`) from `develop`.
2. Ensure all quality checks pass.
3. Open a Pull Request targeting `main`.
4. Once merged into `main`, `publish-release.yml`:
   - Publishes `es.joshluq.kit:analyticskit:X.Y.Z` to GitHub Packages.
   - Automatically generates the Git Tag `vX.Y.Z`.
   - Generates the GitHub Release with automatic release notes.
5. Back-merge `main` into `develop` to keep branches synchronized.

---

## Troubleshooting Publication

- **`401 Unauthorized` on publish**: Ensure `GITHUB_ACTOR` and `GITHUB_TOKEN` have `packages:write` permissions.
- **`Dependency requires at least JVM runtime version 21`**: Verify GitHub Actions workflow uses `java-version: '21'` (Temurin).
- **Paths ignored**: Note that changes only in `.agents/**`, `**.md`, or `docs/**` will not trigger publication builds (configured in workflow `paths-ignore`).
