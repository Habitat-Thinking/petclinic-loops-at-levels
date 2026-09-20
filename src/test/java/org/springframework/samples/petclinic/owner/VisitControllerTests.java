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

package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetFormatter;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.validation.BindingResult;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 * @author Wick Dynex
 */
@WebMvcTest(value = VisitController.class,
		includeFilters = @ComponentScan.Filter(value = VetFormatter.class, type = FilterType.ASSIGNABLE_TYPE))
@DisabledInNativeImage
@DisabledInAotMode
class VisitControllerTests {

	private static final int TEST_OWNER_ID = 1;

	private static final int TEST_PET_ID = 1;

	private static final int TEST_VET_ID = 2;

	private static final String ENGLISH_CONFIRMATION = "Your visit has been booked";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MessageSource messages;

	@MockitoBean
	private OwnerRepository owners;

	@MockitoBean
	private VetRepository vets;

	private Pet pet;

	@BeforeEach
	void init() {
		Owner owner = new Owner();
		this.pet = new Pet();
		owner.addPet(this.pet);
		this.pet.setId(TEST_PET_ID);
		given(this.owners.findById(TEST_OWNER_ID)).willReturn(Optional.of(owner));
		given(this.vets.findAll()).willReturn(List.of(vet(1, "James", "Carter"), vet(TEST_VET_ID, "Helen", "Leary")));
	}

	private static Vet vet(int id, String firstName, String lastName) {
		Vet vet = new Vet();
		vet.setId(id);
		vet.setFirstName(firstName);
		vet.setLastName(lastName);
		return vet;
	}

