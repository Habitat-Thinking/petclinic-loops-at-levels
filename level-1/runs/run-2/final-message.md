Owners now have an email address. It shows on the owner details page and can be entered when you create or edit an owner. All 78 tests pass, including the MySQL and Postgres integration tests, which load the updated schema and sample data. I haven't opened the pages in a browser myself.

**What changed**
- **`Owner.java`:** new `email` field with a getter and setter, and it's included in `toString()`. It's checked with `@Email`, so a badly formed address comes back as a form error.
- **Database:** an `email` column in the `owners` table for H2, Postgres and MySQL. The 10 sample owners now have made-up addresses like `george.franklin@example.com`.
- **Pages:** an email field on the create/edit form, and an Email row under Telephone on the details page. I added an `email` input type to `fragments/inputField.html`, so the browser also checks the format.
- **Translations:** an `email` label in all the language files.
- **Tests:** the controller tests check that the email saves and displays, and that an invalid email is rejected on both create and edit. `ClinicServiceTests` checks that the email is saved to the database and read back.

**Decisions for you**
- **Email is optional.** Telephone is required, but making email required would block editing any owner saved before this change until someone adds one. Adding `@NotBlank` to the field makes it required.
- **No length limit on email.** Address and city don't have one either. On H2 and MySQL an email over 255 characters would cause a server error instead of a form error. Adding `@Size(max = 255)` to the field fixes that.

**Existing databases:** the schema scripts only create tables that don't exist yet, so an existing `owners` table won't get the new column automatically. For Postgres I added `ALTER TABLE owners ADD COLUMN IF NOT EXISTS email`, so it updates itself. MySQL has no equivalent command, so a MySQL database that outlives its container needs the column added by hand. The databases in `docker-compose.yml` are wiped each time their container is recreated, so this doesn't affect them.

Nothing is committed yet.