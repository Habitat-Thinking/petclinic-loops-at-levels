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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test class for {@link VetFormatter}
 */
@ExtendWith(MockitoExtension.class)
@DisabledInNativeImage
class VetFormatterTests {

	@Mock
	private VetRepository vets;

	private VetFormatter vetFormatter;

	@BeforeEach
	void setup() {
		this.vetFormatter = new VetFormatter(this.vets);
	}

	// T-13 — FR-3
	@Test
	void parsesAVetById() throws ParseException {
		given(this.vets.findAll()).willReturn(makeVets());

		Vet vet = this.vetFormatter.parse("2", Locale.ENGLISH);

		assertThat(vet.getId()).isEqualTo(2);
	}

	// T-14 — FR-5, AS-5
	@Test
	void rejectsAnIdThatIsNotAVet() {
		given(this.vets.findAll()).willReturn(makeVets());

		Assertions.assertThrows(ParseException.class, () -> this.vetFormatter.parse("999", Locale.ENGLISH));
	}

	/**
	 * Helper method to produce some sample vets just for test purpose
	 */
	private List<Vet> makeVets() {
		return List.of(vet(1, "James", "Carter"), vet(2, "Helen", "Leary"));
	}

	private Vet vet(int id, String firstName, String lastName) {
		Vet vet = new Vet();
		vet.setId(id);
		vet.setFirstName(firstName);
		vet.setLastName(lastName);
		return vet;
	}

}
