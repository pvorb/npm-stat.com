package de.vorb.npmstat.services;

import lombok.Data;

import java.time.LocalDate;

@Data
class Gap {

    private final LocalDate from;
    private final LocalDate to;

}
