Owners now have an email address. It shows on the owner details page, and you can enter or change it on the add and edit owner forms. The full suite passed: 78 tests, 0 failures, 0 skipped. `MySqlIntegrationTests` and `PostgresIntegrationTests` really ran against Docker, and `spring-javaformat:validate` passes. Nothing is committed yet.

**What changed**
- **`Owner.java`:** a new `email` field with a getter and setter. It is checked with `@Email`, and the error text comes from a message key.
- **Databases:** an `email` column added to the `owners` table in all three `schema.sql` files (h2, mysql, postgres), and all three `data.sql` files give the seed owners addresses.
- **Templates:** the form gets an email field using the existing `inputField` fragment, and `ownerDetails.html` gets an Email row. Both use message keys, not hardcoded text.
- **Messages:** two new keys, `email` and `email.invalid`, in the English file and all 9 other language files.
- **Tests:**
  - In `OwnerControllerTests`: a new owner is saved with its email, a bad email shows an error on the form, the edit form is pre-filled with the email, an edit saves the new email, and the details page shows it.
  - The MySQL and Postgres integration tests now check that owner 1's page shows the seed email, so the new column is confirmed on both databases.

**Decisions** (for the PR description)
1. **Email is optional.** Nothing asked for it to be required, and existing owners have none. An empty field passes; anything else must look like an email address.
2. **No length check in the app.** The column is `VARCHAR(255)` on H2 and MySQL and `TEXT` on Postgres. Like the existing `address` field, there is no `@Size` limit, so an email longer than 255 characters would cause a database error on H2 or MySQL instead of a form error.
3. **Seed emails use `@example.com`,** a domain reserved for examples, so they are obviously fake (e.g. `george.franklin@example.com`). The data files had to change anyway, because the H2 and MySQL inserts list values by position.
4. **Email is left out of `Owner.toString()`,** even though the other personal fields are in it. Email is personal data and adding it would put it in more output. Whether the rule on owner personal data should list email explicitly is for the team to decide.
5. **Labels:** "Email" in English, with the error "Email must be a valid email address".
6. **Translations are my own, and a native speaker should check them before merge.** The labels are de "E-Mail", es "Correo electrónico", fa "ایمیل", hi "ईमेल", ja "メールアドレス", ko "이메일", pt "E-mail", ru "Электронная почта", tr "E-posta". The error messages are also translated. I didn't copy English into any of those files.