# Article Service Template Context Contributor

Developed against Liferay DXP 7.3, 7.2

Built with [Liferay Workspace](https://help.liferay.com/hc/en-us/articles/360029147471-Liferay-Workspace) and [Blade CLI](https://help.liferay.com/hc/en-us/articles/360029147071-Blade-CLI).

## Usage

Follow the steps below to build and deploy or copy the modules from the [releases](../../releases/latest) page to your Liferay's deploy folder.

The "Article Service Template Context Contributor" makes a `articleService` object available in your freemarker templates. This object exposes a number of different methods to let you easily get the rendered content of a web content article.

The following methods are available.

| Methods                                                           |
| ----------------------------------------------------------------- |
| getContentByClassPK(long classPK)                                 |
| getContentByClassPK(long classPK, String ddmTemplateKey)          |
| getContentByPrimaryKey(String primaryKey)                         |
| getContentByPrimaryKey(String primaryKey, String ddmTemplateKey)  |

## Purpose

It's common when using Liferay to want to change how web content is rendered inside of an asset publisher. Liferay makes this possible by creating a new Application Display Template or ADT for your asset publisher. When using an ADT if you want to get the web contents rendered content this could be done using the `serviceLocator` to access Liferay's `JournalArticleLocalService`. `serviceLocator` had to be enabled via the control panel, which isn't necessarily best to make globally available since it can access any Liferay service. With serviceLocator the content could be rendered like so:

```
<#if serviceLocator??>
	<#if className == "com.liferay.journal.model.JournalArticle">
		<#assign journalArticle = assetRenderer.getArticle() />
		<#assign JournalArticleLocalService = serviceLocator.findService("com.liferay.journal.service.JournalArticleLocalService") />

		<#assign content = JournalArticleLocalService.getArticleContent(journalArticle, "{templateID}", "view", themeDisplay.getLocale().toString(), themeDisplay) />

		${content}
	</#if>
</#if>
```

Alternatively, this module creates a [Template Context Contributor](https://help.liferay.com/hc/en-us/articles/360029006112-Template-Context-Contributor) that provides access to a few simplified methods for rendering web content. This keeps you from needing to expose serviceLocator and also simplifies the api, removing the need to pass large object like themeDisplay. 

### Example Usage in a Web Content Template

Web content structures allow you to select "Web Content" as a field type. When this is selected you can access and render the web content using the following method.

```
<#assign webContentData = jsonFactoryUtil.createJSONObject(WebContent19m8.getData())/>

// Render with the default template
${articleService.getContentByClassPK(webContentData.classPK?number)}

// Optionally pass the ddmTemplateKey
${articleService.getContentByClassPK(webContentData.classPK?number, "12345")}
```

### Example Usage in an Asset Publisher Template

```
<#if entries?has_content>
    <div class="d-flex flex-wrap">
    	<#list entries as curEntry>
    	    <div class="w-25 flex-shrink-0 p-3">
        		${articleService.getContentByClassPK(curEntry.getClassPK()?number)}
        	</div>
    	</#list>
    </div>
</#if>
```

## How to Build and Deploy to Liferay

In order to build or deploy this module you will need to [install Blade CLI](https://help.liferay.com/hc/en-us/articles/360028833852-Installing-Blade-CLI).

### To Build

`$ blade gw build`

You can find the built modules at `modules/{module-name}/build/libs/{module-name}.jar`.

### To Deploy

In `gradle-local.properties` add the following line to point towards the Liferay instance you want to deploy to:
```
liferay.workspace.home.dir=/path/to/liferay/home
```

## Issues & Questions Welcome