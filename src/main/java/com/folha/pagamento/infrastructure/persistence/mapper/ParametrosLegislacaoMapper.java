package com.folha.pagamento.infrastructure.persistence.mapper;

import com.folha.pagamento.domain.model.FaixaInss;
import com.folha.pagamento.domain.model.FaixaIrrf;
import com.folha.pagamento.domain.model.ParametrosLegislacao;
import com.folha.pagamento.infrastructure.persistence.entity.FaixaInssJpaEntity;
import com.folha.pagamento.infrastructure.persistence.entity.FaixaIrrfJpaEntity;
import com.folha.pagamento.infrastructure.persistence.entity.ParametrosLegislacaoJpaEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParametrosLegislacaoMapper {

    public static ParametrosLegislacao toDomain(ParametrosLegislacaoJpaEntity jpa) {
        if (jpa == null) return null;

        List<FaixaInss> faixasInss = jpa.getFaixasInss() != null 
                ? jpa.getFaixasInss().stream().map(ParametrosLegislacaoMapper::toDomain).collect(Collectors.toList())
                : new ArrayList<>();

        List<FaixaIrrf> faixasIrrf = jpa.getFaixasIrrf() != null 
                ? jpa.getFaixasIrrf().stream().map(ParametrosLegislacaoMapper::toDomain).collect(Collectors.toList())
                : new ArrayList<>();

        return new ParametrosLegislacao(
                jpa.getId(),
                jpa.getAno(),
                jpa.getMesInicio(),
                jpa.getSalarioMinimo(),
                jpa.getTetoInss(),
                jpa.getValorDependenteIrrf(),
                jpa.getDescontoSimplificadoIrrf(),
                jpa.getAliquotaFgtsClt(),
                jpa.getAliquotaFgtsAprendiz(),
                jpa.getAliquotaInssPatronalAprendiz(),
                faixasInss,
                faixasIrrf
        );
    }

    public static FaixaInss toDomain(FaixaInssJpaEntity jpa) {
        if (jpa == null) return null;
        return new FaixaInss(
                jpa.getId(),
                jpa.getFaixaOrdem(),
                jpa.getLimiteMinimo(),
                jpa.getLimiteMaximo(),
                jpa.getAliquota()
        );
    }

    public static FaixaIrrf toDomain(FaixaIrrfJpaEntity jpa) {
        if (jpa == null) return null;
        return new FaixaIrrf(
                jpa.getId(),
                jpa.getFaixaOrdem(),
                jpa.getLimiteMinimo(),
                jpa.getLimiteMaximo(),
                jpa.getAliquota(),
                jpa.getParcelaDeduzir()
        );
    }
}
