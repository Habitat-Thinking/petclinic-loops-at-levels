# Add an email address to owners

The task said: store an email per owner, show it on the owner details page, and make it
editable on the create and edit forms. Everything below is a choice the task left open.

- **Decision**: email is optional.
  **Alternatives**: `@NotBlank`, like the other owner fields.
  **Why**: the task said "add", not "require". Owners created before this change have
  no address, and a required field would block every edit to them until someone makes
  one up.

- **Decision**: when an email is given, it must pass `@Email` and be at most 255
  characters. Both failures show one message, `email.invalid`.
  **Alternatives**: no format check; a stricter regex like the telephone's; a separate
  length message.
  **Why**: `@Email` comes with the validation starter we already have, so no new
  dependency. Without the length check, a long value would reach the database and fail
  there with a 500 error. An address over 254 characters is already invalid under
  RFC 5321, so one "not a valid address" message covers both cases honestly.
  `@Email` accepts some addresses a mail server would still refuse, such as `a@b`.
  That is accepted as good enough.

- **Decision**: a blank field is stored as an empty string, not `NULL`.
  **Alternatives**: register a `StringTrimmerEditor` so blanks become `NULL`.
  **Why**: that is how every other owner field already behaves. Changing the binder
  would affect all of them, which is outside this change.

- **Decision**: the column is `email VARCHAR(255)` in h2 and mysql, `email TEXT` in
  postgres, nullable, added as the last column.
  **Alternatives**: `VARCHAR(254)`.
  **Why**: it matches `address` and the existing postgres style, and it agrees with the
  `@Size` limit.

- **Decision**: the seed data gives each of the ten sample owners a fictional
  `firstname.lastname@example.com` address.
  **Alternatives**: leave the seed email `NULL`.
  **Why**: h2 and mysql insert by position, so every owner row had to change anyway.
  `example.com` is reserved and can never belong to a real person, which satisfies the
  "obviously fictional" rule. It also means the new field shows up in the demo data.

- **Decision**: the label is "Email", with message keys `email` and `email.invalid`.
  It goes after Telephone on both the form and the details page.
  **Alternatives**: "Email Address"; putting it next to the name.
  **Why**: it is short and matches the one-word labels around it.

- **Decision**: the form field uses the existing `inputField` fragment with type
  `text`, not an HTML `type="email"` input.
  **Alternatives**: add an `email` case to the fragment for the browser's own checking.
  **Why**: the server check is what counts, and a text field needs no fragment change
  (smallest diff). A browser check would also stop the server-side error message from
  ever being seen.

- **Decision**: email is treated as owner personal data under directive 6. It is not
  added to `Owner.toString()` and is not logged anywhere.
  **Alternatives**: add it to `toString()` like the other fields.
  **Why**: directive 6 does not name email, but it is at least as identifying as a
  telephone number.
  **Flag for a human**: `Owner.toString()` already includes `firstName`, `lastName`,
  `address`, `city` and `telephone`, which is at odds with directive 6. I left it alone
  because fixing it is not part of this change.

- **Decision**: email is not shown on the owners list page or used in search.
  **Why**: the task named only the details page and the forms.

- **Decision**: I wrote the translations for all nine locale bundles myself, not
  copied from English.
  **Flag for a human**: they are short, standard UI strings (de "E-Mail", es "Correo
  electrónico", fa "ایمیل", hi "ईमेल", ja "メールアドレス", ko "이메일", pt "E-mail",
  ru "Электронная почта", tr "E-posta") plus a matching "invalid address" sentence.
  A native speaker has not reviewed them, so please have one check before release.
