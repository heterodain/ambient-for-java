package com.heterodain.ambient;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class AmbientTest {
	Ambient ambient = new Ambient(0, "****", "****");

	@Test
	public void testReadLatest10() throws Exception {
		String json = ambient.read(10);

		System.out.println(json);
	}

	@Test
	public void testReadPeriod() throws Exception {
		LocalDateTime start = LocalDateTime.now().minusDays(1);
		LocalDateTime end = LocalDateTime.now();
		String json = ambient.read(start, end);

		System.out.println(json);
	}

	@Test
	public void testSend() throws Exception {
		ambient.send(1.23D, 2.34D, 3.45D);
	}
}
