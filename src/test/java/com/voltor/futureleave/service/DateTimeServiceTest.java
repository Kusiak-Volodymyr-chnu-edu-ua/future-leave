package com.voltor.futureleave.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

public class DateTimeServiceTest {

	@Test
	public void shouldReturnNowWithUTC() {
		assertEquals(ZoneOffset.UTC, DateTimeService.now().getOffset());
	}

	@Test
	public void shouldReturnNowWithOffset() {
		assertEquals(ZoneOffset.MIN, DateTimeService.now(ZoneOffset.MIN).getOffset());
	}

	@Test
	public void shouldConvertToUTC() {
		ZonedDateTime dateTimeWithDifferentOffset = ZonedDateTime.now(ZoneOffset.MIN);
		assertEquals(ZoneOffset.UTC, DateTimeService.toUTC(dateTimeWithDifferentOffset).getOffset());
	}
}
