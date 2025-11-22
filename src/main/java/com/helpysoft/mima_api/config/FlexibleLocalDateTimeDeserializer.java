package com.helpysoft.mima_api.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Deserializer personnalisé pour LocalDateTime qui accepte :
 * - Format complet avec heure : "2025-11-24T12:00:00"
 * - Format date seule : "2025-11-24" (converti en "2025-11-24T00:00:00")
 */
public class FlexibleLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        String dateString = jsonParser.getText();

        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        try {
            // Essayer d'abord de parser comme LocalDateTime (format complet avec heure)
            return LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            try {
                // Si ça échoue, essayer de parser comme LocalDate et ajouter l'heure à minuit
                LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
                return date.atStartOfDay(); // Convertit en LocalDateTime à 00:00:00
            } catch (DateTimeParseException ex) {
                throw new IOException("Impossible de parser la date: " + dateString +
                    ". Formats acceptés: 'yyyy-MM-dd' ou 'yyyy-MM-ddTHH:mm:ss'", ex);
            }
        }
    }
}
