package com.tallerjava.tallerjava.Compra.dominio;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

public enum EnumEstadoCompra {

    PROCESANDOSE(1), APROBADA(2), DESAPROBADA(3);


    private final int codigo;

    EnumEstadoCompra(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static EnumEstadoCompra fromCodigo(int codigo) {
        for (EnumEstadoCompra e : values()) {
            if (e.codigo == codigo) {
                return e;
            }
        }
        throw new IllegalArgumentException("Código de estado desconocido: " + codigo);
    }

    @Converter(autoApply = false)
    public class EstadoCompraConverter implements AttributeConverter<EnumEstadoCompra, Integer> {
        @Override
        public Integer convertToDatabaseColumn(EnumEstadoCompra attribute) {
            return attribute != null ? attribute.getCodigo() : null;
        }

        @Override
        public EnumEstadoCompra convertToEntityAttribute(Integer dbData) {
            return EnumEstadoCompra.fromCodigo(dbData);
        }

    }
}