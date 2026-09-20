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
package org.springframework.samples.petclinic.vet;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;

/**
 * Instructs Spring MVC on how to parse and print elements of type 'Vet', so that the vet
 * chooser on the visit form can post a vet and get a {@link Vet} back.
 *
 * This mirrors {@code PetTypeFormatter}, with one deliberate difference: it works in vet
 * ids rather than in displayed names. Pet type names are unique by construction, so
 * parsing one by name is safe; two vets can share a name, so parsing by name would
 * silently pick one of them. The id is the only stable handle a form control can carry.
 */
@Component
public class VetFormatter implements Formatter<Vet> {

	private final VetRepository vets;

	public VetFormatter(VetRepository vets) {
		this.vets = vets;
	}

	@Override
	public String print(Vet vet, Locale locale) {
		Integer id = vet.getId();
		return id != null ? id.toString() : "";
	}

	@Override
	public Vet parse(String text, Locale locale) throws ParseException {
		Collection<Vet> findVets = this.vets.findAll();
		for (Vet vet : findVets) {
			if (Objects.equals(String.valueOf(vet.getId()), text)) {
				return vet;
			}
		}
		throw new ParseException("vet not found: " + text, 0);
	}

}
