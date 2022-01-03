/*
 * (c) 2003-2021 MuleSoft, Inc. This software is protected under international copyright
 * law. All use of this software is subject to MuleSoft's Master Subscription Agreement
 * (or other master license agreement) separately entered into in writing between you and
 * MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package com.mulesoft.cyberark.custom.properties.loader.api;

import static com.mulesoft.cyberark.custom.properties.loader.api.CyberarkConfigurationPropertiesExtensionLoadingDelegate.CONFIG_ELEMENT;
import static com.mulesoft.cyberark.custom.properties.loader.api.CyberarkConfigurationPropertiesExtensionLoadingDelegate.EXTENSION_NAME;
import static com.mulesoft.cyberark.custom.properties.loader.api.CyberarkConfigurationPropertiesExtensionLoadingDelegate.CYBERARK_PARAMETER_GROUP;
import static org.mule.runtime.api.component.ComponentIdentifier.builder;

import java.util.List;

import org.mule.runtime.api.component.ComponentIdentifier;
import org.mule.runtime.config.api.dsl.model.ConfigurationParameters;
import org.mule.runtime.config.api.dsl.model.ResourceProvider;
import org.mule.runtime.config.api.dsl.model.properties.ConfigurationPropertiesProvider;
import org.mule.runtime.config.api.dsl.model.properties.ConfigurationPropertiesProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builds the provider for a custom-properties-provider:config element.
 *
 * @since 1.0
 */
public class CyberarkConfigurationPropertiesProviderFactory implements ConfigurationPropertiesProviderFactory {
 
  public static final String EXTENSION_NAMESPACE = EXTENSION_NAME.toLowerCase().replace(" ", "-");
  public static final String CYBERARK_PARAMETER_GROUP_NAME = CYBERARK_PARAMETER_GROUP.toLowerCase().replace(" ", "-");
  private static final ComponentIdentifier CUSTOM_PROPERTIES_PROVIDER =
      builder().namespace(EXTENSION_NAMESPACE).name(CONFIG_ELEMENT).build();
  
  private final static Logger LOGGER = LoggerFactory.getLogger(CyberarkConfigurationPropertiesProviderFactory.class);

  @Override
  public ComponentIdentifier getSupportedComponentIdentifier() {
    return CUSTOM_PROPERTIES_PROVIDER;
  }

  @Override
  public ConfigurationPropertiesProvider createProvider(ConfigurationParameters parameters,
                                                        ResourceProvider externalResourceProvider) {
	  
	// This is how you can access the configuration parameter of the <custom-properties-provider:config> element.
	LOGGER.info("List Cyberark Parameters");
	List<ConfigurationParameters> vaultTokenList = parameters
	        .getComplexConfigurationParameter(builder()
	                .namespace(EXTENSION_NAMESPACE)
	                .name(CYBERARK_PARAMETER_GROUP_NAME).build());
	
	ConfigurationParameters token = vaultTokenList.get(0);
	
	 
	  try {
		  LOGGER.info("Connecting to Cyberark Key Service");
		  return new CyberarkConfigurationPropertiesProvider(geCyberarkConn(parameters));
	  } catch (Exception e) {
	      LOGGER.error("Error connecting to Cyberark Key Service", e);
	      return null;
	  }
  }
	  
  private CyberarkConnectionProperties geCyberarkConn(ConfigurationParameters parameters) {
	  	  
	  String cyberarkHostValue = null;
	  String cyberarkPortValue = null;
	  String cyberarkPathValue = null;
	  
	  List<ConfigurationParameters> vaultTokenList = parameters
	            .getComplexConfigurationParameter(builder()
	                    .namespace(EXTENSION_NAMESPACE)
	                    .name(CYBERARK_PARAMETER_GROUP_NAME).build());
	  
	  ConfigurationParameters token = vaultTokenList.get(0);
	  
	 
	 
	  try {
		  cyberarkHostValue = token.getStringParameter("cyberarkHost");
	    } catch (Exception e) {
	      LOGGER.error("Cyberark Host not present");
	      throw new RuntimeException("Cyberark Host parameter not present");
	  }
	  try {
		  cyberarkPortValue = token.getStringParameter("cyberarkPort");
	    } catch (Exception e) {
	      LOGGER.error("Cyberark Port not present");
	      throw new RuntimeException("Cyberark Port parameter not present");
	  }
	  try {
		  cyberarkPathValue = token.getStringParameter("cyberarkPath");
	    } catch (Exception e) {
	      LOGGER.error("Cyberark Path not present");
	      throw new RuntimeException("Cyberark Path parameter not present");
	  }
	  
	 
	 
	  CyberarkConnectionProperties cConn = null;
	  
	  try {
		  LOGGER.info("Establish Cyberark Connection");
		  cConn = new CyberarkConnectionProperties(cyberarkHostValue, cyberarkPortValue, cyberarkPathValue);
		  return cConn;
  
      } catch (Exception e){
    	  LOGGER.error(" Error Occured while making Cyberark connection: " + e.getMessage());
    	  throw new RuntimeException("Error Occured while making Cyberark connection: " + e.getMessage());
      }	  
  }  
}
