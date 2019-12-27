package com.example.spt.jaxb.course;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 璇句欢鎻忚堪
 * @author zxh
 *
 */
@XStreamAlias("metadata")
public class Metadata implements java.io.Serializable{
    /**
     * 支持的标准，默认为SCORM标准
     */
    @XStreamAsAttribute
    String schema="ADL SCORM";
    /**
     * 标准的版本号：默认为1.2版本
     */
    @XStreamAsAttribute
    String schemaversion = "1.2";


    public Metadata() {
        super();
    }

    public String getSchema() {
        return schema;
    }
    public void setSchema(String schema) {
        this.schema = schema;
    }
    public String getSchemaversion() {
        return schemaversion;
    }
    public void setSchemaversion(String schemaversion) {
        this.schemaversion = schemaversion;
    }


}
