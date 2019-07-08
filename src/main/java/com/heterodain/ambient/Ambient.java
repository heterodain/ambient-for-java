package com.heterodain.ambient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ambient {
	private static Logger logger = LoggerFactory.getLogger(Ambient.class);

	private int channelId;
	private String readKey;
	private String writeKey;

	public Ambient(int channelId, String readKey, String writeKey) {
		this.channelId = channelId;
		this.readKey = readKey;
		this.writeKey = writeKey;
	}

	public void send(double... datas) throws IOException {
		List<String> dataStrs = new ArrayList<>();
		for (int i = 1; i <= datas.length; i++) {
			dataStrs.add("\"d" + i + "\":" + datas[i - 1]);
		}
		String json = "{\"writeKey\":\"" + writeKey + "\",\"data\":[{" + dataStrs.stream().collect(Collectors.joining(",")) + "}]}";

		String url = "http://54.65.206.59/api/v2/channels/" + channelId + "/dataarray";
		logger.debug("request > " + url);
		logger.debug("body > " + json);

		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		try (OutputStream os = conn.getOutputStream()) {
			IOUtils.write(json, os, "UTF-8");
		}
		int resCode = conn.getResponseCode();
		if (resCode != 200) {
			throw new IOException("Ambient Response Code " + resCode);
		}
	}

	public String read(LocalDate date) throws IOException {
		String url = "http://54.65.206.59/api/v2/channels/" + channelId + "/data?readKey=" + readKey + "&date=" + date.format(DateTimeFormatter.ISO_DATE);
		logger.debug("request > " + url);

		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestMethod("GET");
		int resCode = conn.getResponseCode();
		if (resCode != 200) {
			throw new IOException("Ambient Response Code " + resCode);
		}

		return IOUtils.toString(conn.getInputStream(), "UTF-8");
	}

	public String read(LocalDateTime start, LocalDateTime end) throws IOException {
		String url = "http://54.65.206.59/api/v2/channels/" + channelId + "/data?readKey=" + readKey;
		url += "&start=" + URLEncoder.encode(start.format(DateTimeFormatter.ISO_DATE_TIME), "UTF-8");
		url += "&end=" + URLEncoder.encode(end.format(DateTimeFormatter.ISO_DATE_TIME), "UTF-8");
		logger.debug("request > " + url);

		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestMethod("GET");
		int resCode = conn.getResponseCode();
		if (resCode != 200) {
			throw new IOException("Ambient Response Code " + resCode);
		}

		return IOUtils.toString(conn.getInputStream(), "UTF-8");
	}

	public String read(int n) throws IOException {
		String url = "http://54.65.206.59/api/v2/channels/" + channelId + "/data?readKey=" + readKey + "&n=" + n;
		logger.debug("request > " + url);

		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestMethod("GET");
		int resCode = conn.getResponseCode();
		if (resCode != 200) {
			throw new IOException("Ambient Response Code " + resCode);
		}

		return IOUtils.toString(conn.getInputStream(), "UTF-8");
	}

}
