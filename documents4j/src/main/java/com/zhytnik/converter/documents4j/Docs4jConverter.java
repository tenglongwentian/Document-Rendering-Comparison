package com.zhytnik.converter.documents4j;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.zhytnik.converter.common.Converter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author Alexey Zhytnik
 * @since 31.08.2016
 */
abstract class Docs4jConverter implements Converter<InputStream, ByteArrayOutputStream> {

    @Override
    public ByteArrayOutputStream convert(InputStream document) throws Exception {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        boolean conversion = buildConverter()
                .convert(document).as(getType())
                .to(output).as(DocumentType.PDF)
                .execute();

        if (!conversion) throw new RuntimeException("Conversion failed");
        return output;
    }

    private IConverter buildConverter() {
        return LocalConverter.builder()
                .baseFolder(null /*temporary resource folder*/)
                .workerPool(1, 1, 1, TimeUnit.SECONDS)
                .processTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    protected abstract DocumentType getType();
}
