Done. Owners now have an email address, shown on the details page and editable in the create/edit form.

**What changed**

- `Owner.java` — new `email` field with `@NotBlank` + `@Email(message = "{email.invalid}")`, getter/setter, and a `toString` entry.
- **Schemas** (h2, mysql, postgres) — `email` column appended after `telephone`. Appending matters: the h2/mysql seed files use positional `INSERT ... VALUES`.
- **Seed data** (all three) — an `@example.com` address derived from each of the 10 owners' names, so no demo record is left without one.
- **Templates** — a row on `ownerDetails.html` rendered as a `mailto:` link, and an input on `createOrUpdateOwnerForm.html`. I added an `'email'` case to the shared `inputField` fragment so the field gets `type="email"` rather than reusing `'text'`.
- **i18n** — `email` and `email.invalid` added to `messages.properties` and all 9 translated locales, matching each file's existing style (`messages_hi` uses `\u` escapes, the rest raw UTF-8).
- **Tests** — existing owner fixtures and form posts updated, plus a new `processCreationFormHasEmailFormatError` case; blank-email assertions added to the existing error tests.
- **JMeter plan** — the owner-edit sampler posts an email param too, so the load test keeps submitting a valid form.

**Verification**

All 77 tests pass, including the MySQL and Postgres testcontainer integration tests (both schemas exercised against real engines) and `I18nPropertiesSyncTest`, which checks the locale files stay in sync. `spring-javaformat:validate` is clean. I also booted the app and confirmed against live pages: the details page renders the mailto link, the edit form pre-populates, creating and editing an owner persists the address, and a malformed address produces the inline message "Email must be a well-formed email address" while a blank one is rejected.

**One decision worth your call:** I made email **required**, consistent with every other `Owner` field (`address`, `city`, `telephone` are all `@NotBlank`). The tradeoff is that any owner row predating this change has a NULL email and can't be saved from the edit form until one is filled in. That's a non-issue for the bundled demo data since I seeded all 10 rows, but it would matter against an existing MySQL/Postgres database with real records. If you'd rather it be optional, drop the `@NotBlank` from `Owner.java:66` — `@Email` alone permits null — and I'll adjust the affected test assertions.