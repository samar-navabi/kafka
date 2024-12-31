package org.samar.libraryEventsProducer.record;

public record LibraryEvents(
        Integer libraryEventId, String libraryEventType,
        Book book
) {
}
