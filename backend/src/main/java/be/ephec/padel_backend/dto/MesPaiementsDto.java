package be.ephec.padel_backend.dto;

import java.math.BigDecimal;

public class MesPaiementsDto {
    private BigDecimal montantDu;
    private BigDecimal montantPaye;
    private BigDecimal soldeRestant;

    public MesPaiementsDto(BigDecimal montantDu, BigDecimal montantPaye, BigDecimal soldeRestant) {
        this.montantDu = montantDu;
        this.montantPaye = montantPaye;
        this.soldeRestant = soldeRestant;
    }

    public BigDecimal getMontantDu() { return montantDu; }
    public BigDecimal getMontantPaye() { return montantPaye; }
    public BigDecimal getSoldeRestant() { return soldeRestant; }
}