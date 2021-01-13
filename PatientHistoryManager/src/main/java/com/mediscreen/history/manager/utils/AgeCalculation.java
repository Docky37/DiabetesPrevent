package com.mediscreen.history.manager.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * This util class contains a static method use to calculate the current age of a person from his given birth date.
 *
 * @author Thierry SCHREINER
 * @since 2021
 */
public final class AgeCalculation {

    /**
     * Private no args empty constructor.
     */
    private AgeCalculation() {
    }

    /**
     * This method returns the interval in full years between a given date and today.
     *
     * @param birthDate
     * @return an int
     */
    public static int calculateAge(final LocalDate birthDate) {
        return (int) birthDate.until(LocalDate.now(), ChronoUnit.YEARS);
    }
}
