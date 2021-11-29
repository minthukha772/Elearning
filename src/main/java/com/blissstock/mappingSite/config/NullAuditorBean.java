package com.blissstock.mappingSite.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class NullAuditorBean implements AuditorAware {

    @Override
    public Optional getCurrentAuditor() {
        return null;
    }
}