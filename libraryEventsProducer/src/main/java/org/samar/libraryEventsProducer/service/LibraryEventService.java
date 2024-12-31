package org.samar.libraryEventsProducer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.samar.libraryEventsProducer.producer.LibraryEventsProducer;
import org.samar.libraryEventsProducer.record.LibraryEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LibraryEventService
{
    @Autowired
    private LibraryEventsProducer producer;

    public ResponseEntity<LibraryEvents> createLibraryEvent(LibraryEvents libraryEvent) throws JsonProcessingException
    {

        //System.out.println(libraryEvent.toString());
        //return null;
        //producer.sendLibraryEvents2(libraryEvent);
        producer.sendLibraryEventsSynchronous(libraryEvent);
        System.out.println("This is the end of the message!");
        return new ResponseEntity<>(libraryEvent, HttpStatus.CREATED);
    }
}
