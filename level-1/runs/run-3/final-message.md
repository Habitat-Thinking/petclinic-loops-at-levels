Owners now have an email address. You can enter or change it on the create and edit owner form, and it appears on the owner details page under Telephone. All 78 tests pass, including the MySQL and Postgres integration tests. I haven't committed anything.

**Decision for you:** email is **optional**. Existing owners don't have one, so requiring it would block editing any of them until an email was added. If an email is entered, it has to be a valid address or the form shows an error. If you'd rather make it required, it's a one-line change (add `@NotBlank`).

**What changed**
- **`Owner.java`:** a new `email` field with a getter and setter; it's also included in `toString`.
- **Pages:** `createOrUpdateOwnerForm.html` has the new input and `ownerDetails.html` has the new row. I didn't add a column to the owner search results list, since you didn't ask for it.
- **Database:** an `email` column in the H2, MySQL and Postgres schemas.
- **Sample data:** the 10 sample owners now have emails like `george.franklin@example.com`. The H2 and MySQL sample data lists values in column order, so it had to change once the column was added.
- **Translations:** the new label and error message are in the base messages file and all 9 translation files, which a test requires. I wrote the translations myself, so a native speaker should check them.
- **Tests:** `OwnerControllerTests` now covers creating an owner with and without an email, rejecting an invalid email, and showing the email on the details page. `ClinicServiceTests` checks that the email is loaded from the database.

**Existing databases:** the MySQL and Postgres setup scripts run on every startup but only create tables that don't exist yet, so a database you already have won't get the new column.
- **Postgres:** I added `ALTER TABLE owners ADD COLUMN IF NOT EXISTS email TEXT;`, which adds it automatically.
- **MySQL:** has no equivalent statement, so an existing database needs `ALTER TABLE owners ADD COLUMN email VARCHAR(255);` run once by hand. Without it, the app will fail against that database.