# Recorded toolchain

Recorded 2026-09-15 while verifying the foundation (step 1 of the talk build).

## What upstream says

From the upstream `README.md` at `818c413`:

- "Java 17 or later is required for the build, and the application can run with Java 17 or newer."
- Prerequisites: "Java 17 or newer (full JDK, not a JRE)".
- Build with Maven (`./mvnw`) or Gradle (`./gradlew`). `pom.xml` sets `<java.version>17</java.version>`.
- Upstream CI (`.github/workflows/maven-build.yml`) builds with `./mvnw -B verify`.

## Working toolchain (verified)

| Component | Version |
|-----------|---------|
| JDK       | OpenJDK 25.0.2 (2026-01-20), vendor Homebrew, `/opt/homebrew/Cellar/openjdk/25.0.2` |
| Maven     | 3.9.16 via the Maven wrapper (`.mvn/wrapper/maven-wrapper.properties`); system Maven is also 3.9.16 |
| OS        | macOS 26.6.2 (build 25G83), arm64 (Apple Silicon) |
| Git       | 2.54.0 |
| Docker    | CLI installed at `/usr/local/bin/docker`; daemon **not running** during verification |

## Build result

Command: `./mvnw -B verify` on `upstream-pristine` (`818c4136ea971c21674525f9053de0d9c7ad8cfe`).

| Run | Wall clock | Result |
|-----|-----------|--------|
| Cold `~/.m2` (748 artefact downloads) | 72 s | BUILD SUCCESS: 74 tests, 0 failures, 0 errors, 2 skipped |
| Warm `~/.m2` (0 downloads)            | 9 s  | BUILD SUCCESS: 74 tests, 0 failures, 0 errors, 2 skipped |

On stage, plan for the warm figure, and prime the cache before the talk.

### Tests that did not really run

Without a Docker daemon these tests skip themselves:

- `MySqlIntegrationTests`: 2 tests skipped (`@Testcontainers(disabledWithoutDocker = true)`).
- `PostgresIntegrationTests`: 0 tests run (an `assumeTrue(isDockerAvailable())` in `@BeforeAll`).

To exercise them, start Docker Desktop and re-run `./mvnw -B verify`.

### Expected noise in the log

- `DockerClientProviderStrategy : Could not find a valid Docker environment`: Docker is absent.
- `RuntimeException: Expected: controller used to showcase what happens when an exception is thrown`:
  `CrashControllerIntegrationTests` provokes this on purpose.

Neither is a failure. It's worth knowing in advance so neither one surprises you on stage.
