package org.jasig.portlet.courses.dao.xml;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

import org.jasig.portlet.courses.model.wrapper.CourseSummaryWrapper;
import org.jasig.portlet.courses.model.wrapper.ObjectFactoryWrapper;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.xml.AbstractJaxb2HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class Jaxb2CourseSummaryHttpMessageConverter extends AbstractJaxb2HttpMessageConverter<Object> {

    private final ConcurrentMap<Class, JAXBContext> jaxbContexts = new ConcurrentHashMap<Class, JAXBContext>();

    protected boolean isSupported(MediaType mediaType) {
        if (mediaType == null) {
            return true;
        }
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            if (supportedMediaType.includes(mediaType)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return (clazz.isAnnotationPresent(XmlRootElement.class) || clazz.isAnnotationPresent(XmlType.class) || clazz.getPackage().equals(CourseSummaryWrapper.class.getPackage())) &&
                isSupported(mediaType);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return AnnotationUtils.findAnnotation(clazz, XmlRootElement.class) != null && isSupported(mediaType);
    }
    
    /**
     * Creates a new {@link Marshaller} for the given class.
     *
     * @param clazz the class to create the marshaller for
     * @return the {@code Marshaller}
     * @throws HttpMessageConversionException in case of JAXB errors
     */
    protected final Marshaller createWrapperMarshaller(Class clazz) {
        try {
            JAXBContext jaxbContext = getJaxbContext(clazz);
            return jaxbContext.createMarshaller();
        }
        catch (JAXBException ex) {
            throw new HttpMessageConversionException(
                    "Could not create Marshaller for class [" + clazz + "]: " + ex.getMessage(), ex);
        }
    }

    /**
     * Creates a new {@link Unmarshaller} for the given class.
     *
     * @param clazz the class to create the unmarshaller for
     * @return the {@code Unmarshaller}
     * @throws HttpMessageConversionException in case of JAXB errors
     */
    protected final Unmarshaller createWrapperUnmarshaller(Class clazz) throws JAXBException {
        try {
            JAXBContext jaxbContext = getJaxbContext(clazz);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setProperty("com.sun.xml.bind.ObjectFactory", new ObjectFactoryWrapper());
            return unmarshaller;
        }
        catch (JAXBException ex) {
            throw new HttpMessageConversionException(
                    "Could not create Unmarshaller for class [" + clazz + "]: " + ex.getMessage(), ex);
        }
    }

    /**
     * Returns a {@link JAXBContext} for the given class.
     *
     * @param clazz the class to return the context for
     * @return the {@code JAXBContext}
     * @throws HttpMessageConversionException in case of JAXB errors
     */
    protected final JAXBContext getWrapperJaxbContext(Class clazz) {
        Assert.notNull(clazz, "'clazz' must not be null");
        JAXBContext jaxbContext = jaxbContexts.get(clazz);
        if (jaxbContext == null) {
            try {
                jaxbContext = JAXBContext.newInstance(clazz);
                jaxbContexts.putIfAbsent(clazz, jaxbContext);
            }
            catch (JAXBException ex) {
                throw new HttpMessageConversionException(
                        "Could not instantiate JAXBContext for class [" + clazz + "]: " + ex.getMessage(), ex);
            }
        }
        return jaxbContext;
    }    
    @Override
    protected Object readFromSource(Class<? extends Object> clazz,
            HttpHeaders headers, Source source) throws IOException {
        try {
            Unmarshaller unmarshaller = createWrapperUnmarshaller(clazz);
            if (clazz.isAnnotationPresent(XmlRootElement.class)) {
                return unmarshaller.unmarshal(source);
            }
            else {
                JAXBElement<? extends Object> jaxbElement = unmarshaller.unmarshal(source, clazz);
                return jaxbElement.getValue();
            }
        }
        catch (UnmarshalException ex) {
            throw new HttpMessageNotReadableException("Could not unmarshal to [" + clazz + "]: " + ex.getMessage(), ex);

        }
        catch (JAXBException ex) {
            throw new HttpMessageConversionException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
        }
    }

    @Override
    protected void writeToResult(Object o, HttpHeaders headers, Result result)
            throws IOException {
        try {
            Class clazz = ClassUtils.getUserClass(o);
            Marshaller marshaller = createWrapperMarshaller(clazz);
            setCharset(headers.getContentType(), marshaller);
            marshaller.marshal(o, result);
        }
        catch (MarshalException ex) {
            throw new HttpMessageNotWritableException("Could not marshal [" + o + "]: " + ex.getMessage(), ex);
        }
        catch (JAXBException ex) {
            throw new HttpMessageConversionException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
        }
    }

    @Override
    protected boolean supports(Class<?> arg0) {
        // should not be called, since we override canRead/Write
        throw new UnsupportedOperationException();
    }

    private void setCharset(MediaType contentType, Marshaller marshaller) throws PropertyException {
        if (contentType != null && contentType.getCharSet() != null) {
            marshaller.setProperty(Marshaller.JAXB_ENCODING, contentType.getCharSet().name());
        }
    }
}
