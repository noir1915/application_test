package com.example.application.util;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

public class XmlUtils {

    public static Source loadResourceAsSource(String resourcePath) {
        return new StreamSource(XmlUtils.class.getClassLoader().getResourceAsStream(resourcePath));
    }
}
