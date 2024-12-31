package org.samar.libraryEventsProducer.controller;

import org.samar.libraryEventsProducer.record.LibraryEvents;
import org.samar.libraryEventsProducer.service.LibraryEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
public class LibraryEventController
{
    @Autowired
    private LibraryEventService libraryEventService;

    @PostMapping("/v1/libraryEvent")
    public ResponseEntity<LibraryEvents> postLibraryEvent(@RequestBody LibraryEvents libraryEvent) throws Exception
    {
        return libraryEventService.createLibraryEvent(libraryEvent);
    }
}
