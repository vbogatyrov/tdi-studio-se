package com.sforce.soap.partner;

/**
 * Generated by ComplexTypeCodeGenerator.java. Please do not edit.
 */
public interface IMassEmailMessage extends com.sforce.soap.partner.IEmail {

      /**
       * element : description of type {http://www.w3.org/2001/XMLSchema}string
       * java type: java.lang.String
       */

      public java.lang.String getDescription();

      public void setDescription(java.lang.String description);

      /**
       * element : targetObjectIds of type {urn:partner.soap.sforce.com}ID
       * java type: java.lang.String[]
       */

      public java.lang.String[] getTargetObjectIds();

      public void setTargetObjectIds(java.lang.String[] targetObjectIds);

      /**
       * element : templateId of type {urn:partner.soap.sforce.com}ID
       * java type: java.lang.String
       */

      public java.lang.String getTemplateId();

      public void setTemplateId(java.lang.String templateId);

      /**
       * element : whatIds of type {urn:partner.soap.sforce.com}ID
       * java type: java.lang.String[]
       */

      public java.lang.String[] getWhatIds();

      public void setWhatIds(java.lang.String[] whatIds);


}
