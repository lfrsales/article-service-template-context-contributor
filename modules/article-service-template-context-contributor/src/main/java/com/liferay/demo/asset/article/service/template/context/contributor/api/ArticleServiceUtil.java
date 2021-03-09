/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.demo.asset.article.service.template.context.contributor.api;

import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.journal.service.JournalArticleServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.PortletRequestModel;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author evanthibodeau
 */
public class ArticleServiceUtil {

	public ArticleServiceUtil(HttpServletRequest httpServletRequest) {
		_httpServletRequest = httpServletRequest;

		_themeDisplay = (ThemeDisplay)_httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		_groupId = _themeDisplay.getSiteGroupId();

		_languageId = _themeDisplay.getLanguageId();

		_portletRequestModel = getPortletRequestModel(_httpServletRequest);
	}

	public String getContentByClassPK(long classPK) {
		try {
			JournalArticle journalArticle = getArticleByClassPK(classPK);

			return getContent(
				journalArticle, journalArticle.getDDMTemplateKey());
		}
		catch (Exception e) {
			return getNoArticleWithClassPKMessage(classPK);
		}
	}

	public String getContentByClassPK(long classPK, String ddmTemplateKey) {
		try {
			JournalArticle journalArticle = getArticleByClassPK(classPK);

			return getContent(journalArticle, ddmTemplateKey);
		}
		catch (Exception e) {
			return getNoArticleWithClassPKMessage(classPK);
		}
	}

	public String getContentByPrimaryKey(String primaryKey) {
		try {
			return JournalArticleServiceUtil.getArticleContent(
				_groupId, primaryKey, _languageId, _portletRequestModel,
				_themeDisplay);
		}
		catch (Exception e) {
			return getNoArticleWithPrimaryKeyMessage(primaryKey);
		}
	}

	public String getContentByPrimaryKey(
		String primaryKey, String ddmTemplateKey) {

		try {
			JournalArticle journalArticle =
				JournalArticleServiceUtil.getArticle(_groupId, primaryKey);

			return getContent(journalArticle, ddmTemplateKey);
		}
		catch (Exception e) {
			return getNoArticleWithPrimaryKeyMessage(primaryKey);
		}
	}

	protected JournalArticle getArticleByClassPK(long classPK) {
		return JournalArticleLocalServiceUtil.fetchLatestArticle(classPK);
	}

	protected String getContent(
			JournalArticle journalArticle, String ddmTemplateKey)
		throws PortalException {

		return JournalArticleLocalServiceUtil.getArticleContent(
			journalArticle, ddmTemplateKey, Constants.VIEW, _languageId,
			_portletRequestModel, _themeDisplay);
	}

	protected String getNoArticleWithClassPKMessage(long classPK) {
		return "Unable to get article with classPK: " + classPK + ".";
	}

	protected String getNoArticleWithPrimaryKeyMessage(String primaryKey) {
		return "Unable to get article with primaryKey: " + primaryKey + ".";
	}

	protected PortletRequestModel getPortletRequestModel(
		HttpServletRequest httpServletRequest) {

		PortletRequest portletRequest =
			(PortletRequest)httpServletRequest.getAttribute(
				JavaConstants.JAVAX_PORTLET_REQUEST);
		PortletResponse portletResponse =
			(PortletResponse)httpServletRequest.getAttribute(
				JavaConstants.JAVAX_PORTLET_RESPONSE);

		if ((portletRequest == null) || (portletResponse == null)) {
			return null;
		}

		return new PortletRequestModel(portletRequest, portletResponse);
	}

	private long _groupId;
	private HttpServletRequest _httpServletRequest;
	private String _languageId;
	private PortletRequestModel _portletRequestModel;
	private ThemeDisplay _themeDisplay;

}