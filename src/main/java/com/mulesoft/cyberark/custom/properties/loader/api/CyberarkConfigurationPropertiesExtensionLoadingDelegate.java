/*
 * (c) 2003-2021 MuleSoft, Inc. This software is protected under international copyright
 * law. All use of this software is subject to MuleSoft's Master Subscription Agreement
 * (or other master license agreement) separately entered into in writing between you and
 * MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package com.mulesoft.cyberark.custom.properties.loader.api;

import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.api.meta.Category.SELECT;

import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclarer;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.ExtensionLoadingDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Declares extension for Cyberark Properties Loader Extension module
 *
 * @since 1.0
 */
public class CyberarkConfigurationPropertiesExtensionLoadingDelegate implements ExtensionLoadingDelegate {

  public static final String EXTENSION_NAME = "Cyberark Properties Loader";
  public static final String CONFIG_ELEMENT = "config";
  public static final String CYBERARK_PARAMETER_GROUP = "Token Connection";
    
  private final static Logger LOGGER = LoggerFactory.getLogger(CyberarkConfigurationPropertiesExtensionLoadingDelegate.class);

  @Override
  public void accept(ExtensionDeclarer extensionDeclarer, ExtensionLoadingContext context) {
      ConfigurationDeclarer configurationDeclarer = extensionDeclarer.named(EXTENSION_NAME)
        .describedAs(String.format("Crafted %s Extension", EXTENSION_NAME))
        .withCategory(SELECT)
        .onVersion("1.0.0")
        .fromVendor("MuleSoft")
        // This defines a global element in the extension with name config
        .withConfig(CONFIG_ELEMENT);
    
      addCyberarkTokenParameters(configurationDeclarer);
      
  }
  
  /**
   * Add the Basic Connection parameters to the parameter list
   *
   * @param configurationDeclarer Extension {@link ConfigurationDeclarer}
   */
  private void addCyberarkTokenParameters(ConfigurationDeclarer configurationDeclarer) {
	  ParameterGroupDeclarer vaultTokenParametersGroup = configurationDeclarer
			  .onParameterGroup(CYBERARK_PARAMETER_GROUP)
			  .withDslInlineRepresentation(true);
	  
	  ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();

	    LOGGER.info("Add the Cyberark Parameter Group");
	    
		
	    vaultTokenParametersGroup
		    .withRequiredParameter("cyberarkHost")
		    .withDisplayModel(DisplayModel.builder().displayName("Cyberark Host").build())
		    .ofType(BaseTypeBuilder.create(JAVA).stringType().build())
		    .withExpressionSupport(ExpressionSupport.SUPPORTED)
		    .describedAs("Cyberark Host");
		
	    vaultTokenParametersGroup
		    .withRequiredParameter("cyberarkPort")
		    .withDisplayModel(DisplayModel.builder().displayName("Cyberark Port").build())
		    .ofType(BaseTypeBuilder.create(JAVA).stringType().build())
		    .withExpressionSupport(ExpressionSupport.SUPPORTED)
		    .describedAs("Cyberark Port");
		
		
	    vaultTokenParametersGroup
		    .withRequiredParameter("cyberarkPath")
		    .withDisplayModel(DisplayModel.builder().displayName("Cyberark Path").build())
		    .ofType(BaseTypeBuilder.create(JAVA).stringType().build())
		    .withExpressionSupport(ExpressionSupport.SUPPORTED)
		    .describedAs("Cyberark Path");
	    
  }
}
