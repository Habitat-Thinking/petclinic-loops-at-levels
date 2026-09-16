# Project Conventions

## Conventions (extracted 2026-09-16)

The must-follow rules for this project live in `HARNESS.md` under
Context → Conventions. Read them first; the items below are
should-follow defaults and preferences that sit on top of them.

### Should-follow

Exceptions are allowed, but state the reason in the PR's "Decisions"
section.

1. **Reuse the existing fragments.** Form fields use
   `fragments/inputField` and `fragments/selectField`. Add a new
   fragment only for a genuinely new kind of field, and extend an
   existing fragment file where possible.
2. **Tests check behaviour a user can observe.** Use `@WebMvcTest` with
   MockMvc and assert on status, view name, model attributes and
   rendered content. Unit tests on validators and entities are fine when
   they assert behaviour (e.g. "`PetValidator` rejects a blank name"),
   never structure or private state.
3. **No abstraction for a single caller.** An interface or generic needs
   a second real user before it earns its place. Spring Data repository
   interfaces (`OwnerRepository`, `PetTypeRepository`, `VetRepository`)
   are the exception — the interface is the implementation.
4. **Smallest diff that works.** Extend an existing file or fragment
   before creating a new one.

### Style preferences

1. **Match PetClinic's plainness.** This is a teaching codebase: prefer
   the boring construction a newcomer can read.
2. **Deleting beats adding.** A refactoring that removes code is worth
   more than one that adds a layer.

### Not encodable yet

- **"Plainness"** needs decomposing into observable cases (e.g. loops
  over clever streams, no reflection or annotation tricks) before it
  can be checked.
- **"Needed by the change's purpose"** (the no-unrelated-reformatting
  rule) needs worked examples to settle where cleanup ends and noise
  begins.
