/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.harness;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

/**
 * Enforces the "Schema parity across databases" constraint from HARNESS.md.
 *
 * The application supports H2, MySQL and Postgres, and each has its own schema file. A
 * column added to one and forgotten in the others is invisible until someone runs the
 * application against that database — which, without Docker, may be nobody until
 * production.
 *
 * This is deliberately a plain test with no dependency beyond JUnit: the rule has to hold
 * when no agent is watching, and when whatever wrote the change has moved on.
 */
class SchemaParityTest {

	private static final Path DB = Path.of("src/main/resources/db");

	/** CREATE TABLE <name> ( ... ) — the optional IF NOT EXISTS varies by dialect. */
	private static final Pattern TABLE = Pattern.compile(
			"CREATE\\s+TABLE\\s+(?:IF\\s+NOT\\s+EXISTS\\s+)?([a-z_]+)\\s*\\(([^;]*?)\\)\\s*[a-z=\\s]*;",
			Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	/** A column line: name, type, and a length when the dialect declares one. */
	private static final Pattern COLUMN = Pattern.compile("^\\s*([a-z_]+)\\s+([A-Za-z_]+)(?:\\((\\d+)\\))?",
			Pattern.CASE_INSENSITIVE);

	private static final Set<String> NOT_COLUMNS = Set.of("index", "key", "primary", "unique", "constraint", "foreign");

	@Test
	void everyColumnExistsInEveryDialect() throws IOException {
		Map<String, Map<String, Integer>> h2 = parse("h2");
		StringBuilder report = new StringBuilder();

		for (String dialect : new String[] { "mysql", "postgres" }) {
			Map<String, Map<String, Integer>> other = parse(dialect);
			for (var table : h2.entrySet()) {
				Map<String, Integer> theirs = other.get(table.getKey());
				if (theirs == null) {
					report.append("  table '")
						.append(table.getKey())
						.append("' is missing from ")
						.append(dialect)
						.append('\n');
					continue;
				}
				Set<String> missing = new TreeSet<>(table.getValue().keySet());
				missing.removeAll(theirs.keySet());
				for (String column : missing) {
					report.append("  ")
						.append(table.getKey())
						.append('.')
						.append(column)
						.append(" exists in h2 but not in ")
						.append(dialect)
						.append('\n');
				}
			}
		}

		if (!report.isEmpty()) {
			fail("Schema parity violated — a change landed in one database and not the others:\n" + report
					+ "\nHARNESS.md: schema changes land in h2, mysql and postgres in the same change.");
		}
	}

	@Test
	void lengthLimitsAgreeWhereBothDeclareThem() throws IOException {
		Map<String, Map<String, Integer>> h2 = parse("h2");
		Map<String, Map<String, Integer>> mysql = parse("mysql");
		StringBuilder report = new StringBuilder();

		for (var table : h2.entrySet()) {
			Map<String, Integer> theirs = mysql.getOrDefault(table.getKey(), Map.of());
			for (var column : table.getValue().entrySet()) {
				Integer ours = column.getValue();
				Integer them = theirs.get(column.getKey());
				if (ours != null && them != null && !ours.equals(them)) {
					report.append("  ")
						.append(table.getKey())
						.append('.')
						.append(column.getKey())
						.append(": h2 declares ")
						.append(ours)
						.append(", mysql declares ")
						.append(them)
						.append('\n');
				}
			}
		}

		// Postgres uses TEXT throughout and declares no lengths, so it is not compared
		// here. Column presence is covered by the test above.
		if (!report.isEmpty()) {
			fail("Length limits disagree between h2 and mysql — the same value would be accepted "
					+ "by one database and rejected by the other:\n" + report);
		}
	}

	/** table -> (column -> declared length, or null when the dialect declares none). */
	private Map<String, Map<String, Integer>> parse(String dialect) throws IOException {
		String sql = Files.readString(DB.resolve(dialect).resolve("schema.sql"));
		Map<String, Map<String, Integer>> tables = new LinkedHashMap<>();

		Matcher table = TABLE.matcher(sql);
		while (table.find()) {
			Map<String, Integer> columns = tables.computeIfAbsent(table.group(1).toLowerCase(),
					key -> new LinkedHashMap<>());
			for (String line : table.group(2).split(",\\s*\n")) {
				Matcher column = COLUMN.matcher(line);
				if (!column.find()) {
					continue;
				}
				String name = column.group(1).toLowerCase();
				if (NOT_COLUMNS.contains(name)) {
					continue;
				}
				columns.put(name, column.group(3) == null ? null : Integer.valueOf(column.group(3)));
			}
		}

		if (tables.isEmpty()) {
			throw new IllegalStateException("parsed no tables from " + dialect + "/schema.sql");
		}
		return tables;
	}

	/**
	 * Guard against the parser silently matching nothing and the tests passing vacuously.
	 */
	@Test
	void theParserActuallyFindsTheSchema() throws IOException {
		Set<String> expected = new LinkedHashSet<>(Set.of("owners", "pets", "vets", "specialties"));
		for (String dialect : new String[] { "h2", "mysql", "postgres" }) {
			Set<String> found = parse(dialect).keySet();
			if (!found.containsAll(expected)) {
				fail(dialect + "/schema.sql: parser found " + found + ", expected at least " + expected
						+ " — the parser is broken, so the parity tests above prove nothing");
			}
		}
	}

}
