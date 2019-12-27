package com.example.util;

import android.util.Log;

import java.io.File;

import com.example.spt.jaxb.course.Manifest;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamUtil {
    /**
     * 将XML文件解析成实体类对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T str2JavaFormXML(File file, Class<T> c) {
        T t = null;
        try {

            XStream xStream = new XStream(new DomDriver());

            xStream.autodetectAnnotations(true);

            xStream.processAnnotations(c);
            // xStream.alias("manifest", Manifest.class);
	       /* xStream.alias("organization", Organization.class);
	        xStream.addImplicitCollection(Manifest.class, "organization", Organization.class);*/

            t = (T)xStream.fromXML(file);

        } catch (Exception e) {

            e.printStackTrace();
        }
        return t;
    }
}
