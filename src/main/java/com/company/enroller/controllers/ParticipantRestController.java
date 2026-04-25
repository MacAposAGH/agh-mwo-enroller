package com.company.enroller.controllers;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipants(
            @RequestParam(name = "sortBy") Optional<String> sortBy,
            @RequestParam(name = "sortOrder") Optional<String> sortOrder,
            @RequestParam(name = "key") Optional<String> key) {
        Collection<Participant> participants = participantService.getAll(sortBy, sortOrder, key);
        return new ResponseEntity<>(participants, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
        Participant participant = participantService.findByLogin(login);
        if (participant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(participant, HttpStatus.OK);
    }

    private ResponseEntity<?> participantAlreadyExists(String login) {
        return new ResponseEntity<>("Unable to create. A participant with login " + login + " already exist.",
                HttpStatus.CONFLICT);
    }

    private ResponseEntity<?> participantDoesntExists(String login) {
        return new ResponseEntity<>("Unable to create. A participant with login " + login + " doesn't exist.",
                HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
        String login = participant.getLogin();
        if (participantService.findByLogin(login) != null) {
            return participantAlreadyExists(login);
        }
        return new ResponseEntity<>(participantService.addParticipant(participant), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateParticipant(@PathVariable("id") String login,
                                               @RequestBody Participant participant) {
        if (participantService.findByLogin(login) == null) {
            return participantDoesntExists(login);
        }
        participantService.updateParticipant(login, participant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login) {
        Participant foundParticipant = participantService.findByLogin(login);
        if (foundParticipant == null) {
            return participantDoesntExists(login);
        }
        participantService.deleteParticipant(foundParticipant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
