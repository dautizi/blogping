package com.danieleautizi.blogping.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Constraint 'dictionary' to centralize
 * attributes in a single interface to implement
 * 
 * @author d.autizi
 * @version 1.0
 */
public interface Constraint {
	
	public static final List<String> ALLOWED_PARAMS = new ArrayList<String>(Arrays.asList("name", "url"));
	
	public static final String APP_PROPERTY = "properties";
	public static final String PROPERTIES_FILE = "blogping.properties";
	public static final String BLACKLIST_FILE = "blacklist.properties";
	
	public static final String DOWN_OF_SERVICE_CODE = "weblog.down.code";
	public static final String DOWN_OF_SERVICE = "weblog.down";
	
	public static final String NAME_MAX_LENGTH = "weblog.name.max-length";
	public static final String URL_MAX_LENGTH = "weblog.url.max-length";
	
	public static final String URL_NOT_VALID_CODE = "weblog.code.ko.url.not-valid";
	public static final String URL_NOT_VALID = "weblog.ko.url.not-valid";
	
	public static final String SAVE_ERROR_CODE = "weblog.code.ko.save";
	public static final String SAVE_ERROR = "weblog.ko.save";
	
	public static final String NAME_OVERSIZED_CODE = "weblog.code.ko.name.oversized";
	public static final String NAME_OVERSIZED = "weblog.ko.name.oversized";
	public static final String NAME_MISS_CODE = "weblog.code.ko.name.miss";
	public static final String NAME_MISS = "weblog.ko.name.miss";

	public static final String URL_OVERSIZED_CODE = "weblog.code.ko.url.oversized";
	public static final String URL_OVERSIZED = "weblog.ko.url.oversized";
	public static final String URL_MISS_CODE = "weblog.code.ko.url.miss";
	public static final String URL_MISS = "weblog.ko.url.miss";
	public static final String HOST_BLACKLISTED_CODE = "weblog.code.ko.url.host-blacklisted";
	public static final String HOST_BLACKLISTED = "weblog.ko.url.host-blacklisted";
	public static final String UNKNOWN_PARAMETERS_CODE = "weblog.code.ko.url.unknown-parameters";
	public static final String UNKNOWN_PARAMETERS = "weblog.ko.url.unknown-parameters";

	public static final String NAME_URL_MISS_CODE = "weblog.code.ko.url-name.miss";
	public static final String NAME_URL_MISS = "weblog.ko.url-name.miss";
	public static final String NAME_URL_INVALID_CODE = "weblog.code.ko.url-name.invalid";
	public static final String NAME_URL_INVALID = "weblog.ko.url-name.invalid";
	public static final String PARAM_NUMBER_INVALID_CODE = "weblog.code.ko.param-number.invalid";
	public static final String PARAM_NUMBER_INVALID = "weblog.code.ko.param-number.invalid";
	
	public static final String WEBLOG_SAVE_CODE = "weblog.code.ok.blog.save";
	public static final String WEBLOG_SAVE = "weblog.ok.blog.save";

	public static final String WEBLOG_REMOVE_CODE = "weblog.code.ok.blog.remove";
	public static final String WEBLOG_REMOVE = "weblog.code.ok.blog.remove";
	
	public static final String DEFAULT_PERSISTENCE_SOURCE = "hashdb";
	public static final String PERSISTENCE_PROPERTY = "persistence_source";

}
