package com.tallerjava.tallerjava.Compra.dominio;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstadoCompraConverter implements AttributeConverter <EnumEstadoCompra, Integer> {


    @Override
    public Integer convertToDatabaseColumn(EnumEstadoCompra e) {
        return (e == null ? null : e.getCodigo());   // tu getCodigo() devuelve 1,2,3…
    }

    @Override
    public EnumEstadoCompra convertToEntityAttribute(Integer db) {
        return (db == null
                ? null
                : EnumEstadoCompra.fromCodigo(db));      // un static que mapea int→enum
    }
}
