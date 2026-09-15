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
| Docker    | Docker Desktop 4.82.0, Engine 29.6.1, Compose v5.3.0 (context `desktop-linux`) |
| Images    | `postgres:18.4` (via Spring Boot Docker Compose), `mysql:9.7` and `testcontainers/ryuk:0.14.0` (via Testcontainers) |

## Build result

Command: `./mvnw -B verify` on `upstream-pristine` (`818c4136ea971c21674525f9053de0d9c7ad8cfe`).

### Full suite, Docker running (the reference result)

| Run | Wall clock | Result |
|-----|-----------|--------|
| Cold images (pulls `postgres:18.4`, `mysql:9.7`, `ryuk`) | 134 s | BUILD SUCCESS: 76 tests, 0 failures, 0 errors, 0 skipped |
| Warm images and `~/.m2`                                  | 21 s  | BUILD SUCCESS: 76 tests, 0 failures, 0 errors, 0 skipped |

On a cold run, `PostgresIntegrationTests` (66 s) and `MySqlIntegrationTests` (59 s) are almost
all image pull time. **Before going on stage:** start Docker Desktop and run the build once,
so both the images and `~/.m2` are cached.

### Without Docker

| Run | Wall clock | Result |
|-----|-----------|--------|
| Cold `~/.m2` (748 artefact downloads) | 72 s | BUILD SUCCESS: 74 tests, 0 failures, 0 errors, 2 skipped |
| Warm `~/.m2` (0 downloads)            | 9 s  | BUILD SUCCESS: 74 tests, 0 failures, 0 errors, 2 skipped |

With no Docker daemon, the database tests skip themselves and the build still reports success:

- `MySqlIntegrationTests`: 2 tests skipped (`@Testcontainers(disabledWithoutDocker = true)`).
- `PostgresIntegrationTests`: 0 tests run (an `assumeTrue(isDockerAvailable())` in `@BeforeAll`).

This is a usable fallback if Docker misbehaves on the day: 9 s against 21 s. But it is not the
full suite, so don't call it one on stage.

### Expected noise in the log

- `DockerClientProviderStrategy : Could not find a valid Docker environment`: only when Docker is absent.
- `RuntimeException: Expected: controller used to showcase what happens when an exception is thrown`:
  `CrashControllerIntegrationTests` provokes this on purpose.
- The Postgres test starts `docker-compose.yml`'s `postgres` service. It leaves a stopped
  `petclinic-loops-at-levels-postgres-1` container behind, which is harmless.

None of these is a failure. It's worth knowing in advance so none of them surprises you on stage.
