package com.voltor.futureleave.localization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LocalizedMessage {

	private String defaultDescription;
	private String titleKey;
	private String descriptionKey;
	private List<String> detailKeys;
	private Map<String, String> messageArguments;
	private Map<String, String> localizedMessageArguments;

	public LocalizedMessage(String defaultDescription, ILocalizedExceptionCode titleKey) {
		this.titleKey = Objects.requireNonNull(titleKey.getCode());
		this.defaultDescription = Objects.requireNonNull(defaultDescription);
		this.detailKeys = new ArrayList<>();
	}

	public String getDefaultDescription() {
		return defaultDescription;
	}

	public void setDefaultDescription(String defaultDescription) {
		this.defaultDescription = defaultDescription;
	}

	public String getTitleKey() {
		return titleKey;
	}

	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}

	public String getDescriptionKey() {
		return descriptionKey;
	}

	public void setDescriptionKey(String descriptionKey) {
		this.descriptionKey = descriptionKey;
	}

	public List<String> getDetailKeys() {
		return detailKeys;
	}

	public void setDetailKeys(List<String> detailKeys) {
		this.detailKeys = detailKeys;
	}

	public Map<String, String> getMessageArguments() {
		return messageArguments;
	}

	public void setMessageArguments(Map<String, String> messageArguments) {
		this.messageArguments = messageArguments;
	}

	public Map<String, String> getLocalizedMessageArguments() {
		return localizedMessageArguments;
	}

	public void setLocalizedMessageArguments(Map<String, String> localizedMessageArguments) {
		this.localizedMessageArguments = localizedMessageArguments;
	}
}
