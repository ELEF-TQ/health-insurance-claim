package com.ensa.projectspringbatch.Processor;

import com.ensa.projectspringbatch.Model.Dossier;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalculProcessor implements ItemProcessor<Dossier, Dossier> {

    private final ConsultationProcessor consultationProcessor;
    private final TraitementMappingProcessor traitementMappingProcessor;
    private final TraitementRemboursementProcessor traitementRemboursementProcessor;
    private final TotalRemboursementProcessor totalRemboursementProcessor;

    @Override
    public Dossier process(Dossier dossier) {
        dossier = consultationProcessor.process(dossier);
        dossier = traitementMappingProcessor.process(dossier);
        dossier = traitementRemboursementProcessor.process(dossier);
        dossier = totalRemboursementProcessor.process(dossier);
        return dossier;
    }
}
