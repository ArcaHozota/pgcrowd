/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.struts2.dispatcher;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.StrutsConstants;
import org.apache.struts2.StrutsException;
import org.apache.struts2.views.freemarker.FreemarkerManager;

import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.location.Location;
import com.opensymphony.xwork2.util.location.LocationUtils;

import freemarker.template.Template;
import jp.co.sony.ppogah.common.PgCrowd2Constants;
import jp.co.sony.ppogah.config.ResponseLoginDto;
import jp.co.sony.ppogah.utils.CommonProjectUtils;

/**
 * Default implementation of
 * {@link org.apache.struts2.dispatcher.DispatcherErrorHandler} which sends
 * Error Report in devMode or
 * {@link javax.servlet.http.HttpServletResponse#sendError} otherwise.
 */
public class DefaultDispatcherErrorHandler implements DispatcherErrorHandler {

	private static final Logger LOG = LogManager.getLogger(DefaultDispatcherErrorHandler.class);

	private FreemarkerManager freemarkerManager;
	private boolean devMode;
	private Template template;

	protected HashMap<String, Object> createReportData(final Exception e, final List<Throwable> chain) {
		final HashMap<String, Object> data = new HashMap<>();
		data.put("exception", e);
		data.put("unknown", Location.UNKNOWN);
		data.put("chain", chain);
		data.put("locator", new Dispatcher.Locator());
		return data;
	}

	@Override
	public void handleError(final HttpServletRequest request, final HttpServletResponse response, final int code,
			final Exception e) {
		final Boolean devModeOverride = PrepareOperations.getDevModeOverride();
		if (devModeOverride != null ? devModeOverride : this.devMode) {
			this.handleErrorInDevMode(response, code, e);
		} else {
			this.sendErrorResponse(request, response, code, e);
		}
	}

	protected void handleErrorInDevMode(final HttpServletResponse response, final int code, final Exception e) {
		LOG.debug("Exception occurred during processing request: {}", e.getMessage(), e);
		try {
			final List<Throwable> chain = new ArrayList<>();
			Throwable cur = e;
			chain.add(cur);
			while ((cur = cur.getCause()) != null) {
				chain.add(cur);
			}

			final Writer writer = new StringWriter();
			this.template.process(this.createReportData(e, chain), writer);

			response.setContentType("text/html");
			response.getWriter().write(writer.toString());
			response.getWriter().close();
		} catch (final Exception exp) {
			try {
				LOG.debug("Cannot show problem report!", exp);
				response.sendError(code,
						"Unable to show problem report:\n" + exp + "\n\n" + LocationUtils.getLocation(exp));
			} catch (final IOException ex) {
				// we're already sending an error, not much else we can do if more stuff breaks
				LOG.warn("Unable to send error response, code: {};", code, ex);
			} catch (final IllegalStateException ise) {
				// Log illegalstate instead of passing unrecoverable exception to calling thread
				LOG.warn("Unable to send error response, code: {}; isCommited: {};", code, response.isCommitted(), ise);
			}
		}
	}

	@Override
	public void init(final ServletContext ctx) {
		try {
			final freemarker.template.Configuration config = this.freemarkerManager.getConfiguration(ctx);
			this.template = config.getTemplate("/org/apache/struts2/dispatcher/error.ftl");
		} catch (final IOException e) {
			throw new StrutsException(e);
		}
	}

	protected void sendErrorResponse(final HttpServletRequest request, final HttpServletResponse response,
			final int code, final Exception e) {
		ResponseLoginDto responseResult = null;
		try {
			// WW-1977: Only put errors in the request when code is a 500 error
			if (code == HttpServletResponse.SC_FORBIDDEN) {
				responseResult = new ResponseLoginDto(code, PgCrowd2Constants.MESSAGE_SPRINGSECURITY_REQUIRED_AUTH);
			} else {
				// WW-4103: Only logs error when application error occurred, not Struts error
				LOG.error("Exception occurred during processing request: {}", e.getMessage());
				responseResult = new ResponseLoginDto(code, e.getMessage());
			}
			CommonProjectUtils.renderString(response, responseResult);
		} catch (final IllegalStateException ise) {
			// Log illegalstate instead of passing unrecoverable exception to calling thread
			LOG.warn("Unable to send error response, code: {}; isCommited: {};", code, response.isCommitted(), ise);
		}
	}

	@Inject(StrutsConstants.STRUTS_DEVMODE)
	public void setDevMode(final String devMode) {
		this.devMode = BooleanUtils.toBoolean(devMode);
	}

	@Inject
	public void setFreemarkerManager(final FreemarkerManager freemarkerManager) {
		this.freemarkerManager = freemarkerManager;
	}
}
