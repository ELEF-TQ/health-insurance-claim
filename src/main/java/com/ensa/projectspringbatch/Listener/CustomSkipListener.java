package com.ensa.projectspringbatch.Listener;

import com.ensa.projectspringbatch.Model.Dossier;
import org.springframework.batch.core.SkipListener;

public class CustomSkipListener implements SkipListener<Dossier, Dossier> {

    @Override
    public void onSkipInRead(Throwable t) {
        System.out.println("Skipped during read: " + t.getMessage());
    }

    @Override
    public void onSkipInWrite(Dossier item, Throwable t) {
        System.out.println("Skipped during write for item: " + item + ", Error: " + t.getMessage());
    }

    @Override
    public void onSkipInProcess(Dossier item, Throwable t) {
        System.out.println("Skipped during process for item: " + item + ", Error: " + t.getMessage());
    }
}