	@Test
	void initNewVisitForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}

	// T-1 — AS-1, FR-3
	@Test
	void initNewVisitFormListsVetsAndOffersATimeField() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("vets"))
			.andExpect(content().string(containsString("type=\"time\"")))
			.andExpect(content().string(containsString("James Carter")))
			.andExpect(content().string(containsString("Helen Leary")));
	}

	// T-2 — AS-1, FR-4
	@Test
	void initNewVisitFormDefaultsToTomorrowAtNine() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("value=\"" + LocalDate.now().plusDays(1) + "\"")))
			.andExpect(content().string(containsString("value=\"09:00\"")));
	}

	// T-3 — AS-2, FR-8
	@Test
	void processNewVisitFormSucceedsWithVetAndTime() throws Exception {
		mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID)
				.param("name", "George")
				.param("date", LocalDate.now().plusDays(1).toString())
				.param("startTime", "14:30")
				.param("vet", String.valueOf(TEST_VET_ID))
				.param("description", "Visit Description"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	// T-4 — AS-2, FR-1, FR-2, FR-8
	@Test
	void processNewVisitFormRecordsTheChosenVetAndTime() throws Exception {
		mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID)
				.param("date", LocalDate.now().plusDays(1).toString())
				.param("startTime", "14:30")
				.param("vet", String.valueOf(TEST_VET_ID))
				.param("description", "Visit Description"))
			.andExpect(status().is3xxRedirection());

		ArgumentCaptor<Owner> saved = ArgumentCaptor.forClass(Owner.class);
		verify(this.owners).save(saved.capture());
		Visit recorded = saved.getValue().getPet(TEST_PET_ID).getVisits().iterator().next();
		assertThat(recorded.getStartTime()).isEqualTo(LocalTime.of(14, 30));
		assertThat(recorded.getVet()).isNotNull();
		assertThat(recorded.getVet().getId()).isEqualTo(TEST_VET_ID);
	}

	// T-5 — AS-5, FR-5
	@Test
	void processNewVisitFormRejectsMissingVet() throws Exception {
		// The rendered assertion is here because a code-mode objection claimed this
		// span shows the generic word "Error" rather than the field's message, on the
		// reading that th:text (1300) runs after th:errors (1200). It does not:
		// th:errors is 1700 — 1200 is th:field — so th:errors runs last and wins the
		// element body. Pinned so the next reader of those precedences does not have
		// to re-derive it from a rendered page.
		mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID)
				.param("date", LocalDate.now().plusDays(1).toString())
				.param("startTime", "14:30")
				.param("description", "Visit Description"))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdateVisitForm"))
			.andExpect(model().attributeHasFieldErrorCode("visit", "vet", "required"))
			.andExpect(content().string(containsString("<span class=\"help-inline\">is required</span>")));

		verify(this.owners, never()).save(any());
	}

	// T-6 — AS-6, FR-6
	@Test
	void processNewVisitFormRejectsMissingStartTime() throws Exception {
		mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID)
				.param("date", LocalDate.now().plusDays(1).toString())
				.param("vet", String.valueOf(TEST_VET_ID))
				.param("description", "Visit Description"))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdateVisitForm"))
			.andExpect(model().attributeHasFieldErrorCode("visit", "startTime", "required"));

		verify(this.owners, never()).save(any());
	}

	// T-7 — AS-5, FR-5
	@Test
	void processNewVisitFormRejectsUnknownVet() throws Exception {
		// An unknown id is the vet's version of an unparseable time: the formatter
		// rejects it, so the field already carries a typeMismatch. Asserting the
		// count, not just the presence, is what pins the controller's guard —
		// without it the guard can be deleted and this test stays green.
		BindingResult result = (BindingResult) mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID)
				.param("date", LocalDate.now().plusDays(1).toString())
				.param("startTime", "14:30")
				.param("vet", "999")
				.param("description", "Visit Description"))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdateVisitForm"))
			.andExpect(model().attributeHasFieldErrors("visit", "vet"))
			.andReturn()
			.getModelAndView()
			.getModel()
			.get(BindingResult.MODEL_KEY_PREFIX + "visit");

		assertThat(result.getFieldErrors("vet")).as("errors on the vet field").hasSize(1);

		verify(this.owners, never()).save(any());
	}

	// FR-6 — an unparseable start time. Unreachable from a browser, whose time
	// input posts HH:mm or nothing, but reachable from any other client. The
	// binder already records a typeMismatch on the field and leaves it null, so
	// the controller's "required" rejection must stand aside or th:errors would
	// render two errors for one bad value.
	@Test
	void processNewVisitFormReportsOneErrorForAnUnparseableStartTime() throws Exception {
		BindingResult result = (BindingResult) mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID)
				.param("date", LocalDate.now().plusDays(1).toString())
				.param("startTime", "9am")
				.param("vet", String.valueOf(TEST_VET_ID))
				.param("description", "Visit Description"))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdateVisitForm"))
			.andExpect(model().attributeHasFieldErrors("visit", "startTime"))
			.andReturn()
			.getModelAndView()
			.getModel()
			.get(BindingResult.MODEL_KEY_PREFIX + "visit");

		assertThat(result.getFieldErrors("startTime")).as("errors on the start time field").hasSize(1);

		verify(this.owners, never()).save(any());
	}

	// FR-7 — a cleared date box. Reachable from an ordinary browser: the date
	// input has no `required` attribute, so clearing it posts `date=`, which binds
	// to null with no binder error. The date rule cannot fire on a null, and the
	// Visit constructor's tomorrow default does not survive the bind, so without
	// the controller's "required" rejection this request would persist a visit
	// with a vet and a start time and no date — the mirror of the hole
	// `start_time NOT NULL` closed. The two size assertions pin that the empty
	// date costs exactly one error and nothing else on the form. They do not
	// exercise the rejection's `!result.hasFieldErrors("date")` guard — only an
	// unparseable date reaches that, and no test posts one, which is the same
	// asymmetry T-7 and the unparseable-start-time test close for the other two
	// fields.
	@Test
	void processNewVisitFormReportsOneErrorForAnEmptyDate() throws Exception {
		BindingResult result = (BindingResult) mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID).param("date", "")
				.param("startTime", "14:30")
				.param("vet", String.valueOf(TEST_VET_ID))
				.param("description", "Visit Description"))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdateVisitForm"))
			.andExpect(model().attributeHasFieldErrorCode("visit", "date", "required"))
			.andReturn()
			.getModelAndView()
			.getModel()
			.get(BindingResult.MODEL_KEY_PREFIX + "visit");

		assertThat(result.getFieldErrors("date")).as("errors on the date field").hasSize(1);
		assertThat(result.getErrorCount()).as("errors on the whole form").isEqualTo(1);

		verify(this.owners, never()).save(any());
	}

	// T-8 — AS-7, FR-7
	@Test
	void processNewVisitFormHasErrorsWhenVisitDateIsNotInFuture() throws Exception {
		mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID)
				.param("name", "George")
				.param("date", LocalDate.now().toString())
				.param("startTime", "14:30")
				.param("vet", String.valueOf(TEST_VET_ID))
				.param("description", "Visit Description"))
			.andExpect(model().attributeHasFieldErrors("visit", "date"))
			.andExpect(model().attributeHasFieldErrorCode("visit", "date", "typeMismatch.visitDate"))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}

	// T-9 — AS-8, FR-7
	@Test
	void processNewVisitFormRejectsBlankDescription() throws Exception {
		mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID)
				.param("date", LocalDate.now().plusDays(1).toString())
				.param("startTime", "14:30")
				.param("vet", String.valueOf(TEST_VET_ID))
				.param("description", ""))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdateVisitForm"))
			.andExpect(model().attributeHasFieldErrors("visit", "description"));

		verify(this.owners, never()).save(any());
	}

	// T-10 — AS-4, FR-10, FR-14
	@Test
	void previousVisitsShowVetAndTime() throws Exception {
		Visit previous = new Visit();
		previous.setId(42);
		previous.setDate(LocalDate.now().minusDays(7));
		previous.setStartTime(LocalTime.of(14, 30));
		previous.setVet(vet(TEST_VET_ID, "Helen", "Leary"));
		previous.setDescription("rabies shot");
		this.pet.addVisit(previous);

		String body = mockMvc
			.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID).param("lang", "en"))
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString();

		// Scoped to the previous-visits section: the vet chooser above it also
		// renders every vet's name, so a page-wide check would pass with the
		// previous-visits table unchanged.
		String previousVisits = previousVisitsSection(body);
		assertThat(previousVisits).as("the previous visit's start time").contains("14:30");
		assertThat(previousVisits).as("the previous visit's vet").contains("Helen Leary");
	}

	// T-19 — AS-14, FR-18
	//
	// German is selected with ?lang=de, not with an Accept-Language header:
	// WebConfiguration resolves the locale with a SessionLocaleResolver that
	// defaults to English and a LocaleChangeInterceptor on the lang parameter,
	// so an Accept-Language header renders the page in English and this test
	// and T-20 and T-21 would pass or fail for a reason that has nothing to do
	// with where their words come from.
	@Test
	void visitFormLabelsAllComeFromMessageKeys() throws Exception {
		String body = mockMvc
			.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID).param("lang", "de"))
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString();

		assertThat(body).as("the form's date field label, in German").doesNotContain(">Date<");
		assertThat(body).as("the form's description field label, in German").doesNotContain(">Description<");
		assertThat(body).as("the form's description field label, in German").contains(">Beschreibung<");
		// Matched with the angle brackets on purpose: the pet table's birthDate
		// heading renders Geburtsdatum, which contains "Datum" as a substring.
		assertThat(body).as("no column named with the date key rather than the visitDate key")
			.doesNotContain(">Datum<");
		// Once as the date field's label, once as the previous-visits heading.
		assertThat(StringUtils.countOccurrencesOf(body, "Besuchsdatum"))
			.as("the visitDate key, as the date field's label and as the previous-visits heading")
			.isEqualTo(2);
	}

	// T-20 — AS-16, FR-19
	@Test
	void visitFormHeadingComesEntirelyFromOneMessageKey() throws Exception {
		String body = mockMvc
			.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID).param("lang", "de"))
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString();

		// Scoped to the <h2>: "Besuch" also appears in the addVisit button and
		// inside Besuchsdatum, so a page-wide check would pass with the heading
		// still half in English.
		String heading = heading(body);
		assertThat(heading).as("the German heading").doesNotContain("New");
		assertThat(heading).as("the German heading").doesNotContain("Visit");
		// The whole heading, not a substring of it: a two-key composition would
		// render "Neu Besuch" and fail here.
		assertThat(heading).as("the whole heading, from the newVisit key")
			.isEqualTo(messages.getMessage("newVisit", null, Locale.GERMAN));
	}

	// T-21 — AS-15, FR-20
	@Test
	void confirmationMessageComesFromAMessageKey() throws Exception {
		Object message = mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID).param("lang", "de")
				.param("date", LocalDate.now().plusDays(1).toString())
				.param("startTime", "14:30")
				.param("vet", String.valueOf(TEST_VET_ID))
				.param("description", "Visit Description"))
			.andExpect(status().is3xxRedirection())
			.andReturn()
			.getFlashMap()
			.get("message");

		assertThat(message).as("the confirmation message, in German").isNotEqualTo(ENGLISH_CONFIRMATION);
		assertThat(message).as("the confirmation message, from the visitBooked key")
			.isEqualTo(messages.getMessage("visitBooked", null, Locale.GERMAN));
	}

	private static String previousVisitsSection(String body) {
		int start = body.indexOf("Previous Visits");
		assertThat(start).as("previous-visits section in the rendered form").isNotEqualTo(-1);
		return body.substring(start);
	}

	private static String heading(String body) {
		int open = body.indexOf("<h2");
		assertThat(open).as("<h2> heading in the rendered form").isNotEqualTo(-1);
		int text = body.indexOf('>', open) + 1;
		int close = body.indexOf("</h2>", text);
		return body.substring(text, close).replaceAll("\\s+", " ").trim();
	}

}
